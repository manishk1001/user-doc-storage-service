FROM gradle:8.5.0-jdk21 AS builder
WORKDIR /home/app
COPY --no-cache . .
RUN gradle build -x test --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /home/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]