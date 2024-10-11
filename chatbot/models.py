# models.py : 테이블 모델 정의
from sqlalchemy import Column, Integer, String
from database import Base

class Chatbot(Base):
    __tablename__ = "chatbot"

    location = Column(String, primary_key=True)
    budget = Column(Integer)