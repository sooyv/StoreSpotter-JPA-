# jdk11 Image Start
FROM openjdk:11

# 인자 설정 - JAR_FILE
ARG JAR_FILE=build/libs/StoreSpotter-0.0.1-SNAPSHOT.jar

# jar 파일 복사.
COPY ${JAR_FILE} app.jar

# 실행 명령
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "app.jar"]