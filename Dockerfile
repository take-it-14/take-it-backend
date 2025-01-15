FROM --platform=linux/amd64 eclipse-temurin:17-jre

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과 JAR 복사
ARG SERVICE_NAME
ARG SERVICE_PORT
COPY ${SERVICE_NAME}/build/libs/*.jar app.jar

# 서비스별 포트 설정 (docker-compose에서 관리)
EXPOSE ${SERVICE_PORT}

# 실행 시 필요 없는 파일 제거
RUN rm -rf /var/cache/apk/* /root/.gradle

# 실행 & 설정 파일 로드
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar", "--spring.config.location=file:/config/"]
