## 自己写一个springboot的ApplicationRunner、CommandLineRunner生命周期监听器





#### 1、在项目中新建一个listener目录



#### 2、编写代码如下：

ApplicationRunner代码如下

```java

@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner run.....");
    }
}

```



CommandLineRunner代码如下

```java
@Component
public class MyCommandLineRunner  implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner run ......");
    }
}
```

