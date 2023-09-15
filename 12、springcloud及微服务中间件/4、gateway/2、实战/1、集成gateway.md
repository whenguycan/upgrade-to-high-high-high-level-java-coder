## 集成gateway



#### 1、打开project中的项目



#### 2、创建一个新的微服务名称为gateway

注意：新的项目就是一个springboot项目，且不能引入spring-boot-starter-web依赖（正常创建项目后，删除这个依赖就行）



#### 3、引入gateway的依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-gateway -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
  <version>3.1.4</version>
</dependency>
```

注意版本号按项目需要调整



#### 4、在gateway微服务中新增配置

```yaml
spring:
  application:
    name: gateway
  cloud:
    gateway:
      routes:
        - id: User
          uri: lb://User  #负载均衡的方式调用服务
          predicates:
            - Path=/user/** #指定使用什么断言类去断言。这儿写Path，就是用PathRoutePredicateFactory类去断言，Path来自PathRoutePredicateFactory的类名
          filters:
            - RewritePath=/user(?<segment>/?.*), $\{segment}
server:
  port: 80
```



#### 5、引入nacos的注册中心依赖

```xml
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

因为只有gateway也注册到nacos中，`lb://User`这个User才知道去哪里找



#### 6、新增nacos的注册配置

```yaml
spring:
  application:
    name: gateway
  cloud:
    nacos:
      discovery:
        password: nacos
        username: nacos
        server-addr: 172.16.4.144:8808
    gateway:
      routes:
        - id: User
          uri: lb://User
          predicates:
            - Path=/user/**
          filters:
            - RewritePath=/user(?<segment>/?.*), $\{segment}
server:
  port: 80

debug: true
```



#### 7、因为gateway里面也对目标服务进行负载均衡，所以也需要引入loadbalance

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-loadbalancer</artifactId>
</dependency>
```

如果不引入这个依赖，项目无法启动



#### 8、测试调用

通过http://gatewayIP:gatewayPort/user/xxxxxx进行测试。当gateway拿到/user/xxxxx会先经过predicates断言，拿到 lb://User的地址（去nacos中查找User服务中所有的地址，然后负载均衡出一个），再经过filters改写请求将/user去掉变成/xxxx去请求User中的地址。