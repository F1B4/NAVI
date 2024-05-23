"""
AI커버곡 비디오 제작 모듈
"""
import os
import cv2
import numpy as np
import requests
import datetime
import subprocess
from PIL import Image, ImageDraw, ImageFont
from pydub import AudioSegment
from server_package.db_connection import get_connection

# 경로 변수 설정
BASE_PATH = "/home/navi/rvcModel"
LYRICS_IMAGES_PATH = f"{BASE_PATH}/lyrics_images"
FONT_PATH = f"{BASE_PATH}/server_package/YeojuCeramic TTF.ttf"
PROCESSED_AUDIO_PATH = f"{BASE_PATH}/datasets/processed"
ORIGINAL_AUDIO_PATH = f"{BASE_PATH}/datasets/original"

def fetch_data_from_db(cover_pk):
    # MySQL 데이터베이스 연결
    connection = get_connection()

    try:
        with connection.cursor() as cursor:
            # cover에서 song_pk 조회
            query_cover = "SELECT song_pk FROM cover WHERE cover_pk = %s"
            cursor.execute(query_cover, (cover_pk,))
            song_pk = cursor.fetchone()['song_pk']
            
            # cover_user에서 part_pk와 user_pk 정보 조회
            query_cover_user = "SELECT cu.part_pk, cu.user_pk, u.nickname, u.image as profile_image FROM cover_user cu JOIN user u ON cu.user_pk = u.user_pk WHERE cu.cover_pk = %s"
            cursor.execute(query_cover_user, (cover_pk,))
            cover_users = cursor.fetchall()
            
            # Lyrics에서 가사 정보 조회
            query_lyrics = "SELECT * FROM lyric WHERE song_pk = %s ORDER BY sequence"
            cursor.execute(query_lyrics, (song_pk,))
            lyrics_info = cursor.fetchall()
            
            # part에서 파트 이름 조회
            query_part = "SELECT part_pk, name FROM part WHERE song_pk = %s"
            cursor.execute(query_part, (song_pk,))
            parts = cursor.fetchall()
            part_names = {part['part_pk']: part['name'] for part in parts}

            # user 이미지 경로 조회
            user_images = {}
            for cover_user in cover_users:
                user_pk = cover_user['user_pk']
                if user_pk not in user_images:
                    user_images[user_pk] = {'image': cover_user['profile_image'], 'nickname': cover_user['nickname']}
    finally:
        connection.close()

    return lyrics_info, cover_users, part_names, user_images

"""
이미지의 크기를 동적으로 결정하여 세로를 target_height에 맞추고, 중앙을 잘라 target_width에 맞추기
"""
def resize_and_crop_image(image, target_width, target_height, num_images):
    original_height, original_width = image.shape[:2]
    
    # 이미지의 총 개수에 따라 이미지의 너비를 동적으로 결정
    image_width = target_width // num_images
    
    # 이미지의 세로를 target_height에 맞추기 위한 비율 계산
    ratio = target_height / original_height
    
    # 이미지의 새로운 너비 계산
    new_width = int(original_width * ratio)
    
    # 이미지 리사이즈
    resized_image = cv2.resize(image, (new_width, target_height))
    
    # 만약 새로운 너비가 목표하는 너비보다 크다면 중앙을 잘라내고, 그렇지 않으면 이미지의 양 옆에 배경 추가
    if new_width > image_width:
        start_x = (new_width - image_width) // 2
        cropped_image = resized_image[:, start_x:start_x + image_width]
    else:
        delta = image_width - new_width
        left, right = delta // 2, delta - (delta // 2)
        cropped_image = cv2.copyMakeBorder(resized_image, 0, 0, left, right, cv2.BORDER_CONSTANT, value=[0, 0, 0])

    return cropped_image

"""
텍스트 주변에 테두리 그리기
"""
def draw_text_with_border(draw, position, text, font, text_color, border_color, border_width):
    x, y = position
    # 텍스트 테두리 그리기
    for angle in range(0, 360, 30):
        dx = border_width * np.cos(np.radians(angle))
        dy = border_width * np.sin(np.radians(angle))
        draw.text((x+dx, y+dy), text, font=font, fill=border_color)
    # 중심 텍스트 그리기
    draw.text(position, text, font=font, fill=text_color)

