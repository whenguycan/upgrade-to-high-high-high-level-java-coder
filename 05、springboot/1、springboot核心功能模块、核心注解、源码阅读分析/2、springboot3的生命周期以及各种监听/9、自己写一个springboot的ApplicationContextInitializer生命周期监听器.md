## 自己写一个springboot的ApplicationContextInitializer生命周期监听器



#### 1、在项目中新建一个listener目录



#### 2、在目录中新建一个java文件，内容如下

> 一定要实现`ApplicationContextInitializer`接口

```java
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("xxxxx......");
    }
}
```





#### 3、在项目resources目录中，新建META-INF目录



#### 4、在META-INF中，新建spring.factories文件，内容如下

```java
org.springframework.context.ApplicationContextInitializer=com.example.demolog.listener.MyApplicationContextInitializer
```

一定要是`xxxx=yyyy`的形式，等号左边是`ApplicationContextInitializer`接口，等号右边是`ApplicationContextInitializer`接口的实现类。

