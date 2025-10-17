# Stage 1: Build JAR
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy everything from the current directory to /app in the container
COPY . .

# Make Gradle wrapper executable
RUN chmod +x gradlew

# Build the JAR
RUN ./gradlew clean build -x test

# Stage 2: Run JAR
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
