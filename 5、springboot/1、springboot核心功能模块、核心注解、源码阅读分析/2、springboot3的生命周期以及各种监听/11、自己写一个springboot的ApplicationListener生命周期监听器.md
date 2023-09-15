## 自己写一个springboot的ApplicationListener生命周期监听器



#### 1、在项目中新建一个listener目录



#### 2、在目录中新建一个java文件，内容如下

> 一定要实现`ApplicationListener`接口

```java
package com.example.demolog.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 3:47 PM
 * @Description: 类描述信息
 */
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("获取到触发事件" + event.getClass().getName());
    }
}

```





#### 3、在项目resources目录中，新建META-INF目录



#### 4、在META-INF中，新建spring.factories文件，内容如下

```java
org.springframework.context.ApplicationListener =com.example.demolog.listener.MyApplicationListener
```

一定要是`xxxx=yyyy`的形式，等号左边是`ApplicationListener`接口，等号右边是`ApplicationListener`接口的实现类。



#### 5、运行项目后观察

参考 `2、springboot完整的生命周期流程的各个阶段以及各个监听器的作用`文章中的图，是不是那么多event触发。