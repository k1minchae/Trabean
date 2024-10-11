# Eclipse Temurin 17 JDK 이미지를 기반으로 합니다.
FROM eclipse-temurin:17-jdk

# 빌드 시 사용할 ARG 변수를 정의합니다.
ARG DB_HOST
ARG DB_PORT
ARG DB_USER
ARG DB_PASSWORD
ARG DB_NAME
ARG API_KEY

# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 빌드된 JAR 파일을 /app 디렉토리에 복사합니다.
COPY build/libs/account-0.0.1-SNAPSHOT.jar app.jar

# ARG 변수를 ENV로 설정하여 컨테이너 실행 시 사용할 수 있도록 합니다.
ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV DB_NAME=${DB_NAME}
ENV API_KEY=${API_KEY}

# 컨테이너가 사용하는 포트 8083을 노출합니다.
EXPOSE 8081

# 컨테이너가 시작될 때 실행할 명령을 설정합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]

# 컨테이너에 /data 디렉토리를 볼륨으로 설정합니다.
VOLUME ["/data"]
