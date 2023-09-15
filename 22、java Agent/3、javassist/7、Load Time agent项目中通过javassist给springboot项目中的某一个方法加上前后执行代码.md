## agent项目中通过javassist给springboot项目中的某一个方法加上前后执行方法



#### 1、在springboot项目中新增如下代码

```java
public class BitStringUtil {

    public String addString(int length){
        String result = "";
        for (int i =0; i < length; i++) {
            result += (char) (i % 26 + 'a');
        }

        return result;
    }
}
```



#### 2、在java agent项目中需要修改：

> 注意：
>
> 1. 因为目标应用是springboot项目，而agent应用只是一个maven项目，当使用命令java -jar 启动springboot应用将agent附着。在启动命令中Agent的jar包由AppClassLoader类加载器加载。而SpringBoot使用自定义的类加载器（LaunchedURLClassLoader）加载jar中的类和第三方jar包中的类，该类加载器的父类加载器为AppClassLoader。但是这其中有个可见性的问题，儿子装载的类可以看到爸爸装载的类，但反过来不行——爸爸装载的类看不到儿子装载的类。
> 2. 在java agent中，如果要修改方法的代码，我们一般不会直接修改方法的代码，而是将原本的方法代码复制出来，将代码放到一个新生成的代理方法中，后续我们都会这个代理方法进行操作！

- 新增依赖

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

- 修改premain方法

  ```java
  public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, IOException, NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
    System.out.println("Can-Redifined-Class:" + inst.isRedefineClassesSupported());
    System.out.println("Can-Retransform-Class:" + inst.isRetransformClassesSupported());
    System.out.println("Can-Set-Native-Method-Prefix" + inst.isNativeMethodPrefixSupported());
  
    System.out.println("Premain-class:" + LoadTimeAgent.class.getName());
  
  
  
    //创建一个ClassPool对象然后追加系统搜索路径到其中。
    ClassPool classPool = new ClassPool(true);
    //装载LaunchedURLClassLoader类，因为目标应用是springboot项目，在springboot中第三方jar包和自己开发的类都是由LaunchedURLClassLoader类加载的，但是javaagent中的类是有AppClassLoader加载的，所以在这儿需要手动获取到LaunchedURLClassLoader类。同时因为LaunchedURLClassLoader是由AppClassLoader加载的，所以这儿可以直接获得！
    CtClass ctClass = classPool.get("org.springframework.boot.loader.LaunchedURLClassLoader");
    //获取到LaunchedURLClassLoader类的所有的构造方法
    CtConstructor[] ctConstructors = ctClass.getDeclaredConstructors();
    System.out.println(ctConstructors[2].toString());
    //根据到第三个构造方法，并在其构造方法前插入我们自己写的modifyClass类的modifyClass方法。
    ctConstructors[2].insertAfter("com.example.modifyClass.modifyClass(this);");
    //写回JVM，让修改生效
    ctClass.toClass();
  }
  ```

- modifyClass类如下

  ```java
  public class modifyClass {
  
      public static void modifyClass(ClassLoader loader){
          try {
  
              System.out.println("当前的类加载器为：" + loader.toString());
  
            	//获取到当前默认的classloader中所有的类
              ClassPool classPool = new ClassPool(true);
            	//将LaunchedURLClassLoader加载器的类路径加入到classpool中
              classPool.appendClassPath(new LoaderClassPath(loader));
  
              String targentClassName = "com.example.agentspringboot.utils.BitStringUtil";
              //根据类名获取到要修改的class
              CtClass targetClass = classPool.get(targentClassName);
  
              //根据方法名获取到要修改的方法名
              CtMethod method = targetClass.getDeclaredMethod("addString");
  
              //拷贝方法到代理方法
              // 第一个参数，是被拷贝方法
              // 第二个参数，新方法的方法名
              // 第三个参数，执行新方法归属哪个类
              // 第四个参数，null即可
              CtMethod newMethod = CtNewMethod.copy(method, method.getName() + "$agent", targetClass, null);
              //将新方法写到类中
              targetClass.addMethod(newMethod);
  
              String src = "{" +
                      "long begin = System.nanoTime();" +
                      "long end = System.nanoTime();" +
                      "Object result = " + method.getName() + "$agent($$);" +
                      "System.out.println(end-begin);" +
                      "return ($r)result;" +
                      "}";
              method.setBody(src);
  
              //将修改好的类写入到JVM，注意，这儿写入JVM，需要指定classLoader和domain的，不能乱写！
              targetClass.toClass(loader, targetClass.getClass().getProtectionDomain());
  
  
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
  
  
  }
  ```

- agent项目打包

- 使用如下命令运行springboot项目

  ```shell
  java -javaagent:/Users/tangwei/javaProject/demo-agent/target/demo-agent-0.0.1-SNAPSHOT-jar-with-dependencies.jar -jar agent-springboot-0.0.1-SNAPSHOT.jar
  ```

- 在springboot项目中写个controller，代码如下

  ```java
  @GetMapping("/index")
  public void index() throws InterruptedException {
    System.out.println(bitStringUtil.addString(10));
  }
  ```

  然后尝试调用/index，看看会不会把指定时间打印出来！

