FROM unitfinance/jdk17-sbt-scala:latest

RUN mkdir -p /gitlabci

WORKDIR /gitlabci

EXPOSE 8088
EXPOSE 9088

ADD ./*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "app.jar"]
