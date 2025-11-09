# ----------------------------
# 1. Build Stage (uses Maven)
# ----------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven files first (for better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the actual source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ----------------------------
# 2. Runtime Stage (uses JDK)
# ----------------------------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy only the built JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

# Tell Spring Boot to use port 8080 (Render expects this)
ENV PORT=8080
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
