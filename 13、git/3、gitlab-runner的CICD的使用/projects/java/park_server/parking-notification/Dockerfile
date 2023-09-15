FROM unitfinance/jdk17-sbt-scala:latest

RUN mkdir -p /gitlabci

WORKDIR /gitlabci

EXPOSE 9105

EXPOSE 9100

ADD ./*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "app.jar"]
