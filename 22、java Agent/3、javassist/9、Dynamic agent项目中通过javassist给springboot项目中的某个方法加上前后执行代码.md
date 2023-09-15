## Dynamic agent项目中通过javassist给springboot项目中的某个方法加上前后执行代码

> 此文档的代码是在 LoadTime agent的项目之下进行了一定的修改



#### 1、需要引入spring-boot-loader的依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-loader -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-loader</artifactId>
</dependency>


<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.6.13</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

因为，我们在代码中，是拿到LunchedURLClassLoader然后模拟修改它，触发ClassFileTransFormer的，所以需要引入它



#### 2、需要javassist依赖

具体去看agent项目中引入javassist



#### 3、修改agent的入口代码

```java
import java.lang.instrument.Instrumentation;

/**
 * @Auther: tangwei
 * @Date: 2023/5/9 1:42 PM
 * @Description: 类描述信息
 */
public class DynamicAgent {

    public static void agentmain(String AgentArgs, Instrumentation inst){

        InfoTransformer infoTransformer = new InfoTransformer();

        try{
            inst.addTransformer(infoTransformer, true);//注意，这儿一定要为true
          
          	//这儿只是为了让InfoTransformer对象能够执行
            inst.retransformClasses(Class.forName("org.springframework.boot.loader.LaunchedURLClassLoader"));

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            //下面一行 一旦带了，就无法实现探针了
            //inst.removeTransformer(infoTransformer);
        }
    }
}

```



#### 4、InfoTransformer对象代码

触发修改LaunchedURLClassLoader类的时候执行

```java
package com.example.agent;

//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateTime;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Formatter;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:42 PM
 * @Description: 类描述信息
 */
public class InfoTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //获取到当前默认的classloader中所有的类
        ClassPool classPool = new ClassPool(true);
        //将LaunchedURLClassLoader加载器的类路径加入到classpool中
        classPool.appendClassPath(new LoaderClassPath(loader));

        String targentClassName = "com.example.agentspringboot.HelloWorldController";
        //根据类名获取到要修改的class
        try {
            CtClass targetClass = classPool.get(targentClassName);

            System.out.println("获取到的类对象为："+targetClass);
          
          	//下面就可以使用javassist对类内容进行修改了！完毕
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}

```



