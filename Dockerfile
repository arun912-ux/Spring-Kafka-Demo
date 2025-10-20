# Build stage: Use JDK for building the application
FROM eclipse-temurin:17-jdk-alpine AS build

# Set non-root user for security
USER 1000

# Set working directory inside the container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Build the application (skipping tests for faster build)
RUN ./gradlew clean build -x test

# Runtime stage: Use JRE for running the application (smaller image size)
FROM eclipse-temurin:17-jre-alpine

# Set non-root user for security
USER 1000

# Set working directory inside the container
WORKDIR /app

# Copy only the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
