"""
AI커버곡 생성 요청 celery task
"""
from server_package.celery_app import celery_app
import os
from datetime import datetime
from pydub import AudioSegment
import subprocess
import torch
import requests
import logging
import server_package.makeVideo as mv
import server_package.file_module as fm
from server_package.db_connection import get_connection

# 경로 변수 설정
BASE_PATH = "/home/navi/rvcModel"
INFER_BASE_PATH = f"{BASE_PATH}/datasets/infer/"
RESULT_PATH = f"{BASE_PATH}/result/"
USERS_PATH = f"{BASE_PATH}/users/"
ORIGINAL_AUDIO_PATH = f"{INFER_BASE_PATH}original/"
LYRICS_IMAGES_PATH = f"{BASE_PATH}/lyrics_images/"

# 로깅 설정
logging.basicConfig(level=logging.INFO)

@celery_app.task(queue="infer_queue")
def infer_task(cover_pk):
    try:
        connection = get_connection()
        # 커버곡 생성 시작 메시지 Spring서버에 전달
        logging.info(f"Cover start response: {cover_pk}번 커버곡 생성 시작")

        # 어떤 유저가 어떤 노래, 어떤 파트 커버하는지 알아오기
        cover_users = []
        with connection.cursor() as cursor:
            sql_cover_user = f"SELECT * FROM cover_user WHERE cover_pk = {cover_pk}"
            cursor.execute(sql_cover_user)
            cover_users = cursor.fetchall()
            sql_song = f"SELECT song_pk FROM cover WHERE cover_pk = {cover_pk}"
            cursor.execute(sql_song)
            song_pk = cursor.fetchone()['song_pk']
        
        # 해당곡의 추론 데이터를 이미 만들어뒀다면 재사용
        infer_path = f"{INFER_BASE_PATH}{song_pk}"
        if not os.path.exists(infer_path):
            os.makedirs(infer_path)
            try:
                infer_datamake(cover_pk)
            except Exception as e:
                print(e)
        
        for cover_user in cover_users:
            user_pk = cover_user['user_pk']
            part_pk = cover_user['part_pk']

            input_path = f"{INFER_BASE_PATH}{song_pk}/{part_pk}.wav"
            output_path = f"{RESULT_PATH}{cover_pk}_{part_pk}.wav"
            pth_path = f"{USERS_PATH}{user_pk}.pth"
            index_path = f"{USERS_PATH}{user_pk}.index"
            command = [
                "python", "main.py", "infer",
                "--input_path", input_path,
                "--output_path", output_path,
                "--pth_path", pth_path,
                "--index_path", index_path,
                "--export_format", "WAV"
            ]
            subprocess.run(command, capture_output=True, text=True)
            torch.cuda.empty_cache()

        # 사용자 커버곡 목소리로 영상 만들기
        lyrics_info, cover_users, part_names, user_images = mv.fetch_data_from_db(cover_pk)
        images = mv.create_lyrics_images_with_text(lyrics_info, cover_users, part_names, user_images, cover_pk)
        # 이미지 데이터를 사용하여 동영상 생성
        frame_rate = 30
        output_video_path = f"{cover_pk}.mp4"
        mv.create_video_from_images(images, lyrics_info, output_video_path, frame_rate)
        print(f"Video saved successfully: {output_video_path}")

        video_path = output_video_path
        audio_files = []
        for i in part_names.keys():
            audio_files.append(f"{RESULT_PATH}{cover_pk}_{i}.wav")
        audio_files.append(f"{INFER_BASE_PATH}datasets/mr/{song_pk}.wav")
        output_path = f'res_{cover_pk}.mp4'
        mv.mix_and_merge_audio_with_video(video_path, audio_files, output_path)
        print("saved cover video")
        
        # 영상 처리 후 s3에 업로드
        s3_path = fm.upload_file_to_s3([output_path], 'video/mp4')
        # db 경로 url 처리
        url = f"s3_base_url/res_{cover_pk}.mp4"
        # db에 파일 정보 저장
        with connection.cursor() as cursor:
            sql_cover = f"update cover set video='{url}' where cover_pk={cover_pk}"
            cursor.execute(sql_cover)
            connection.commit()
            
        # 필요없는 폴더, 파일 삭제
        fm.remove_dir(LYRICS_IMAGES_PATH)
        fm.remove_files(BASE_PATH, f"{cover_pk}", "mp4")
        fm.remove_files(RESULT_PATH, f"{cover_pk}_", "wav")
        
        # 커버곡 생성 완료 후 Spring 으로 완료 메시지 전송
        # 추후에 localhost를 배포 주소로 바꿔야함
        complete_response = requests.post(f'https://backend_server:port/api/ai/cover/{cover_pk}')
        logging.info(f"Cover complete response: {complete_response}")
    
    except Exception as e:
        # 커버곡 생성 실패
        failure_response = requests.post(f'https://backend_server:port/api/ai/cover/{cover_pk}')
        logging.info(f"Cover Failure response: {failure_response}")
    finally:
        connection.close()

