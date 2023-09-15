## springboot使用@ControllerAdvice + @ExceptionHandler统一错误处理



编写一个统一错误处理的代码

```java
@RestControllerAdvice //basePackages如果不指定基础包，默认就是整个项目
public class ExceptionControllerAdvice {
    @ExceptionHandler(Exception.class) //统一异常处理只接收MethodArgumentNotValidException这个异常
    public HashMap returnRes(Exception exc){
        LinkedHashMap<String, String> res = new LinkedHashMap<>();

        System.out.println(exc.getMessage());

        return res;
    }
}
```

