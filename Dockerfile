FROM amazoncorretto:11-alpine-jdk
MAINTAINER org.rssdemo
COPY target/rssfeed-0.0.1-SNAPSHOT.jar rssfeed-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/rssfeed-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080