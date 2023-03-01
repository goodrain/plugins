FROM devhub.wutong.talkweb.com.cn/base/war:0.1.0

ENV  war_name=app

RUN  rm -rf /usr/local/apache-tomcat-9.0.62/webapps/ROOT

COPY ${war_name}.war /usr/local/apache-tomcat-9.0.62/webapps/ROOT.war
