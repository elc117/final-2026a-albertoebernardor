FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY divideai/pom.xml ./pom.xml
COPY divideai/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/divideai-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
