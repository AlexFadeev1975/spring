FROM openjdk:19
MAINTAINER Alex Fadeev

WORKDIR /app
COPY target/book-service-1.0-SNAPSHOT.jar /app/book-service.jar

CMD ["java", "-jar", "/app/book-service.jar"]