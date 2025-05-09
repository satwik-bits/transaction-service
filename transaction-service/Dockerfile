# Use a lightweight OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/transaction-management-service-1.0.0.jar app.jar

# Expose the port the app runs on
EXPOSE 8082

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]
