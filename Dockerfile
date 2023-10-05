FROM gradle:7-jdk11-alpine AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test --no-daemon

FROM amazoncorretto:11-alpine-jdk
COPY --from=build /app/build/libs/booksandmovies-0.0.1-SNAPSHOT.jar /app/booksandmovies.jar
ENTRYPOINT ["java", "-jar", "/app/booksandmovies.jar"]
EXPOSE 8080


