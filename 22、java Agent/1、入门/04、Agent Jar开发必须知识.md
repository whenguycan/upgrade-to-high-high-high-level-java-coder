## Agent Jar的开发中要用的三个重要组成部分



#### 1、Java Agent打成的.jar包中，有三个重要组成部分

- Manifest，作用是对jar属性信息进行记录
- Agent Class
- 一系列ClassFileTransformer.class文件

![avatar](../images/MG422.jpeg)





#### 2、Manifest详解

在Manifest文件中，可以定义的属性非常多，但是与Java Agent相关的属性只有6、7个。

- 在Java8版本中，定义的属性有6个
- 在Java9至java17版本中，定义属性有7个。其中，Launcher-Agent-Class属性，是Java9引入的。



其次，我们将Manifest定义的属性分成了三组：基础、能力和特殊情况。

```text
                                       ┌─── Premain-Class
                       ┌─── Basic ─────┤
                       │               └─── Agent-Class
                       │
                       │               ┌─── Can-Redefine-Classes
                       │               │
Manifest Attributes ───┼─── Ability ───┼─── Can-Retransform-Classes
                       │               │
                       │               └─── Can-Set-Native-Method-Prefix
                       │
                       │               ┌─── Boot-Class-Path
                       └─── Special ───┤
                                       └─── Launcher-Agent-Class
```

分组的目的，是为了便于理解：一下子记住7个属性，不太容易；分成三组，每次记忆两、三个属性，就相对容易一些。



1. 基础

   - Premain-Class: 当agent jar是使用命令行方式启动即`-javaagent:jarpath`方式启动，即使用Load-Time instrumentation时，那么这个属性不能为空。这个属性中记录的是一个类的全路径，该类中必须有一个premain方法。
   - Agent-Class: 当agent jar是使用虚拟机提供的attach机制启动即`attach`方式启动，即使用Dynamic instrumentation时，那么这个属性不能为空。这个属性中记录的是一个类的全路径，该类中必须有一个`agentmain`方法。

2. 能力

   能力，体现在两个层面上：JVM和 Java Agent。

   ```tex
                                 ┌─── redefine
                                 │
              ┌─── Java Agent ───┼─── retransform
              │                  │
              │                  └─── native method prefix
   Ability ───┤
              │                  ┌─── redefine
              │                  │
              └─── JVM ──────────┼─── retransform
                                 │
                                 └─── native method prefix
   ```

   下面三个属性（<font color="red">后面会详细学这三个属性</font>），就是确定Java Agent的能力：

   - Can-Redefine-Classes: 是否能够重新定义此代理所需的类。 true 以外的值被认为是 false。该属性是可选的，默认为 false。
   - Can-Retransform-Classes: 是重新转换此代理所需的类的能力。 true 以外的值被认为是 false。该属性是可选的，默认为 false。
   - Can-Set-Native-Method-Prefix: 是否能够设置此代理所需的本机方法前缀。 true 以外的值被认为是 false。该属性是可选的，默认为 false。

3. 特殊情况

   - Boot-Class-Path: agent jar的引导类加载器（bootstrap class loader）要搜索的路径列表（jvm启动的时候，有3个类加载器分别为引导类加载器、扩展类加载器、应用类加载器）。路径表示目录或库（在许多平台上通常称为 JAR 或 zip 库）。在定位类的平台特定机制失败后，引导类加载器将搜索这些路径。按照列出的顺序搜索路径。列表中的路径由一个或多个空格分隔。路径采用分层 URI 的路径组件的语法。如果路径以斜杠字符 (/) 开头，则它是绝对路径，否则它是相对的。相对路径根据代理 JAR 文件的绝对路径进行解析。格式错误和不存在的路径将被忽略。当代理在 VM 启动后的某个时间启动时，不代表 JAR 文件的路径将被忽略。该属性是可选的。
   - Launcher-Agent-Class：如果要实现将agent jar作为可执行 JAR 启动的时候，则主清单可能包含此属性以指定在调用应用程序主方法之前启动的代理的类名。



#### 3、Agent Class

1. LoadTimeAgent:

   如果我们想使用Load-Time Agent，那么就必须有一个`premain`方法，它有两种写法。

   The JVM first attempts to invoke the following method on the agent class:（<font color="red">推荐使用</font>）

   ```java
   public static void premain(String agentArgs, Instrumentation inst);
   ```

   If the agent class does not implement this method then the JVM will attempt to invoke:

   ```java
   public static void premain(String agentArgs);
   ```

2. DynamicAgent:

   如果我们想使用Dynamic Agent，那么就必须有一个`agentmain`方法，它有两种写法。

   The JVM first attempts to invoke the following method on the agent class:（<font color="red">推荐使用</font>）

   ```java
   public static void agentmain(String agentArgs, Instrumentation inst);
   ```

   If the agent class does not implement this method then the JVM will attempt to invoke:

   ```java
   public static void agentmain(String agentArgs);
   ```

3. ClassFileTransformer:

   在`java.lang.instrument`下包含了`Instrumentation`和`ClassFileTransformer`接口：

   - `java.lang.instrument.Instrumentation`
   - `java.lang.instrument.ClassFileTransformer`

   在`Instrumentation`接口中，定义了添加和移除`ClassFileTransformer`的方法：

   ```java
   public interface Instrumentation { 
       void addTransformer(ClassFileTransformer transformer, boolean canRetransform); //添加ClassFileTransformer到agent jar中。只有ClassFileTransformer被添加了，后续才能修改class文件
   
       boolean removeTransformer(ClassFileTransformer transformer);
   }
   ```

   在`ClassFileTransformer`接口中，定义了`transform`抽象方法：

   ```java
   public interface ClassFileTransformer {
       byte[] transform(ClassLoader         loader, //加载当前类的加载器
                        String              className, //当前加载的类名
                        Class<?>            classBeingRedefined, 
                        ProtectionDomain    protectionDomain,
                        byte[]              classfileBuffer //要修改类的字节码内容
                       
                       ) throws IllegalClassFormatException;
               
   }
   ```
   
   当我们想对Class进行bytecode instrumentation时，就要实现`ClassFileTransformer`接口，并重写它的`transform`方法。





#### 4、总结

```
Manifest --> Agent Class --> Instrumentation --> ClassFileTransformer
```

根据Mainfest中定义的Agent Class的路径找到对应的类，然后根据找到的Agent class的类找到对应的Instrumentation类，就可以找到所有add进来的ClassFileTransformer文件。

