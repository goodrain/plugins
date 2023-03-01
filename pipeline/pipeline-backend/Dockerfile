FROM adoptopenjdk/openjdk8

RUN mkdir /talkweb-apps

COPY target/app.jar /talkweb-apps

WORKDIR /talkweb-apps

ENTRYPOINT ["java", "-Dfile.encoding=utf-8", "-jar",  "/talkweb-apps/app.jar"]
