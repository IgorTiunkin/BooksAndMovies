FROM gradle:jdk11-alpine AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build --no-daemon

FROM amazoncorretto:11-alpine-jdk



