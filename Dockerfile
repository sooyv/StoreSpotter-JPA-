## jdk11 Image Start
#FROM openjdk:11
#
## 인자 설정 - JAR_FILE
#ARG JAR_FILE=build/libs/*.jar
#
## jar 파일 복사.
#COPY ${JAR_FILE} app.jar
#
#ENV SPRING_PROFILE = "prod"
#
## application-prod.yml 설정 파일을 이미지 내 /app/config 디렉토리에 복사합니다.
#COPY ./src/main/resources/application-prod.yml /app/config/application-prod.yml
#
## 컨테이너 시작 시 실행될 명령어를 정의합니다.
## 여기서는 application-prod.yml 파일의 내용을 출력하고, 애플리케이션을 실행합니다.
#CMD cat /app/config/application-prod.yml
## 실행 명령
#ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "app.jar"]

# jdk11 Image Start
FROM openjdk:11

# 작업 디렉토리 설정
WORKDIR /app

# 인자 설정 - JAR_FILE
ARG JAR_FILE=build/libs/*.jar

# jar 파일 및 application-prod.yml 설정 파일을 이미지 내에 복사합니다.
COPY ${JAR_FILE} app.jar
COPY ./src/main/resources/application-prod.yml ./config/application-prod.yml

# 환경변수 설정
ENV SPRING_PROFILE=prod

# 스크립트 파일 생성 및 실행 권한 부여
RUN echo "#!/bin/sh\n\
cat ./config/application-prod.yml\n\
exec java -Dspring.profiles.active=\${SPRING_PROFILE} -jar app.jar" > ./entrypoint.sh
RUN chmod +x ./entrypoint.sh

# 실행 명령
ENTRYPOINT ["./entrypoint.sh"]
