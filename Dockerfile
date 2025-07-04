# Etapa 1: Compilación con Maven y JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen liviana para ejecutar la app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/ms-books-payments-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5226
ENTRYPOINT ["java", "-jar", "app.jar"]