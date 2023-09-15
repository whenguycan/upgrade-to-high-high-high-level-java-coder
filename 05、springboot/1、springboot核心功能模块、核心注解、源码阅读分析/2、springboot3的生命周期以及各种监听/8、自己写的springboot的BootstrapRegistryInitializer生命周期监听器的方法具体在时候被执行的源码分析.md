## 自己写的springboot的BootstrapRegistryInitializer生命周期监听器的方法具体在时候被执行的源码分析

上文，我们自己写了一个springboot的BootstrapRegistryInitializer生命周期监听器，代码如下：

```java
package com.example.demolog.listener;

import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.BootstrapRegistryInitializer;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 1:29 PM
 * @Description: 类描述信息
 */
public class MyBootstrapRegitryInitializer implements BootstrapRegistryInitializer {
    @Override
    public void initialize(BootstrapRegistry registry) {
        System.out.println("init........");
    }
}

```



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

`createBootstrapContext()`方法内的代码如下：

```java
private DefaultBootstrapContext createBootstrapContext() {
  DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext(); 
  this.bootstrapRegistryInitializers.forEach((initializer) -> initializer.initialize(bootstrapContext));
  return bootstrapContext;
}
```

this.bootstrapRegistryInitializers.forEach(.....)遍历所有的bootstrapRegistryInitializer的initialize方法。