FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-slim-buster
WORKDIR /app

COPY --from=build /app/target/tenpo-1.0.0.jar ./tenpo.jar
CMD ["bash", "-c", "java -jar tenpo.jar"]
