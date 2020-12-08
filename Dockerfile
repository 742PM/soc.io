FROM maven:3.6.3-jdk-11 as backend
WORKDIR /
RUN mvn clean verify -DskipTests=true

FROM openjdk:14-jdk-alpine
COPY --from=backend /target/soc.io-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
CMD [ "sh", "-c", "java -jar app.jar" ]
