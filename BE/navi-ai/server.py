from fastapi import FastAPI, Form
from server_package.train_process import train_model_task
from server_package.infer_process import infer_task
import server_package.combine as combine

app = FastAPI()

# AI 모델 학습 요청 엔드포인트
@app.post("/ai/train")
async def train(pk : str = Form(...)):
    # pk: user_pk
    print("연결 완료")
    train_model_task.delay(pk)


# 커버곡 생성(추론 요청) 엔드포인트
@app.post("/ai/cover")
async def infer(pk : str = Form(...)):
    # pk: cover_pk
    infer_task.delay(pk)


# 노래방 video 생성 엔드포인트
@app.post("/noraebangs/record")
async def record(pk : str = Form(...)):
    # pk: noraebang_pk
    combine.audioCombine(pk)