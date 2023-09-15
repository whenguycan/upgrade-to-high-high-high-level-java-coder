## docker搭建sentinel的dashboard

#### 1、sentinel的dashboard的作用

- 能够直观的看到系统中所有的可被流控的资源
- 在dashboard针对某一个资源做流控管理，可以把规则直接push到对应的资源的服务中，使其生效。



#### 2、搭建过程

- 到如下地址直接下载sentinel的jar包，注意版本！

  https://github.com/alibaba/Sentinel/releases

  

- 准备如下的Dockerfile

  ```dockerfile
  FROM unitfinance/jdk17-sbt-scala:latest
  
  RUN mkdir -p /gitlabci
  
  WORKDIR /gitlabci
  
  EXPOSE 8080 #暴露8080端口
  
  ADD ./*.jar ./app.jar
  
  ENTRYPOINT ["java", "-jar", "-Dserver.port=8080", "app.jar"] #指定启动端口为8080
  ```

- 使用上述Dockerfile构建镜像

  ```shell
  docker build -f Dockerfile -t sentinel:1.8.6 .
  ```

  

- 使用镜像运行sentinel容器

  ```shell
  docker run -i -d --name=sentinel -p 8080:8080 镜像ID
  ```

- 运行容器后，浏览器打开 http://IP:8080 端口，用户名和密码都是sentinel

