# Stage 1: build (Dùng Maven image có sẵn)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
# Copy file cấu hình mirror và pom.xml
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src
# Chạy build với file settings.xml để dùng Google mirror
RUN mvn -s .mvn/settings.xml clean package -DskipTests

# Stage 2: run (Chỉ chạy JAR, không có Maven)
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]