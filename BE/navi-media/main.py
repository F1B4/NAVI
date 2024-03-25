from fastapi import FastAPI

app = FastAPI()

@app.get("/ss")
def read_root():
    return {"Hello": "FastAPI"}