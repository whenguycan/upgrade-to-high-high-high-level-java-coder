## JVM编译器的分类和分层编译的概念



#### 1、JVM编译器的分类

JVM中集成了两种编译器，Client Compiler和Server Compiler；

- Client Compiler注重启动速度和局部的优化
- Server Compiler则更加关注全局的优化，性能会更好，但由于会进行更多的全局分析，所以启动速度会变慢。



Client Compiler：

HotSpot VM带有一个Client Compiler C1编译器。

这种编译器启动速度快，但是编译后机器码执行性能比较Server Compiler来说会差一些。



Server Compiler：

Hotspot虚拟机中使用的Server Compiler有两种：C2和Graal。

默认的Server Compiler是C2编译器。



#### 2、怎么确定当前的JVM使用哪个编译器呢？

使用`java -version`查看，输出

```shell
java version "1.8.0_201"
Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
```

`mixed mode`是指C1和C2编译器的混合模式。



#### 3、分层编译

Java 7开始引入了分层编译的概念，它结合了C1和C2的优势，追求启动速度和峰值性能的一个平衡。分层编译将JVM的执行状态分为了五个层次。五个层级分别是：

1. 解释执行。
2. 执行不带profiling的C1代码。
3. 执行仅带方法调用次数以及循环回边执行次数profiling的C1代码。
4. 执行带所有profiling的C1代码。
5. 执行C2代码。

profiling就是收集能够反映程序执行状态的数据。其中最基本的统计数据就是方法的调用次数，以及循环回边的执行次数。

![avatar](../images/11111111.png)

- 图中第①条路径，代表编译的一般情况，热点方法从解释执行到被3层的C1编译，最后被4层的C2编译。
- 如果方法比较小（比如Java服务中常见的getter/setter方法），3层的profiling没有收集到有价值的数据，JVM就会断定该方法对于C1代码和C2代码的执行效率相同，就会执行图中第②条路径。在这种情况下，JVM会在3层编译之后，放弃进入C2编译，直接选择用1层的C1编译运行。
- 在C1忙碌的情况下，执行图中第③条路径，在解释执行过程中对程序进行profiling ，根据信息直接由第4层的C2编译。
- C1中的执行效率是1层>2层>3层，第3层一般要比第2层慢35%以上，所以在C2忙碌的情况下，执行图中第④条路径。这时方法会被2层的C1编译，然后再被3层的C1编译，以减少方法在3层的执行时间。
- 如果编译器做了一些比较激进的优化，比如分支预测，在实际运行时发现预测出错，这时就会进行反优化，重新进入解释执行，图中第⑤条执行路径代表的就是反优化。

总的来说，C1的编译速度更快，C2的编译质量更高，分层编译的不同编译路径，也就是JVM根据当前服务的运行情况来寻找当前服务的最佳平衡点的一个过程。从JDK 8开始，JVM默认开启分层编译。

