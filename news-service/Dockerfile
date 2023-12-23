FROM openjdk:19
MAINTAINER Alex Fadeev

WORKDIR /app
COPY target/news-service-1.0-SNAPSHOT.jar /app/news-service.jar

CMD ["java", "-jar", "/app/news-service.jar"]