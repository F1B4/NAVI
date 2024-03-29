from fastapi import FastAPI, Form
from server_package.train_process import train_model_task
from server_package.infer_process import infer_task

app = FastAPI()

# AI 모델 학습 요청 엔드포인트
@app.post("/ai/train")
async def train(pk : str = Form(...)):
    print("연결 완료")
    train_model_task.delay(pk)


# 커버곡 생성(추론 요청) 엔드포인트
@app.post("/ai/cover")
async def infer(cover_pk : int):
    infer_task.delay(cover_pk)

# mr+유저 목소리 합쳐서 s3 올리고 노래방테이블의 record에 s3 주소 저장 