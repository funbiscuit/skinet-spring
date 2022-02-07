FROM openjdk:17-jdk-alpine
RUN ls -R
COPY skinet-api/build/libs/skinet-api.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
