## springboot实战使用redisson



#### 1、引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.redisson/redisson -->
<dependency>
  <groupId>org.redisson</groupId>
  <artifactId>redisson</artifactId>
  <version>3.23.3</version>
</dependency>
```



#### 2、新增配置文件RedissonConfig：

> 根据redis的集群方式不同，RedissonConfig的配置也不同，具体可以参考官方：https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95#24-%E9%9B%86%E7%BE%A4%E6%A8%A1%E5%BC%8F

redis单机部署下RedissonConfig的配置：

```java
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.10.210.19:30002").setDatabase(1).setPassword(xxx);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
```



#### 3、Redisson的使用

> 各种使用方法参考官方文档：https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8

- 锁的使用

  ```java
  @Autowired
  RedissonClient redissonClient; //获取到上面配置文件中初始化的redissonClient
  
  @GetMapping("/index")
  public void index() throws InterruptedException {
  	
    //设置锁名称，注意RLock跟java的ReentrantLock一致，是可重入锁！
    RLock lock = redissonClient.getLock("index2");
    //尝试加锁，加锁最多等待100秒，加锁成功后60秒释放锁
    boolean lockRes = lock.tryLock(100, 60, TimeUnit.SECONDS);
    if (lockRes){
      System.out.println(Thread.currentThread().getId() + "成功获取到锁");
      Thread.sleep(30000L);
  		
      //判断当前是否在使用，且被当前线程占用
      if(lock.isLocked() && lock.isHeldByCurrentThread()){
        //释放锁
        lock.unlock();
      }
      System.out.println(Thread.currentThread().getId() + "成功释放锁");
    }else{
      System.out.println(Thread.currentThread().getId() + "没有获取到锁");
    }
  
  }
  ```

- 信号量的使用

  ```java
  @Autowired
  private RedissonClient redissonClient;
  
  
  RSemaphore rsem = redissonClient.getSemaphore("goods_id");//获取到一个信号量，goods_id需要是redis中String类型的一个key，值为一个数字。不排除高并发下，2个进程同时获取到信号量的可能！！
  rsem.tryAcquire(2, 100, TimeUnit.MILLISECONDS);//尝试对信号量中的value-2，最多等待100毫秒，如果超时返回false，如果对value成功-2返回true，否则返回false。并发情况下，也是排队等待的。
  ```

  

