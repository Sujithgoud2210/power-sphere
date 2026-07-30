# =============================================================================
# PowerSphere – Backend Dockerfile
# Multi-stage build: Maven compile → JRE runtime
# =============================================================================

# ---- Stage 1: Build with Maven ----
FROM eclipse-temurin:21-jdk-alpine AS builder

LABEL stage=builder
LABEL description="PowerSphere Backend Build Stage"

# Install Maven
RUN apk add --no-cache maven

WORKDIR /build

# Copy Maven wrapper and POM first for dependency caching
COPY pom.xml ./
RUN mvn dependency:go-offline -B -q -DskipTests || true

# Copy source code
COPY src/main/java ./src/main/java
COPY src/main/resources ./src/main/resources

# Build the application (skip tests for speed, tests run separately in CI)
RUN mvn clean package -DskipTests -Pprod -B -q

# ---- Stage 2: Runtime with JRE ----
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL description="PowerSphere Backend Runtime"
LABEL version="1.0.0"
LABEL maintainer="PowerSphere Team"

# Install curl for health check
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -S powersphere && \
    adduser -S -G powersphere powersphere

# Create app directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chown -R powersphere:powersphere /app

# Switch to non-root user
USER powersphere

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -sf http://localhost:8080/actuator/health || exit 1

# Environment variables with defaults
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseZGC -XX:ZCollectionInterval=30 -XX:ZFragmentationLimit=10"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
