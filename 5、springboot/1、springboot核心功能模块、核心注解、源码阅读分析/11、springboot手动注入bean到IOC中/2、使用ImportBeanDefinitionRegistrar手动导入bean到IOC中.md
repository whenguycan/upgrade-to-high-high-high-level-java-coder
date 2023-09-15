## 使用ImportBeanDefinitionRegistrar手动导入bean到IOC

有一个类，代码如下

```java
public class Duck {

}
```



使用ImportBeanDefinitionRegistrar手动导入bean

```java
public class MyBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    /** 方法说明
     * 可以认为是beanDefinition的注册中心。所有的beanDefinition都会被注册到这里面去。
     * 有如下的功能：
     *      1. 以Map<String, BeanDefinition>的形式注册bean
     *      2. 根据beanName 删除和获取 beanDefiniation
     *      3. 得到持有的beanDefiniation的数目
     *      4. 根据beanName 判断是否包含beanDefiniation
     */

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        registry.registerBeanDefinition("duck", new RootBeanDefinition(Duck.class));//将Duck这个类包装成一个RootBeanDefinition，并最终生成bean的时候以duck命名bean
    }
}

```



使用了ImportBeanDefinitionRegistrar，就使用要使用@Import，这一步是必须的！

```java
@Import({MyBeanDefinitionRegister.class})
@SpringBootConfiguration
public class MyConfig {


}
```



测试Duck是否加入bean成功

```java
@SpringBootApplication
public class DemoLogApplication {

    public static void main(String[] args) {
        var ioc = SpringApplication.run(DemoLogApplication.class, args);

        for(String beanName: ioc.getBeanNamesForType(Duck.class)){ //获取到ioc中的所有Duck类的bean
            System.out.println("************************");
            System.out.println(beanName);
        }


    }

}
```

