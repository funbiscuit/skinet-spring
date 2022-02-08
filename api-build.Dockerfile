FROM openjdk:17-jdk-alpine as jar-builder

RUN mkdir -p /app

WORKDIR /app

COPY gradle gradle
COPY gradlew .

# cache gradle dist
RUN ./gradlew -v

COPY build.gradle settings.gradle ./
COPY skinet-api/build.gradle skinet-api/settings.gradle skinet-api/
COPY skinet-api/src skinet-api/src
RUN ./gradlew :skinet-api:bootJar

COPY skinet-api/build/libs/skinet-api-*.jar /app.jar


FROM openjdk:17-jdk-alpine
COPY --from=jar-builder /app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
