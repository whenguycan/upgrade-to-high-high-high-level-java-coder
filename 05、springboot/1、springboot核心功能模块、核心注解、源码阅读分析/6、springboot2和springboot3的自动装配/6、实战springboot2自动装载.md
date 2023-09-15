## 实战springboot2自动装配



#### 操作过程

创建demo-projecta、demo-projectb两个项目，都使用springboot2.6.13的版本



在demo-projecta中随便建一个util工具类，只是为了模拟

```java
package com.example.utils;

/**
 * @Auther: tangwei
 * @Date: 2023/6/30 9:55 AM
 * @Description: 类描述信息
 */
public class DemoUtil {

    public void index(){

    }
}

```

然后新建一个configutaion类，代码如下

```java
package com.example.config;

import com.example.utils.DemoUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: tangwei
 * @Date: 2023/6/30 9:53 AM
 * @Description: 类描述信息
 */
@Configuration
public class TestAutoConfiguration {

    @Bean
    public DemoUtil demoUtil(){
      	System.out.println("new demo");
        return new DemoUtil();
    }
}

```



创建resources/META-INF/spring.factories文件，写入内容如下：

```tex
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.config.TestAutoConfiguration
```



随后对demo-projecta项目打包并安装到本地maven仓库中，注意打包的时候要去掉`spring-boot-maven-plugin`，因为打的包不能是一个fat-jar即不能包含其它依赖的jar包！





进入demo-projectb项目，引入demo-projecta的依赖

```xml
<dependency>
  <groupId>com.example</groupId>
  <artifactId>demo-projecta</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

然后直接启动项目，看看控制台会不会打印出`new demo`，打印出来证明我们的测试成功了！







