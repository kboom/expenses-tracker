FROM openjdk:8-jre

RUN mkdir /app

COPY target/docker/application.jar /app
COPY entrypoint.sh /app

EXPOSE 8080

WORKDIR /app
RUN chmod +x entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]