# 임시 이미지 생성
FROM gradle:8.7.0-jdk17 AS build

# 캐시 최적화를 위해 설정
COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app

# 프로젝트 빌드 수행
RUN gradle clean build -x test

# 실행용 이미지
FROM eclipse-temurin:17-jdk-jammy

# UTF-8 locale 설치
RUN apt-get update && \
    apt-get install -y locales && \
    sed -i '/ko_KR.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen

# 환경 변수로 한글 로케일 설정
ENV LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR:ko \
    LC_ALL=ko_KR.UTF-8

VOLUME /tmp

# JAR 파일 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

# JVM 인코딩 설정 환경 변수 추가
ENV JAVA_OPTS="-Dfile.encoding=UTF-8"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
