FROM openjdk:11-jdk-slim

COPY ./target/core-*.jar /app/service.jar

ENTRYPOINT["java", "-jar", "/app/service.jar"]
