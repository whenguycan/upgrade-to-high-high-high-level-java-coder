## Springboot的SPI机制



#### 什么是SPI机制

在java编程中，都是以面向接口编程的。如果有一个接口有非常多的实现类，分布在不同的jar包中，我们没法直观的查看都有哪些实现类。而且，没必要把所有的实现类强耦合到代码中去，那么SPI就出现了。**spi就是提供这样的一个机制：快速为某个接口寻找服务实现的机制**





#### SPI用法约定

在jar包的META-INF/services/目录里创建一个以<font color="red">接口全路径名称命名的文件</font>。<font color="red">该文件里写的就是实现该服务接口的具体实现类的全路径</font>。而当外部程序装配这个模块的时候，只需要ServiceLoader.load(接口名称.class)，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载，完成模块的注入。通过这个约定，就不需要把服务放在代码中了，通过模块被装配的时候就可以发现服务类了。

> <font color="red">springboot在加载的时候，会扫描所有jar包下的META-INF/services下的配置文件</font>

比如：

```java
io.seata.discovery.registry.custom.CustomRegistryProvider
io.seata.discovery.registry.consul.ConsulRegistryProvider
io.seata.discovery.registry.eureka.EurekaRegistryProvider
io.seata.discovery.registry.nacos.NacosRegistryProvider
io.seata.discovery.registry.redis.RedisRegistryProvider
io.seata.discovery.registry.sofa.SofaRegistryProvider
io.seata.discovery.registry.zk.ZookeeperRegistryProvider
io.seata.discovery.registry.etcd3.EtcdRegistryProvider
....
```



#### 具体使用

- 按照SPI约定，写好了接口和类
- 编写如下代码，灵活选用接口实现类

```java
ServiceLoader<接口名称> load = ServiceLoader.load(接口名称.class)
load.forEach((item)->{
  
  //这儿根据需要去拿自己需要的接口实现类去操作。
  System.out.println(item.getClass().getName());
});
```

