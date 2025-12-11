# 1단계: Build Stage
FROM gradle:8.7.0-jdk17 AS build

WORKDIR /home/app

# Gradle 캐시 최적화 (중요!)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 캐시 생성용 dummy build
RUN gradle clean build -x test || true

# 실제 소스 복사
COPY . .

# 실제 빌드
RUN gradle clean build -x test


# 2단계: Run Stage
FROM eclipse-temurin:17-jdk-jammy

# 한글 로케일 설정
RUN apt-get update && \
    apt-get install -y locales && \
    sed -i '/ko_KR.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen

ENV LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR:ko \
    LC_ALL=ko_KR.UTF-8

# jar 파일 명시적으로 지정 (가장 안전)
COPY --from=build /home/app/build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]
