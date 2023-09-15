## openfeign调用固定的地址

之前我们写的openfeign的请求接口，在@FeignClient("Goods") 中的Goods是注册到nacos中的服务名

```java
@FeignClient("Goods") //这儿的Goods是注册到nacos中的服务名
public interface GoodsFeign {

    @GetMapping("/index")
    public String getGoodsIndex();
}
```

拿如果，我们的微服务没有注册到nacos，而是使用k8s的service做负载均衡呢？应该怎么写？

```java
@FeignClient(name = "xxxxx", url = "localhost:8099")// 这儿可以直接写死请求地址
public interface GoodsFeign {

    @GetMapping("/index")
    public String getGoodsIndex();
}
```

