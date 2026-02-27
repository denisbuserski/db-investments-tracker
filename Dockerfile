FROM ubuntu:latest
LABEL authors="db-user"

ENTRYPOINT ["top", "-b"]



# Stage 1: Build with Maven + JDK 17
FROM maven:3.9.3-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run with JDK 17 runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/db-password-generator-1.0.0.jar db-password-generator-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "db-password-generator-1.0.0.jar"]