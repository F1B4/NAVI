"""
AI모델 학습 요청 celery task
"""
from server_package.celery_app import celery_app
import server_package.audio_processor as ap
import subprocess
import torch
import server_package.file_module as fm
import requests
import logging
from server_package.db_connection import get_connection

def execute_command(command):
    try:
        result = subprocess.run(command, capture_output=True, text=True)
        return {"output": result.stdout, "error": result.stderr}
    except Exception as e:
        return {"error": str(e)}

# 경로 변수 설정
BASE_PATH = "/home/navi/rvcModel"
DATASETS_PATH = f"{BASE_PATH}/datasets"
ORIGINAL_PATH = f"{DATASETS_PATH}/original"
PROCESSED_PATH = f"{DATASETS_PATH}/processed"
LOGS_PATH = f"{BASE_PATH}/logs"
USERS_PATH = f"{BASE_PATH}/users"

# 로깅 설정
logging.basicConfig(level=logging.INFO)

@celery_app.task(queue="train_queue")
def train_model_task(user_pk):
    try: 
        connection = get_connection()
        # 해당 유저(user_pk)의 voice 테이블 리스트 가져오기
        with connection.cursor() as cursor:
            sql_voice = f"SELECT path FROM voice WHERE user_pk = {user_pk}"
            cursor.execute(sql_voice)
            voice_list = cursor.fetchall()

        # voice s3경로 접근해서 유저 목소리 파일 전부 다운받기
        voice_list = [voice['path'] for voice in voice_list]
        fm.download_files(voice_list, ORIGINAL_PATH)
        
        # 데이터 가공(m4a -> wav -> 10초)
        processor = ap.AudioProcessor(ORIGINAL_PATH, PROCESSED_PATH)
        processor.make_dataset()

        # 데이터 전처리
        command = [
            "python", "main.py", "preprocess",
            "--model_name", str(user_pk),
            "--dataset_path", f"{PROCESSED_PATH}/work/final",
            "--sampling_rate", "40000"
        ]
        execute_command(command)
        
        # 특징 추출
        command = [
            "python", "main.py", "extract",
            "--model_name", str(user_pk),
            "--rvc_version", "v2",
            "--sampling_rate", "40000"
        ]
        execute_command(command)

        torch.cuda.empty_cache()
        # 학습
        command = [
            "python", "main.py", "train",
            "--model_name", str(user_pk),
            "--save_every_epoch", "20",
            "--total_epoch", "60",
            "--sampling_rate", "40000",
            "--batch_size", "4"
        ]
        execute_command(command)
        
        # 모델 파일 찾고 변경
        model_name = f"{user_pk}_60e_"
        new_model_path = f"{USERS_PATH}/{user_pk}.pth"
        fm.find_and_move_file(LOGS_PATH, model_name, new_model_path)

        # 인덱스 파일 찾고 변경
        index_path = f"{LOGS_PATH}/{user_pk}"
        index_name = "added_"
        new_index_path = f"{USERS_PATH}/{user_pk}.index"
        fm.find_and_move_file(index_path, index_name, new_index_path)
        
        # s3에 업로드
        s3_path = fm.upload_file_to_s3([new_model_path, new_index_path], 'application/octet-stream')
        # user 테이블에 모델과 인덱스 파일 경로 insert
        with connection.cursor() as cursor:
            sql_voice = "UPDATE `user` SET model=%s, `index`=%s WHERE user_pk=%s"
            cursor.execute(sql_voice, (s3_path[0], s3_path[1], user_pk))
            connection.commit()

        # 학습에 사용된 데이터셋, 모델, 인덱스파일(원래 경로)은 로컬에서 모두 삭제
        # 데이터셋 : datasets/processed/raw, work 폴더 삭제
        fm.remove_dir(f"{PROCESSED_PATH}/raw")
        fm.remove_dir(f"{PROCESSED_PATH}/work")
        # original 폴더 내부 모두 삭제해야함. 근데 폴더는 살려놔야함
        fm.remove_files(ORIGINAL_PATH, "", "")
        # 인덱스 파일 : logs/user_pk 폴더 삭제
        logs_dir = f"{LOGS_PATH}/{user_pk}"
        fm.remove_dir(logs_dir)
        # 모델 : logs/밑에 pth 파일 삭제 (파일 여러개 삭제)
        partial_name = f"{user_pk}_"
        fm.remove_files(LOGS_PATH, partial_name, 'pth')

        # 모델 학습 완료 후 Spring 으로 완료 메시지 전송
        complete_response = requests.post(f'http://backend_server:port/api/ai/train/{user_pk}')
        logging.info(f"Train complete response: {complete_response.text}")
    
    except Exception as e:
        # 모델 학습 실패
        failure_response = requests.post(f'http://backend_server:port/api/ai/train/{user_pk}')
        logging.info(f"Train Failure response: {failure_response.text}")
