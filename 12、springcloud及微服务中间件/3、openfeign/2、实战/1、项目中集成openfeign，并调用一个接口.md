## 集成openfeign并调用一个接口



#### 1、在需要使用openfeign的微服务中引入依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

为什么不需要指定openfeign的版本？



#### 2、入口文件添加注解

> 注意只要在需要使用openfeign的微服务中使用即可

@EnableFeignClients



#### 3、编写接口准备调用

在Goods项目中写一个接口，随便返回一个字符串

```java
@RestController
public class IndexController {

    @Autowired
    private Environment env;

    @GetMapping("/index")
    public String idnex(){
        return "goods-project-index-function";
    }
}
```



#### 4、在User中编写feign的调用接口

```java
@FeignClient("Goods") //这儿的Goods是注册到nacos中的服务名
public interface GoodsFeign {

    @GetMapping("/index")
    public String getGoodsIndex();
}
```



#### 5、在User中调用接口

```java
@RestController
public class IndexController {

    @Autowired
    GoodsFeign goodsFeign;

    @GetMapping("/index")
    public void idnex(){
        System.out.println(goodsFeign.getGoodsIndex());
    }
}
```



#### 6、启动项目，发现User模块无法启动

> 因为新版本中，feign的负载均衡已经剔除了ribbon组件，需要手动配置spring cloud loadbalanace组件

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-loadbalancer</artifactId>
</dependency>
```

加入这个依赖之后，就可以顺利启动项目



#### 7、调用接口测试

 注意：要想调用通，注册的两个服务必须在同一个namespace、同一个分组中

