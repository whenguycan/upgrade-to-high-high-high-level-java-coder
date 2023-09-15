## springboot集成redis



#### 1、引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```



#### 2、新增配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
```



#### 3、直接开始写入数据会有问题

需要先配置一个config，设置好各种数据类型的编码类型，不然会出现乱码问题和空指针错误！

```java
@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }
}
```



#### 4、测试使用

写个string类型的数据到redis中试试看。

```java
@Autowired
private RedisTemplate redisTemplate;



ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.set("username2", "唐炜");
```

