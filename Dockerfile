# jdk11 Image Start
FROM openjdk:11

# 인자 설정 - JAR_FILE
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복사.
COPY ${JAR_FILE} app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]