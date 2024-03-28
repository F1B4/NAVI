from fastapi import FastAPI
from server_package.train_process import train_model_task
from server_package.infer_process import infer_task

app = FastAPI()

# AI 모델 학습 요청 엔드포인트
@app.post("/ai/train")
async def train(user_pk : int):
    train_model_task.delay(user_pk)


# 커버곡 생성(추론 요청) 엔드포인트
@app.post("/ai/cover")
async def infer(cover_pk : int):
    infer_task.delay(cover_pk)