def is_empty_folder(folder_path):
    # 빈 폴더인지 확인
    return len(os.listdir(folder_path)) == 0

"""
추론곡 파트별 분배, 전처리
"""
def infer_datamake(cover_pk):
    connection = get_connection()

    try:
        with connection.cursor() as cursor:
            sql_cover = f"SELECT * FROM cover WHERE cover_pk = {cover_pk}"
            cursor.execute(sql_cover)
            
            # 결과 가져오기
            cover = cursor.fetchall()
            
            song_pk = cover[0]['song_pk']
            sql_lyric = f"SELECT * FROM lyric WHERE song_pk = {song_pk}"
            cursor.execute(sql_lyric)
            lyrics = cursor.fetchall()
            
            sql_cover_user = f"SELECT * FROM cover_user WHERE cover_pk = {cover_pk}"
            cursor.execute(sql_cover_user)
            cover_users = cursor.fetchall()

            allPart = []
            for r in cover_users:
                allPart.append([[r['part_pk']], [], []])
            
            for part in allPart:
                for lyric in lyrics:
                    if lyric['part_pk'] == part[0][0]:
                        part[1].append(lyric)
                        part[2].append((lyric['start_time'], lyric['end_time']))

        for part in allPart:
            # 오디오 파일 불러오기 (파트별 전처리 하기전 추론곡)
            audio_path = f"{ORIGINAL_AUDIO_PATH}{song_pk}.wav"
            audio = AudioSegment.from_file(audio_path, format="wav")
            
            # 노래가 끝나는 시간 설정
            song_end_time_ms = len(audio)

            # 무음 처리하지 말아야 할 구간 설정
            keep_silent_ranges = part[2]

            # 전체 노래에서 무음 처리할 구간 계산
            mute_ranges = []
            last_end_time_ms = 0

            # 각 구간을 순회하며 무음 처리할 구간 계산
            for start_time_str, end_time_str in keep_silent_ranges:
                start_time = datetime.strptime(start_time_str, "%H:%M:%S")
                end_time = datetime.strptime(end_time_str, "%H:%M:%S")
                start_time_ms = (start_time.hour * 3600 + start_time.minute * 60 + start_time.second) * 1000
                end_time_ms = (end_time.hour * 3600 + end_time.minute * 60 + end_time.second) * 1000
                
                # 이전 종료 시간과 현재 시작 시간 사이를 무음 처리할 구간으로 추가
                if last_end_time_ms < start_time_ms:
                    mute_ranges.append([last_end_time_ms, start_time_ms])
                last_end_time_ms = end_time_ms

            # 마지막 무음 처리하지 않을 구간과 노래 끝나는 시간 사이의 구간 추가
            if last_end_time_ms < song_end_time_ms:
                mute_ranges.append([last_end_time_ms, song_end_time_ms])

            # 계산된 무음 처리할 구간을 사용하여 오디오에서 무음 처리
            working_audio = audio[:]
            for start_ms, end_ms in mute_ranges:
                silence = AudioSegment.silent(duration=end_ms - start_ms)
                working_audio = working_audio[:start_ms] + silence + working_audio[end_ms:]

            output_folder = f"{INFER_BASE_PATH}{song_pk}"
            working_audio.export(f"{output_folder}/{part[0][0]}.wav", format="wav")
    finally:
        connection.close()
