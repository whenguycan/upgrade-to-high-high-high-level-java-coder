## ClassFileTransformer作用



agent中可以提供此接口的实现以转换类文件。转换发生在 JVM 定义类之前。



接口代码如下：

一般在实际使用中都是实现该接口并重写transform方法！

```java
public interface ClassFileTransformer {
    byte[] transform(ClassLoader         loader,
                     String              className,
                     Class<?>            classBeingRedefined,
                     ProtectionDomain    protectionDomain,
                     byte[]              classfileBuffer)
        throws IllegalClassFormatException;
}
```

参数如下：

- `loader` 要转换的类的定义加载器，如果是引导加载器，则为 null
- `className`  需要读取进行转换的类全路径。例如，“java/util/List”
- `classBeingRedefined` 如果这是由重新定义或重新转换触发的，则重新定义或重新转换的类；如果这是一个类加载，则为 null
- `protectionDomain` 正在定义或重新定义的类的保护域
- `classfileBuffer` 读取到需要转换的文件的字节数组的内容，<font color="red">然后，我们一般需要使用javassist、ASM、ByteBuddy等字节码类库去操作！</font>





被加入的ClassFileTransformer的执行时机：

对`ClassFileTransformer.transform()`方法调用的时机有三个：

1. 需要被transform的类正在被加载，在Load-Time Agent模式下<font color="red">自动</font>调用`ClassLoad.defineClass`方法的时候，即如下图：

   <img src="/Users/tangwei/Desktop/课件/22、java Agent/images/WechatIMG577.png" alt="avatar" style="zoom: 25%;" />

2. 需要被transform的类已经加载，手动调用`Instrumentation.redefineClasses`方法的时候

   

3. 需要被transform的类已经加载，手动调用`Instrumentation.retransformClasses`方法的时候

   ![avatar](/Users/tangwei/Desktop/课件/22、java Agent/images/define-redefine-retransform.png)





返回如下：

经过转换逻辑操作之后的字节数据（转换后的结果），如果未执行转换，则为 null。



注意事项：

在transform方法中，禁止处理jdk包中的类和当前自己写的agent包中的类。



<font color="red">如果往Instumentation中加入了多个ClassFileTransFormer，那么多个ClassFileTransFormer是一个串联的关系，前一个ClassFileTransFormer的输出做为后一个ClassFileTransFormer的输入！且，前一个ClassFileTransFormer抛出异常，不影响后一个ClassFileTransFormer</font>



小总结：

- `loader`: 如果值为`null`，则表示bootstrap loader。
- `className`: 表示internal class name，例如`java/util/List`。
- `classfileBuffer`: 一定不要修改它的原有内容，可以复制一份，在复制的基础上就进行修改。
- 返回值: 如果返回值为`null`，则表示没有进行修改。