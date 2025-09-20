FROM openjdk:17-jdk-alpine

LABEL maintainer="Event Notification System"
LABEL version="1.0.0"
LABEL description="Event Notification System - Java Backend"

# Create app directory
WORKDIR /app

# Copy maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Create logs directory
RUN mkdir -p logs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "target/eventnotification-0.0.1-SNAPSHOT.jar"]