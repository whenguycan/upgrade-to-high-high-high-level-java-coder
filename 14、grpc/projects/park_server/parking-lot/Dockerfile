FROM unitfinance/jdk17-sbt-scala:latest

RUN mkdir -p /gitlabci

WORKDIR /gitlabci

EXPOSE 8080

EXPOSE 9115

ADD ./*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "app.jar"]