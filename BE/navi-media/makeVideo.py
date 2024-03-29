#!/usr/bin/env python
# coding: utf-8

# In[1]:


import os
import cv2
import numpy as np
import pymysql
import requests
import datetime
import subprocess
import os
from PIL import Image, ImageDraw, ImageFont


# In[2]:


def fetch_data_from_db(cover_pk):
    # MySQL 데이터베이스 연결
    connection = pymysql.connect(host='j10d107.p.ssafy.io',
                                 port=3305,
                                 user='navi',
                                 password='navid107',
                                 database='navi',
                                 charset='utf8mb4',
                                 cursorclass=pymysql.cursors.DictCursor)

    try:
        with connection.cursor() as cursor:
            # cover에서 song_pk 조회
            query_cover = "SELECT song_pk FROM cover WHERE cover_pk = %s"
            cursor.execute(query_cover, (cover_pk,))
            song_pk = cursor.fetchone()['song_pk']
            
            # cover_user에서 part_pk와 user_pk 정보 조회
            query_cover_user = "SELECT part_pk, user_pk FROM cover_user WHERE cover_pk = %s"
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
                    query_user_image = "SELECT image FROM user WHERE user_pk = %s"
                    cursor.execute(query_user_image, (user_pk,))
                    user_image = cursor.fetchone()['image']
                    user_images[user_pk] = user_image
    finally:
        connection.close()

    return lyrics_info, cover_users, part_names, user_images

def resize_and_crop_image(image, target_width, target_height, num_images):
    """이미지의 크기를 동적으로 결정하여 세로를 target_height에 맞추고, 중앙을 잘라 target_width에 맞춥니다."""
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

def draw_text_with_border(draw, position, text, font, text_color, border_color, border_width):
    """텍스트 주변에 테두리를 그리는 함수"""
    x, y = position
    # 텍스트 테두리 그리기
    for angle in range(0, 360, 30):
        dx = border_width * np.cos(np.radians(angle))
        dy = border_width * np.sin(np.radians(angle))
        draw.text((x+dx, y+dy), text, font=font, fill=border_color)
    # 중심 텍스트 그리기
    draw.text(position, text, font=font, fill=text_color)

