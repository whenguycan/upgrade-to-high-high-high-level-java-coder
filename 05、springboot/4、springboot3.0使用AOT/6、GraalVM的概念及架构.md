## GraalVM的概念及架构



#### 1、GraalVM是什么

GraalVM是一个高性能的JDK，旨在加速用Java和其它JVM语言编写的应用程序的执行，同时还提供Javascript、Python和其它流行语言的运行时环境。



#### 2、GraalVM提供两种运行java应用程序的方式

- 在JVM基础上使用Graal即使编译器
- 将java代码直接编译成可执行文件（**AOT**），直接执行。<font color="red">**我们一般使用GraalVM就使用这个**</font>

GraalVM 的多语言功能可以在单个应用程序中混合多种编程语言，同时消除外语调用成本。



#### 3、GraalVM的架构图

![avatar](../images/v2-62f1a9f01496bf9fc45e6d3fb25d63c6_1440w.webp)

上图的

![avatar](../images/WechatIMG618.png)

就是一个完整的GraalVM。

然后针对不同的语言，都会一次性打包成可执行文件，直接可执行！



