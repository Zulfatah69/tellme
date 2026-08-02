# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

LABEL maintainer="TellMe Team" \
      description="TellMe Spring Boot Application Builder" \
      version="1.0.0"

WORKDIR /build
# Copy pom.xml and download dependencies for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="TellMe Team" \
      description="TellMe Spring Boot Application Runtime" \
      version="1.0.0"

WORKDIR /app

# Create non-root user
RUN addgroup -S tellme && adduser -S tellme -G tellme

# Create uploads directory and set permissions
RUN mkdir -p /app/uploads && chown tellme:tellme /app/uploads

# Copy built artifact from builder stage
COPY --from=builder /build/target/tellme-1.0.0.jar app.jar

# Switch to non-root user
USER tellme

# Expose configurable port
EXPOSE 8081

# Set JVM flags for containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
