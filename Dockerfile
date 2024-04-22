FROM openjdk:11

ARG JAR_FILE=build/libs/StoreSpotter-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

# 실행 명령
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]