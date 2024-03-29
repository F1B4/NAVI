# RVC_CLI 모델 폴더 내에 위치해야함
import aiomysql
from typing import Optional
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import sys
sys.path.append('/home/navi/apiTest')
import audio_processor as ap
import asyncio

app = FastAPI()

class Voice(BaseModel):
    voice_pk : int
    path : str
    user_pk : int
    song_pk : int

# 학습 요청 엔드포인트
@app.post("/ai/train")
async def createModel(q: Optional[Voice]):
    # 파라미터 : 해당 유저의 voice 테이블 내용 DTO List로 받아오기

    # voice s3경로 접근해서 파일 전부 다운받기

    # 데이터 가공(m4a -> wav -> 10초)
    # 목소리 녹음 파일 경로
    src_dir = "/home/navi/rvcModel/datasets/original"
    # 데이터 가공 후 저장 경로
    work_path = "/home/navi/rvcModel/datasets/processed"
    processor = ap.AudioProcessor(src_dir, work_path)
    processor.make_dataset()

    # 데이터 전처리 : 모델 이름은 유저 pk로, 경로도 마찬가지
    command = [
        "python", "main.py", "preprocess",
        "--model_name", model_name,
        "--dataset_path", dataset_path,
        "--sampling_rate", "40000"
    ]
    # 비동기 실행
    process = await asyncio.create_subprocess_exec(
        *command,
        stdout=asyncio.subprocess.PIPE,
        stderr=asyncio.subprocess.PIPE
    )

    # 모델 전처리 실행이 끝날 때까지 기다림
    stdout, stderr = await process.communicate()

    # 모델 전처리 실패한 경우
    if process.returncode != 0:
        return {"message":stderr.decode()}

    # 특징 추출

    # 학습

    # 인덱스 파일 생성

    # 모델, 인덱스 파일 압축해서 s3 올리기

    # 학습에 사용된 데이터셋, 모델, 인덱스파일은 로컬에서 모두 삭제

    return {"message":"completed AI model training"}


# 추론 엔드포인트 : 필요한 정보(user_pk)
# @app.post("/ai/infer")
# async def getCover(user_pk : int):


# async def get_db_connection():
#     connection = await aiomysql.connect(
#         host='127.0.0.1',
#         user='ssafy',
#         password='ssafy',
#         db='navi',
#         )
#     return connection