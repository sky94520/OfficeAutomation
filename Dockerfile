FROM maven:latest

VOLUME ['/code']
WORKDIR /code
COPY pom.xml /code

EXPOSE 8081

CMD mvn spring-boot:run
