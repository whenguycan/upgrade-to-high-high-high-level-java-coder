## 使用Actuator暴露监控指标到prometheus

> 在Springboot项目中使用



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

```properties
management.endpoint.metrics.enabled=true
management.endpoints.web.exposure.include=*
management.endpoint.prometheus.enabled=true
management.metrics.export.prometheus.enabled=true
```



#### 3、打开页面确认是否成功

http://IP:PORT/actuator/prometheus



#### 4、具体指标含义

参考：https://www.cnblogs.com/MJyc/p/11090394.html