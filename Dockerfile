
FROM openjdk:19

COPY target/ServiceAPI-1.0-SNAPSHOT-jar-with-dependencies.jar /ServiceAPI.jar

# set the startup command to execute the jar
CMD ["java", "-jar", "/ServiceAPI.jar"]

