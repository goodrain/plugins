FROM swr.cn-north-4.myhuaweicloud.com/wutong/jdk17-gradle73:latest

RUN mkdir /talkweb-apps

COPY build/libs/app.jar /talkweb-apps/app.jar

WORKDIR /talkweb-apps

ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "-Duser.timezone=GMT+8", "/talkweb-apps/app.jar"]
