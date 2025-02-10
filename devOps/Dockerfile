FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/*.jar
RUN echo $JAR_FILE
COPY $JAR_FILE /app.jar
LABEL authors="iori"

ENTRYPOINT ["java", "-jar", "/app.jar"]