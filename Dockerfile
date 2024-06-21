FROM maven:3.8.7-openjdk-18-slim AS build
COPY . /.
RUN mvn -f /pom.xml clean package

FROM openjdk:23-jdk-slim
RUN apt-get update && apt-get install -y git
COPY ./sonar-scanner-4.7.0.2747-linux /sonar-scanner-4.7.0.2747-linux
RUN chmod a+x /sonar-scanner-4.7.0.2747-linux/bin/sonar-scanner
RUN chmod 755 /sonar-scanner-4.7.0.2747-linux/jre/bin/java
COPY --from=build /target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]