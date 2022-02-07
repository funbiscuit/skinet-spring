FROM openjdk:17-jdk-alpine
COPY skinet-api/build/libs/skinet-api.jar app.jar
COPY docker/ssl.p12 ssl.p12
ENTRYPOINT ["java","-jar","/app.jar"]
