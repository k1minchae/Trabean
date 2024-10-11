# main.py : 프로젝트의 전체적인 환경을 설정하는 파일

from fastapi import FastAPI, Depends
from starlette.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from database import SessionLocal
from models import Chatbot

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 의존성으로 DB 세션을 가져오는 함수
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# /hello 엔드포인트에서 chatbot 데이터 조회
@app.get("/hello")
def hello(db: Session = Depends(get_db)):
    # chatbot 테이블에서 모든 데이터 조회
    chatbot_data = db.query(Chatbot).all()
    
    # 데이터를 출력 (콘솔에 출력)
    for data in chatbot_data:
        print(f"Location: {data.location}, Budget: {data.budget}")
    
    return {"message": "데이터가 출력되었습니다."}
