FROM adoptopenjdk/openjdk8

RUN mkdir /apps

COPY app.jar /apps/app.jar

WORKDIR /apps

ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar",  "/apps/app.jar"]
