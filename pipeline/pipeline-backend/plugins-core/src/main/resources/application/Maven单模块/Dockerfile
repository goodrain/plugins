FROM openjdk:8-jdk

RUN mkdir /apps

COPY app.jar /apps/app.jar

WORKDIR /apps

ENTRYPOINT ["java", "-jar", "/apps/app.jar"]
