# Build stage
FROM sbtscala/scala-sbt:eclipse-temurin-21.0.1_12_1.9.8_2.13.12 as builder

WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN sbt clean compile && \
    sbt assembly

# Runtime stage
FROM eclipse-temurin:21.0.1_12-jre-alpine

WORKDIR /app

# Copy the built JAR from builder
COPY --from=builder /app/target/scala-2.13/dashboard-backend-assembly-*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Run the application
CMD ["java", "-jar", "app.jar"]
