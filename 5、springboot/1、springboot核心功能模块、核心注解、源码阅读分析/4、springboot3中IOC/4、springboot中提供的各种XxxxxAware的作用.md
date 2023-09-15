## springboot中提供的各种XxxxxAware的作用

XxxxxAware都来自一个interface叫Aware，旨在让我们在自定义的bean中（各种代码中）可以使用spring容器对象中一些底层的功能。



#### 自己写一个类，使用ApplicationContextAware

```java
@Component
public class MyAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //applicationContext 就是ioc容器

        System.out.println("ioc为" + applicationContext.toString());

    }
}

```

每一个XxxxxAware都会有一个XxxxAwareProcessor来处理对应的XxxxxAware。每一个XxxxxAwareProcessor都是一个BeanPostProcessor，因为都实现了BeanPostProcessor接口！