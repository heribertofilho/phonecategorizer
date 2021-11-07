FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY sample.db sample.db
ENTRYPOINT ["java","-jar","/app.jar"]