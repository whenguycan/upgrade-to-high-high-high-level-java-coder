## java.lang.Instrument包概述



#### 1、包在哪里

- 在Java 8版本中，位于`rt.jar`文件
- 在Java 9版本之后，位于 `java.instrument` 模块（`JDK_HOME/jmods/java.instrument.jmod`）



#### 2、包中包含了哪些类和接口

```
                        ┌─── ClassDefinition (类)
                        │
                        ├─── ClassFileTransformer (接口)
                        │
java.lang.instrument ───┼─── Instrumentation (接口)
                        │
                        ├─── IllegalClassFormatException (异常)
                        │
                        └─── UnmodifiableClassException (异常)
```



#### 3、包的作用是什么？

- 它定义了一些“规范”，比如 Manifest 当中的 `Premain-Class` 和 `Agent-Class` 属性，再比如 `premain` 和 `agentmain` 方法，这些“规范”是 Agent Jar 必须遵守的。
- 它定义了一些类和接口，例如 `Instrumentation` 和 `ClassFileTransformer`，这些类和接口允许我们在 Agent Jar 当中实现修改某些类的字节码。