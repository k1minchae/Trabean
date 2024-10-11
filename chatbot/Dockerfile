# Python 3.12 이미지를 사용
FROM python:3.12-slim

# 작업 디렉토리 설정 (루트 디렉토리 내 app 폴더로 설정)
WORKDIR /app

# 필요한 패키지를 설치하기 위해 requirements.txt 파일 복사
COPY requirements.txt ./

# 의존성 설치
RUN pip install --no-cache-dir -r requirements.txt

# FastAPI 애플리케이션 소스 코드 복사
COPY . .

# FastAPI 앱을 실행할 포트
EXPOSE 8082 

# FastAPI 앱 실행 (chatbot.main:app을 실행)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8082"]