def create_lyrics_images_with_text(lyrics_info, cover_users, part_names, user_images,cover_pk):
    save_dir = "lyrics_images"
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)
    title_image_path = os.path.join(save_dir, f"thumbnail_{cover_pk}.jpg")
    image_width = 1920
    image_height = 1080
    text_area_height = 400
    part_name_text_height = 50
    font_title_size = 100  # 제목 글씨 크기를 100으로 설정
    font_path = "YeojuCeramic TTF.ttf"
    font_part = ImageFont.truetype(font_path, 40)
    font_title = ImageFont.truetype(font_path, font_title_size)
    font_lyric = ImageFont.truetype(font_path, 40)

    total_parts = len(part_names)
    user_image_width = image_width // total_parts

    images = []  # 이미지 데이터를 저장할 리스트를 초기화합니다.

    for index, lyric in enumerate(lyrics_info):
        combined_image = Image.new('RGB', (image_width, image_height), 'white')
        draw = ImageDraw.Draw(combined_image)

        for idx, part_pk in enumerate(part_names.keys()):
            part_name = part_names[part_pk]
            user_pk_list = [cu['user_pk'] for cu in cover_users if cu['part_pk'] == part_pk]
        
            for user_pk in user_pk_list:
                user_image_url = user_images.get(user_pk)
                if user_image_url:
                    response = requests.get(user_image_url)
                    if response.status_code == 200:
                        image_data = np.frombuffer(response.content, np.uint8)
                        user_image = cv2.imdecode(image_data, cv2.IMREAD_COLOR)
                        resized_image = resize_and_crop_image(user_image, user_image_width, image_height - text_area_height - part_name_text_height, len(user_pk_list))
        
                        # 해당 파트와 현재 가사의 파트가 일치하는지 확인
                        if index != 0:
                            # 해당 파트와 현재 가사의 파트가 일치하는지 확인
                            if part_pk != lyric['part_pk']:
                                # 파트가 일치하지 않으면 흑백 이미지로 변환
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_BGR2GRAY)
                                resized_image = cv2.cvtColor(resized_image, cv2.COLOR_GRAY2RGB)

                        # # 이미지를 BGR로 변환
                        # resized_image_bgr = cv2.cvtColor(resized_image, cv2.COLOR_RGB2BGR)
                        # 이미지를 RGB로 변환
                        # 이미지를 BGR에서 RGB로 변환
                        resized_image_rgb = cv2.cvtColor(resized_image, cv2.COLOR_BGR2RGB)
                        pil_resized_image = Image.fromarray(resized_image_rgb)  # RGB 이미지를 PIL 이미지로 변환
                        
                        combined_image.paste(pil_resized_image,(idx * user_image_width, 0))
        
                        # 텍스트 위치 조정 및 추가
                        text_x = idx * user_image_width + (user_image_width - draw.textsize(part_name,font=font_part)[0]) / 2
                        text_y = image_height - text_area_height - part_name_text_height + 10
        
                        draw_text_with_border(draw, (text_x, text_y - 80), part_name, font_part, 'white', 'black', 1)

        # 가사 추가
        if index == 0:  # 첫 번째 가사인 경우
            font = font_title  # 제목을 위한 글꼴 크기를 사용
        else:
            font = font_lyric
        # text_width, text_height = draw.textsize(lyric['content'], font=font_lyric)
        # draw.text(((image_width - text_width) / 2, image_height - text_area_height + (text_area_height - text_height) / 2), lyric['content'], fill="black", font=font_lyric)
        
        text_width, text_height = draw.textsize(lyric['content'], font=font)
        text_position = ((image_width - text_width) / 2, image_height - text_area_height + (text_area_height - text_height) / 2)
        # 텍스트 그리기 함수를 사용하거나 직접 그릴 수 있습니다.
        draw.text(text_position, lyric['content'], fill="black", font=font)

        
        # 최종 이미지 저장 및 이미지 데이터를 리스트에 추가합니다.
        final_image_data = np.array(combined_image)
        images.append(final_image_data)  # 이미지 데이터를 리스트에 추가합니다.
        
        if index == 0:  # 첫 번째 가사(제목)에 해당하는 이미지를 파일로 저장합니다.
            combined_image.save(title_image_path)
            print(f"Title image saved successfully: {title_image_path}")
            
        print(f"Image saved successfully")

    return images  # 생성된 이미지 데이터를 반환합니다.

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

# 함수 실행 예시
cover_pk = 8  # 예시 cover_pk 값
lyrics_info, cover_users, part_names, user_images = fetch_data_from_db(cover_pk)

images = create_lyrics_images_with_text(lyrics_info, cover_users, part_names, user_images,cover_pk)

# 이미지 데이터를 사용하여 동영상을 생성합니다.
frame_rate = 30
output_video_path = f"{cover_pk}.mp4"
create_video_from_images(images, lyrics_info, output_video_path, frame_rate)
print(f"Video saved successfully: {output_video_path}")


# In[3]:


def merge_audio_files(audio_files, output_path):
    # 오디오 파일 합치기
    command = ['ffmpeg']
    for audio_file in audio_files:
        command.extend(['-i', audio_file])
    command.extend(['-filter_complex', f'amix=inputs={len(audio_files)}:duration=first', output_path])
    subprocess.run(command, check=True)

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
        '-c:v', 'copy',              # 비디오 코덱 설정
        '-c:a', 'aac',               # 오디오 코덱 설정
        '-strict', 'experimental',
        output_path                  # 출력 파일 경로
    ]
    subprocess.run(command, check=True)
    
    # 임시 파일 삭제
    os.remove(merged_audio_path)


# In[4]:


# 사용 예시
video_path = f'{cover_pk}.mp4'                       # 동영상 파일 경로
audio_files=[]
for i in part_names.keys():
    audio_files.append(f"{cover_pk}_{i}.wav")
output_path = f'res_{cover_pk}.mp4'
mix_and_merge_audio_with_video(video_path, audio_files, output_path)
print("ok")


# In[ ]:




