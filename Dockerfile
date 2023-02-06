FROM maven:3-openjdk-18 AS MAVEN_BUILD
COPY ./ ./
RUN mvn clean install

FROM openjdk:18
COPY --from=MAVEN_BUILD target/Endavibe-0.0.1-SNAPSHOT.jar /endavibe.jar
CMD ["java", "-jar", "/endavibe.jar"]
