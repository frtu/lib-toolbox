# https://spring.io/guides/gs/spring-boot-docker/
ARG VERSION=8-jdk-alpine
FROM openjdk:$VERSION

WORKDIR .
VOLUME /tmp

ARG JAR_FILE=sample-reactive-web-1.1.4-SNAPSHOT.jar
COPY maven/target/${JAR_FILE} /deployments/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/deployments/app.jar"]
