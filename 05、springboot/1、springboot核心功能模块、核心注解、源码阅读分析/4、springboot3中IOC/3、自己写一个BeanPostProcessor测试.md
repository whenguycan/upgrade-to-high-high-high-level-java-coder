## 自己写一个BeanPostProcessor测试





#### 写一个IndexController，代码如下

```java
@Slf4j
@RestController
public class IndexController {


    @GetMapping("/index")
    public String index(){

      
        return "";

    }

}
```



#### 自己写一个BeanPostProcessor，代码如下

```java
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("indexController")){
            System.out.println(bean.getClass());
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("indexController")){
            System.out.println(bean.getClass());
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}

```

