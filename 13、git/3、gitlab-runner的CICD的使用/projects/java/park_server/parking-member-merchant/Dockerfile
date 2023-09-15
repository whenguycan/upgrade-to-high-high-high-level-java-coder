FROM unitfinance/jdk17-sbt-scala:latest

RUN mkdir -p /gitlabci

WORKDIR /gitlabci

EXPOSE 8110

EXPOSE 9110

ADD ./*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "app.jar"]