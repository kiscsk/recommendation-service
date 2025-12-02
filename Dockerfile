# Use a lightweight Java 25 runtime as the base image
FROM eclipse-temurin:25-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/recommendation-service.jar .

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "recommendation-service.jar"]