# the first stage of our build will use a maven 2323 parent image
FROM maven:3.9.0-eclipse-temurin-19-focal AS MAVEN_BUILD

# copy the pom and src code to the container
COPY ./ ./

# package our application code
RUN mvn clean package

# the second stage of our build will use open jdk 19
FROM openjdk:21-slim-buster

# copy only the artifacts we need from the first stage and discard the rest
COPY --from=MAVEN_BUILD /target/ServiceAPI-1.0-SNAPSHOT-jar-with-dependencies.jar /ServiceAPI.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/ServiceAPI.jar"]