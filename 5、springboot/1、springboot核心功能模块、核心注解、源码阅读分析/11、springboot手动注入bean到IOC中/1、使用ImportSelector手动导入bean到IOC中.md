## 使用ImportSelector手动导入bean



有一个类，代码如下

```java
public class Duck {

}
```



使用ImportSelector手动导入bean

```java
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        String[] beans = new String[]{
            "com.example.demolog.config.Duck" //这儿写要导入类的全路径
        };

        return beans;
    }
}
```



使用了ImportSelector，就使用要使用@Import，这一步是必须的！

```java
@Import({MyImportSelector.class}) //让MyImportSelector类生效
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

