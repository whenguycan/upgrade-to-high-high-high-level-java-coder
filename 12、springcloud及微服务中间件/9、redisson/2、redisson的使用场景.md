## redisson的使用场景

1. 在微服务高可用部署环境下，高并发下，从redis的缓存中读取缓存数据发生缓存击穿！此时用redisson是确保在高可用的情况下，只有1个请求去mysql中读取数据，其它请求还是从redis中获取数据

```java
if(){//reids中有数据
  
  //直接取走
  
}else{//redis中没有数据
  
  //为了解决缓存击穿的问题，这儿加个redisson分布式锁，确保请求1个1个进来
  if(){//再次判断redis中有没有数据
    //有数据，直接取走
  }else{
    //没有数据，去mysql中去数据，并设置到redis中
  }
  
  
}
```



2. 在单体应用中使用了synchronized或者Retrantlock上锁了，但是单体应用为了高可用性，部署了多个，那么此时原本的synchronized或者Retrantlock上的锁还有用么？肯定是没用的！就必须改为redisson！