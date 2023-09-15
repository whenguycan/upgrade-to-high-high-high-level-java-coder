## 4、自定义的springboot的SpringApplicationRunListener生命周期监听器的方法具体在什么时候被执行的源码分析



上文，我们自己写了一个springboot的SpringApplicationRunListener生命周期监听器，代码如下

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

这其中的starting、started、ready等方法，是在什么时候被执行的呢？

我们看下源码，在项目的入口文件中，代码如下：

```java
public static void main(String[] args) {
  SpringApplication.run(DemoLogApplication.class, args);
}
```

随后会进入如下的方法中

```java
public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
  return new SpringApplication(primarySources).run(args);
}
```

调用了`run`方法，run方法层层调用最后进入到了SpringApplication中的run方法

```java
public ConfigurableApplicationContext run(String... args) {
  long startTime = System.nanoTime();
  DefaultBootstrapContext bootstrapContext = createBootstrapContext();
  ConfigurableApplicationContext context = null;
  configureHeadlessProperty();
  SpringApplicationRunListeners listeners = getRunListeners(args); //这儿加载了所有的生命周期监听器
  listeners.starting(bootstrapContext, this.mainApplicationClass); //调用starting方法
  try{
    
    ......
      
    ConfigurableEnvironment environment = prepareEnvironment(listeners, bootstrapContext, applicationArguments); //调用environmentPrepared方法
    
    ......
      
    prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner);
    //调用contextPrepared、contextLoaded方法
    ......
    
    listeners.started(context, timeTakenToStartup); //调用started方法
    
  }catch(Throwable ex){
    
    handleRunFailure(context, ex, listeners); //调用failed方法
  }
  
  try {
			if (context.isRunning()) {
				......
				listeners.ready(context, timeTakenToReady); //调用ready方法
			}
		}
}
```

