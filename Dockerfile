# 임시 이미지 생성
FROM gradle:8.7.0-jdk17 AS build

# 캐시 최적화를 위해 설정
COPY --chown=gradle:gradle . /home/app
WORKDIR /home/app

# 프로젝트 빌드 수행
RUN gradle clean build -x test

# 실행용 이미지
FROM openjdk:17-jdk-slim

# UTF-8 locale 설치
RUN apt-get update && \
    apt-get install -y locales && \
    locale-gen ko_KR.UTF-8 && \
    update-locale LANG=ko_KR.UTF-8

ENV LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR:ko \
    LC_ALL=ko_KR.UTF-8

VOLUME /tmp

# JAR 파일 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

# 외부에서 환경변수 주입 가능
ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
