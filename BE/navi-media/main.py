from fastapi import FastAPI

app = FastAPI()

@app.get("/media/ss")
def read_root():
    return {"Hello": "FastAPI"}