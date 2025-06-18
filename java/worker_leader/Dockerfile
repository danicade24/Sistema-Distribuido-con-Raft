FROM maven:3.8.5-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11
WORKDIR /app
COPY --from=build /app/target/worker_leader-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
