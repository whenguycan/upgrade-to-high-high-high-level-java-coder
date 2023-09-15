## 使用Actuator暴露监控指标到prometheus



#### 1、引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- actuator的指标转成prometheus的包，并会暴露/actuator/prometheus的路由 -->
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
  <scope>runtime</scope>
</dependency>
```



#### 2、添加配置

```yaml
management:
  endpoint:
    metrics: 
      enabled: true #打开metrics端点
    prometheus:
      enabled: true #打开prometheus端点
    health:
      enabled: true #打开health端点
  endpoints:
    web:
      exposure:
        include: "*" #设置通过web方式暴露的端点， *号是指所有端点
  prometheus:
    metrics:
      export:
        enabled: true

```



#### 3、打开页面确认是否成功

http://IP:PORT/actuator  打开这个地址能看到当前应用暴露的所有的端点

http://IP:PORT/actuator/prometheus  打开这个地址是当前应用暴露的prometheus的端点的信息



#### 4、具体指标含义

参考：https://www.cnblogs.com/MJyc/p/11090394.html