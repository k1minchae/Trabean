# database.py : DB 설정 파일

from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

# MySQL 연결 URL
SQLALCHEMY_DATABASE_URL = "mysql://root:ssafy@localhost:3306/trabean"

# MySQL 엔진 생성
engine = create_engine(
    SQLALCHEMY_DATABASE_URL
)

# 세션 생성
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# 기본 Base 클래스 생성
Base = declarative_base()