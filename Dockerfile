# 임시 이미지 생성
FROM gradle:8.7.0-jdk17-alpine AS build

# 캐시 최적화를 위해 설정
COPY --chown=gradle:gradle backend /home/app
WORKDIR /home/app

# 프로젝트 빌드 수행
RUN gradle clean build -x test

# 실행용 이미지
FROM openjdk:17-jdk-slim
VOLUME /tmp

# JAR 파일 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

# 외부에서 환경변수 주입 가능
ENV JAVA_OPTS=""

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
