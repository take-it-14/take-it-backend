# 1️⃣ 실행 스테이지 - 경량 JRE 사용
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과 JAR 복사
ARG SERVICE_NAME
ARG SERVICE_PORT
COPY ${SERVICE_NAME}/build/libs/*.jar app.jar

# 서비스별 포트 설정 (docker-compose에서 관리)
EXPOSE ${SERVICE_PORT}

# 실행 시 필요 없는 파일 제거 (권한 문제 방지)
RUN rm -rf /var/cache/apk/* /root/.gradle

# JVM 최적화 옵션 추가 (메모리 효율성 향상)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1HeapRegionSize=16M", "-jar", "app.jar"]