FROM openjdk:11-jdk-slim

COPY ./target/core-*.jar /app/service.jar

CMD ["java", "-jar", "/app/service.jar"]
