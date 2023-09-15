## springboot调整输出的日志级别

日志的级别，从高到低为：FATAL、ERROR、WARN、INFO、DEBUG、TRACE



#### 调整项目中所有的日志级别

配置日志级别：

```yaml
logging:
  level:
    root: warn   #指定整个项目的日志级别是warn，那么输出的日志级别高于、等于warn才会被输出
```

写代码测试：

```java
@Slf4j
@RestController
public class IndexController {

    @GetMapping("/index")
    public String index(){

        log.error("error");
        log.warn("warn");
        log.info("info");
        log.debug("debug");
        log.trace("trace");


        return "springboot3";
    }

}
```





#### 调整项目中某个类、某个包的日志级别

配置日志级别

```yaml
logging:
  level:
    com.example.demolog.controller: info  #com.example.demolog.controller是指日志的级别精确到这个包下的所有文件，不被包含的文件都默认使用root的级别
```

写代码测试：

```java
@Slf4j
@RestController
public class IndexController {

    @GetMapping("/index")
    public String index(){

        log.error("error");
        log.warn("warn");
        log.info("info");
        log.debug("debug");
        log.trace("trace");


        return "springboot3";
    }

}
```

