# 1. Python 3.12 이미지 기반으로 설정
FROM python:3.12

# 2. 작업 디렉토리를 /app으로 설정
WORKDIR /app

# 3. 환경 변수 파일을 컨테이너의 /app 디렉토리에 복사
COPY .env .env

# 4. 필요한 패키지 목록 requirements.txt를 복사
COPY requirements.txt .

# 5. 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# 6. 애플리케이션의 모든 파일을 /app으로 복사
COPY . .

# 7. 컨테이너가 사용하는 포트 8082를 노출
EXPOSE 8082

# 8. uvicorn을 사용하여 FastAPI 애플리케이션 실행
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8082"]
