# 빌드 단계
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일 복사
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

# 소스 코드 복사
COPY src ./src

# Gradle Wrapper 실행 권한 부여 및 빌드
RUN chmod +x ./gradlew && ./gradlew bootJar

# 실행 단계
FROM amazoncorretto:17

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물 복사
COPY --from=builder /app/build/libs/FinPick-0.0.1-SNAPSHOT.jar app.jar


# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
