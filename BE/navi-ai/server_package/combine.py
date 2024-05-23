"""
MR과 목소리 합치기
"""
import os
from pydub import AudioSegment
import server_package.file_module as fm
import requests
import logging
from datetime import datetime
from server_package.db_connection import get_connection

# 경로 변수 설정
BASE_PATH = "/home/navi/rvcModel"
MR_PATH = f"{BASE_PATH}/datasets/mr"
WORK_PATH = f"{BASE_PATH}/server_package/noraebang"

def audioCombine(song_pk: str, path: str, noraebang_pk: str):
    try:
        connection = get_connection()
        # s3에서 목소리 파일 다운
        local_download_path = fm.download_files([path], WORK_PATH)

        mrAudio = AudioSegment.from_file(f"{MR_PATH}/{song_pk}.webm")
        voiceAudio = AudioSegment.from_file(local_download_path[0])
        mrAudio -= 2

        # 오디오 파일 합치기 (동시에 재생)
        combined = mrAudio.overlay(voiceAudio)

        # 합쳐진 파일 내보내기
        current_time = datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
        unique_file_name = f"{current_time}_{song_pk}"  # record 파일명 : 현재시간_songpk
        result_file = f"{WORK_PATH}/{unique_file_name}.mp3"
        combined.export(result_file, format="mp3")

        # s3에 업로드
        s3_path = fm.upload_file_to_s3([result_file], 'audio/mpeg')

        # db에 s3 경로 갱신
        with connection.cursor() as cursor:
            sql_record = "update noraebang set record=%s where noraebang_pk=%s"
            cursor.execute(sql_record, (s3_path, noraebang_pk))
            connection.commit()

        # 저장된 파일 삭제
        os.remove(result_file)
        os.remove(local_download_path[0])

        # 배포 spring 서버에 메시지 날리기
        response = requests.post(f'http://backend_server:port/api/noraebangs/complete/{noraebang_pk}')
        logging.info(f"send result to spring")
    except Exception as e:
        response = requests.post(f'http://backend_server:port/api/noraebangs/complete/{noraebang_pk}')
        logging.info(f"send result(fail) to spring")
    finally:
        connection.close()
