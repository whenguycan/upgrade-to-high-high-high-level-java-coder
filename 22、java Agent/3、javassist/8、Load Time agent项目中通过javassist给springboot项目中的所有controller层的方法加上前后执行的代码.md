## agent项目中通过javassist给springboot项目中的所有controller层的方法加上前后执行的代码



#### 1、在springboot中写一个controller类，代码如下：

```java
@RestController
public class HelloWorldController {

    @Autowired
    BitStringUtil bitStringUtil;

    @GetMapping("/index")
    public void index() throws InterruptedException {
        System.out.println(bitStringUtil.addString(10));
    }

    @GetMapping("/index2")
    public void index2(){
        System.out.println("index2");
    }
}
```

springboot项目打包



#### 2、在agent中需要做如下修改



- 在入口类中新增一个classFileTransFormer

  ```java
  public class LoadTimeAgent {
  
      public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, IOException, NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
          System.out.println("Can-Redifined-Class:" + inst.isRedefineClassesSupported());
          System.out.println("Can-Retransform-Class:" + inst.isRetransformClassesSupported());
          System.out.println("Can-Set-Native-Method-Prefix" + inst.isNativeMethodPrefixSupported());
  
          System.out.println("Premain-class:" + LoadTimeAgent.class.getName());
  
  				//新增一个transformer
          InfoTransformer infoTransformer = new InfoTransformer();
          inst.addTransformer(infoTransformer);
      }
  }
  ```

- InfoTransformer类的内容如下

  ```java
  package com.example.agent;
  import javassist.*;
  
  import java.io.*;
  import java.lang.instrument.ClassDefinition;
  import java.lang.instrument.ClassFileTransformer;
  import java.lang.instrument.IllegalClassFormatException;
  import java.security.ProtectionDomain;
  
  /**
   * @Auther: tangwei
   * @Date: 2023/5/6 12:42 PM
   * @Description: 类描述信息
   */
  public class InfoTransformer implements ClassFileTransformer {
  
  
  
      public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
  
          //当加载的类是在com/example/agentspringboot包中
          if(className.contains("com/example/agentspringboot")){
              System.out.println(className);
              //对所有的Controller结尾的类
              if (className.endsWith("Controller")){
                  // 获取一个 class 池。
                  ClassPool classPool = ClassPool.getDefault();
                  classPool.appendClassPath(new LoaderClassPath(loader));//将当前的classloader的classpath加入到搜索路径中
  
                  try {
                      // 创建一个新的 class 类。classfileBuffer 就是当前class的字节码
                      CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                      //判断类是否被冻结
                      if(ctClass.isFrozen()){
                          //如果被冻结了需要先解冻
                          ctClass.defrost();
                      }
                      //获取到目标类的所有方法
                      CtMethod[] personFly = ctClass.getDeclaredMethods();
                      for (CtMethod ctMethod : personFly) {
                          ctMethod.insertBefore("System.out.println(\"起飞之前准备降落伞\");");
                          ctMethod.insertAfter("System.out.println(\"执行后清理\");");
                      }
                      // 返回新的字节码入JVM中
                      return ctClass.toBytecode();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
  
          }
        	// 不使用原始的方法对class进行任何修改，直接返回null
          return null;
  
      }
  }
  
  ```

  然后agent项目打包



#### 3、使用如下命令运行springboot项目

```shell
java -javaagent:/Users/tangwei/javaProject/demo-agent/target/demo-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar -jar agent-springboot-0.0.1-SNAPSHOT.jar
```



#### 4、访问springboot的2个方法，看看是否插入了内容