def create_lyrics_images_with_text(lyrics_info, cover_users, part_names, user_images, cover_pk):
    if not os.path.exists(LYRICS_IMAGES_PATH):
        os.makedirs(LYRICS_IMAGES_PATH)
    title_image_path = os.path.join(LYRICS_IMAGES_PATH, f"thumbnail_{cover_pk}.jpg")
    image_width = 1920
    image_height = 1080
    text_area_height = 400
    part_name_text_height = 50
    font_title_size = 100  # 제목 글씨 크기를 100으로 설정
    font_part = ImageFont.truetype(FONT_PATH, 40)
    font_title = ImageFont.truetype(FONT_PATH, font_title_size)
    font_lyric = ImageFont.truetype(FONT_PATH, 40)
    
    total_parts = len(part_names)
    user_image_width = image_width // total_parts
    
    images = []  # 이미지 데이터를 저장할 리스트를 초기화합니다.
    
    for index, lyric in enumerate(lyrics_info):
        combined_image = Image.new('RGB', (image_width, image_height), 'white')
        draw = ImageDraw.Draw(combined_image)
    
        for idx, part_pk in enumerate(part_names.keys()):
            part_name = part_names[part_pk]
            user_pk_list = [(cu['user_pk'], cu['nickname']) for cu in cover_users if cu['part_pk'] == part_pk]
    
            for user_pk, user_nickname in user_pk_list:
                user_info = user_images.get(user_pk)
                if user_info:
                    user_image_url = user_info['image']
                    if user_image_url:
                        response = requests.get(user_image_url)
                        if response.status_code == 200:
                            image_data = np.frombuffer(response.content, np.uint8)
                            user_image = cv2.imdecode(image_data, cv2.IMREAD_COLOR)
                            resized_image = resize_and_crop_image(user_image, user_image_width,
                                                                   image_height - text_area_height - part_name_text_height,
                                                                   len(user_pk_list))
    
                            # 첫 번째 가사 파트인 경우 모든 사용자 이미지를 컬러로 유지
                            if index == 0:
                                pass
                            # 가사의 내용이 특정 문자열인 경우 모든 사용자 이미지를 흑백으로 변환
                            elif "♪~♬~♪~♬" in lyric['content']:
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_BGR2GRAY)
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_GRAY2RGB)
                            # 해당 파트와 현재 가사의 파트가 일치하지 않으면 흑백 이미지로 변환
                            elif part_pk != lyric['part_pk']:
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_BGR2GRAY)
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_GRAY2RGB)
    
                            resized_image_rgb = cv2.cvtColor(resized_image, cv2.COLOR_BGR2RGB)
                            pil_resized_image = Image.fromarray(resized_image_rgb)
                            combined_image.paste(pil_resized_image, (idx * user_image_width, 0))
    
                            text_x = idx * user_image_width + (
                                        user_image_width - draw.textsize(part_name, font=font_part)[0]) / 2
                            text_y = image_height - text_area_height - part_name_text_height + 10
                            text = f"{part_name}\n\n{user_info['nickname']}"
                            text_width, text_height = draw.textsize(text, font=font_part)
                            draw.text(((idx + 0.5) * user_image_width - text_width / 2, text_y - 150), text,
                                      font=font_part,
                                      fill='white', align='center', stroke_width=2, stroke_fill='black')
    
        if index == 0:
            font = font_title
        else:
            font = font_lyric
    
        text_width, text_height = draw.textsize(lyric['content'], font=font)
        text_position = ((image_width - text_width) / 2,
                         image_height - text_area_height + (text_area_height - text_height) / 2)
        draw.text(text_position, lyric['content'], fill="black", font=font)
    
        final_image_data = np.array(combined_image)
        images.append(final_image_data)
    
        if index == 0:
            combined_image.save(title_image_path)
            print(f"Title image saved successfully: {title_image_path}")
    
        print(f"Image saved successfully")
    
    return images

def create_video_from_images(images, lyrics_info, output_video_path, frame_rate):
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    video_writer = cv2.VideoWriter(output_video_path, fourcc, frame_rate, (images[0].shape[1], images[0].shape[0]))

    for idx, image in enumerate(images):
        # 현재 이미지의 시간 정보 가져오기
        current_lyric = lyrics_info[idx]
        start_time = current_lyric['start_time']
        end_time = current_lyric['end_time']

        # 이미지를 해당 시간만큼 반복해서 추가
        duration = (datetime.datetime.strptime(end_time, '%H:%M:%S') - datetime.datetime.strptime(start_time, '%H:%M:%S')).total_seconds()
        repeat_times = int(frame_rate * duration)
        
        # PIL 이미지를 OpenCV 이미지로 변환하면서 RGB에서 BGR로 색상 순서 변경
        image_bgr = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        for _ in range(repeat_times):
            video_writer.write(image_bgr)

    video_writer.release()

def merge_audio_files(audio_files, output_path):
    # 첫 번째 오디오 파일을 기준으로 시작
    combined_audio = AudioSegment.silent(duration=0)  # 빈 오디오 세그먼트 생성

    # 오디오 파일 리스트를 순회하며 합치기
    for i, file_path in enumerate(audio_files):
        # 오디오 파일 불러오기
        current_audio = AudioSegment.from_file(file_path)
        
        # 마지막 파일인 경우 볼륨을 3.5dB 낮추기
        if i == len(audio_files) - 1:
            current_audio -= 3.5
        
        # 오디오 파일을 기존의 combined_audio 위에 겹쳐서 합치기
        if i == 0:
            combined_audio = current_audio
        else:
            combined_audio = combined_audio.overlay(current_audio)
    
    # 합쳐진 오디오 파일 내보내기
    combined_audio.export(output_path, format="wav")

def mix_and_merge_audio_with_video(video_path, audio_files, output_path):
    # 오디오 파일 합치기
    merged_audio_path = 'merged_audio.wav'
    merge_audio_files(audio_files, merged_audio_path)
    # 동영상 파일과 합쳐진 오디오 파일 합치기
    command = [
        'ffmpeg',
        '-y',
        '-i', video_path,            # 동영상 파일 경로
        '-i', merged_audio_path,     # 합쳐진 오디오 파일 경로
        '-c:v', 'libx264',           # 비디오 코덱 설정 (libx264으로 변경)
        '-crf', '28',                # 비디오 품질 설정 (압축 레벨을 조절하여 용량을 줄임, 기본값은 23)
        '-preset', 'slow',           # 압축 속도 및 품질 설정 (압축 속도가 느리지만 품질이 좋음)
        '-c:a', 'aac',               # 오디오 코덱 설정
        '-b:a', '128k',              # 오디오 비트레이트 설정
        '-strict', 'experimental',
        output_path                  # 출력 파일 경로
    ]
    subprocess.run(command, check=True)
    
    # 임시 파일 삭제
    os.remove(merged_audio_path)
