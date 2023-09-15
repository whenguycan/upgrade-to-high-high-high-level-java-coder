## springboot项目使用docker部署



#### 1、springboot打成jar包



#### 2、编写Dockerfille

```dockerfile
FROM openjdk:17 # java8使用 anapsix/alpine-java:8_server-jre_unlimited 这个镜像

RUN mkdir -p /gitlabci #创建一个目录

WORKDIR /gitlabci #切换工作目录

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT} #要暴露的端口

ADD ./*.jar ./app.jar

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime #设置时区
RUN echo 'Asia/Shanghai' >/etc/timezone

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=test", "app.jar"]
```



#### 3、使用上述Dockerfile打成镜像



#### 4、使用docker run命令启动项目

