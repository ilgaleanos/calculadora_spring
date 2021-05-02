# compilacion del proyecto
FROM gradle:6.8-jdk11 AS builder

WORKDIR /home/gradle/compile/
COPY . .
ENV TIMEOUT_MEMORY=3600
RUN gradle clean test && gradle bootJar


# compilación del contenedor limpio
FROM alpine:latest

RUN apk update && apk add --no-cache openjdk11-jre 

RUN addgroup -S calculadora && adduser -S calculadora -G calculadora
RUN chown -R calculadora:calculadora /home/calculadora

WORKDIR /home/calculadora/api/

ENV TIMEOUT_MEMORY=3600 \
PORT=8080

COPY --from=builder /home/gradle/compile/build/libs/calculadora-1.0.0.jar .

EXPOSE 8080

CMD ["java", "-jar", "calculadora-1.0.0.jar"]
