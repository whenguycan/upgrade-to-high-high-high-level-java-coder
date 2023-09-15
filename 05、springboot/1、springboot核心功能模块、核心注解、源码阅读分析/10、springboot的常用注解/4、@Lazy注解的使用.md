## @Lazy注解的使用

在springboot中，创建bean且使用单例的时候，默认在springboot启动的时候bean就已经创建好了，而有些bean，我们可能需要等到使用（调用）的时候再去创建它，就可以使用@Lazy注解。

有一个User类如下

```java
public class User {

    public Integer age;

    public User() {

        System.out.println("User created");

    }
}
```

Config如下：

```java
@SpringBootConfiguration
public class MyConfig {

    @Lazy
    @Bean
    public User user(){
        return new User();
    }

}
```



当项目中调用了这个user的bean的时候，才会创建这个user的bean，并加入到IOC中

使用@Autowired等方式都会触发创建。