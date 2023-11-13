FROM openjdk:19
MAINTAINER Alex Fadeev
ENV LOAD_FROM_FILE=true
WORKDIR /app
COPY target/students-1.0-SNAPSHOT.jar /app/students.jar

CMD ["java", "-jar", "/app/students.jar"]
