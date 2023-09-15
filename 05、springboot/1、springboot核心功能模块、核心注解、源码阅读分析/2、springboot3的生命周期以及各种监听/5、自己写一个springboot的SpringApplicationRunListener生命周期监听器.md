## 自己写一个springboot的SpringApplicationRunListener生命周期监听器



#### 1、在项目中新建一个listener目录



#### 2、在目录中新建一个java文件，内容如下

> 以下类的各个方法，正好可以对应springboot的生命周期的各个阶段。
>
> MyListener类一定要实现`SpringApplicationRunListener`接口

```java
package com.example.demolog.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 9:33 AM
 * @Description: 类描述信息
 */
public class MyListener implements SpringApplicationRunListener {
  
  	//在java8中需要一个构造方法，在java17中是不需要的
  	public MyListener(SpringApplication application, String[] args){
      
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {

        System.out.println("正在初始化....");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("环境准备就绪");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("上下文准备");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("上下文准备就绪");
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("已成功启动");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("启动异常");
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("系统准备就绪");
    }
}

```



#### 3、在项目resources目录中，新建META-INF目录



#### 4、在META-INF中，新建spring.factories文件，内容如下

```java
org.springframework.boot.SpringApplicationRunListener=com.example.demolog.listener.MyListener
```

一定要是`xxxx=yyyy`的形式，等号左边是`SpringApplicationRunListener`接口，等号右边是`SpringApplicationRunListener`接口的实现类。