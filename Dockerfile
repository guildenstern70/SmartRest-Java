# ==============================================================================
# Stage 1: Build Stage
# ==============================================================================
FROM eclipse-temurin:26-jdk AS builder

WORKDIR /workspace

# Copy Gradle wrapper and configuration files
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/

# Ensure executable permissions on the Gradle wrapper
RUN chmod +x gradlew

# Copy application source code
COPY src/ src/

# Build the Spring Boot executable jar (skipping unit tests during packaging)
RUN ./gradlew bootJar --no-daemon -x test && \
    find build/libs -name "*.jar" ! -name "*-plain.jar" -exec cp {} /workspace/app.jar \;

# ==============================================================================
# Stage 2: Runtime Stage
# ==============================================================================
FROM eclipse-temurin:26-jre

# Create dedicated non-root user and group for security
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Copy the executable jar built in stage 1
COPY --from=builder --chown=spring:spring /workspace/app.jar /app/app.jar

# Run container as non-root user
USER spring:spring

# Expose Spring Boot default port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
