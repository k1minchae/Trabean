# Eclipse Temurin 17 JDK 이미지를 기반으로 합니다.
FROM eclipse-temurin:17-jdk


# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 빌드된 JAR 파일을 /app 디렉토리에 복사합니다.
COPY build/libs/gateway-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너가 사용하는 포트 80 노출합니다.
EXPOSE 80

# 컨테이너가 시작될 때 실행할 명령을 설정합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]

# 컨테이너에 /data 디렉토리를 볼륨으로 설정합니다.
VOLUME ["/data"]
