## 调试pod中的java应用的方法的入参、返回



#### 1、在pod交互模式下，启动arthas

```shell
kubectl exec -it ${pod} --container ${containerId} -- /bin/bash -c "wget https://arthas.aliyun.com/arthas-boot.jar && java -jar arthas-boot.jar"
```

但是，在多数情况下，仅凭上面的命令是无法执行成功的，因为，比如 有些镜像没有wget工具，有些环境无法连接外网下载等等，所以，我们需要在制作容器镜像的时候就把arthas的包下载好并放到容器中

那么在制作我们的docker镜像的dockerfile中需要添加

```dockerfile
COPY --from=hengyunabc/arthas:latest /opt/arthas /opt/arthas
```

上面的意思是把hengyunabc/arthas:latest`这个镜像中的/opt/arthas目录拷贝到我们的镜像的/opt/arthas目录中。

然后我们需要在pod的交互模式下，再次启动arthas

```shell
kubectl exec -it ${pod} --container ${containerId} -- /bin/bash -c "java -jar /opt/arthas/arthas-boot.jar"
```

arthas启动成功后，会提示如下：

```shell
[INFO] Found existing java process, please choose one and input the serial number of the process, eg : 1. Then hit ENTER.
* [1]: 1 app.jar
```

命令交互，告诉我们arthas扫描到了哪些java进程，并阻塞等待用户输入要处理的进程，此时我们只要输入`1`即可进入arthas。



#### 2、观察一个方法的调用情况

在arthas的交互模式下使用`watch`命令，来观察一个方法的调用情况

> 具体可以参考官方文档：https://arthas.aliyun.com/doc/arthas-tutorials.html?language=cn&id=arthas-basics 找到`命令列表 > watch`



- watch命令使用范例

  ```shell
  watch 调用类的全路径 方法名称 观察表达式默认为{params, target, returnObj} 条件表达式 -b -e -s -f -x 2 -n 2
  ```

  参数说明：

  1. `-b` 方法被调用前监听点，配合观察表达式的`params`可以获取到方法的入参，默认是关闭的，需要加上`-b`才打开
  2. `-e` 方法执行过程中抛出异常监听点，默认关闭，需要加上`-e`才打开
  3. `-s`方法正常执行并返回监听点，默认关闭，需要加上`-s`才打开
  4. `-f`方法执行结束(正常返回和异常返回)监听点，默认打开，不需要加`-f`就能使用
  5. `-x 2` 指定`params、returnObj`结果的属性遍历深度， 默认值为1，最大值为4。
  6. `-n 2`指定观察点只会被执行2次



- 使用示例：

  - 准备一个类

    ```java
    package demo;
    
    import java.io.PrintStream;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Random;
    import java.util.concurrent.TimeUnit;
    
    public class MathGame {
        private static Random random = new Random();
        private int illegalArgumentCount = 0;
    
        public static void main(String[] args) throws InterruptedException {
            MathGame game = new MathGame();
            do {
                game.run();
                TimeUnit.SECONDS.sleep(1L);
            } while (true);
        }
    
        public void run() throws InterruptedException {
            try {
                int number = random.nextInt();
                List<Integer> primeFactors = this.primeFactors(number);
                MathGame.print(number, primeFactors);
            }
            catch (Exception e) {
                System.out.println(String.format("illegalArgumentCount:%3d, ", this.illegalArgumentCount) + e.getMessage());
            }
        }
    
        public static void print(int number, List<Integer> primeFactors) {
            StringBuffer sb = new StringBuffer("" + number + "=");
            Iterator<Integer> iterator = primeFactors.iterator();
            while (iterator.hasNext()) {
                int factor = iterator.next();
                sb.append(factor).append('*');
            }
            if (sb.charAt(sb.length() - 1) == '*') {
                sb.deleteCharAt(sb.length() - 1);
            }
            System.out.println(sb);
        }
    
        public List<Integer> primeFactors(int number) {
            if (number < 2) {
                ++this.illegalArgumentCount;
                throw new IllegalArgumentException("number is: " + number + ", need >= 2");
            }
            ArrayList<Integer> result = new ArrayList<Integer>();
            int i = 2;
            while (i <= number) {
                if (number % i == 0) {
                    result.add(i);
                    number /= i;
                    i = 2;
                    continue;
                }
                ++i;
            }
            return result;
        }
    }
    ```

  - 观察函数调用返回时的参数、MathGame对象和返回值

    > 观察表达式默认为{params, target, returnObj}

    ```shell
    $ watch demo.MathGame primeFactors -x 2
    Press Q or Ctrl+C to abort.
    Affect(class count: 1 , method count: 1) cost in 32 ms, listenerId: 5
    
    #第一次被执行
    method=demo.MathGame.primeFactors location=AtExceptionExit
    ts=2021-08-31 15:22:57; [cost=0.220625ms] result=@ArrayList[
        @Object[][
            @Integer[-179173],
        ],
        @MathGame[
            random=@Random[java.util.Random@31cefde0],
            illegalArgumentCount=@Integer[44],
        ],
        null,
    ]
    
    #第二次被执行
    method=demo.MathGame.primeFactors location=AtExit
    ts=2021-08-31 15:22:58; [cost=1.020982ms] result=@ArrayList[
        @Object[][
            @Integer[1],
        ],
        @MathGame[
            random=@Random[java.util.Random@31cefde0],
            illegalArgumentCount=@Integer[44],
        ],
        @ArrayList[
            @Integer[2],
            @Integer[2],
            @Integer[26947],
        ],
    ]
    
    ```

    上面的结果里，说明函数被执行了两次，第一次结果是location=AtExceptionExit，说明函数抛出异常了，因此returnObj是 null
    在第二次结果里是location=AtExit，说明函数正常返回，因此可以看到returnObj结果是一个 ArrayList

    

  - 观察函数调用入口的参数和返回值

    ```shell
    $ watch demo.MathGame primeFactors "{params,returnObj}" -x 2 -b
    Press Ctrl+C to abort.
    Affect(class-cnt:1 , method-cnt:1) cost in 50 ms.
    
    ts=2018-12-03 19:23:23; [cost=0.0353ms] result=@ArrayList[
        @Object[][
            @Integer[-1077465243],
        ],
        null,
    ]
    
    ```

    对比前一个例子，返回值为空（事件点为函数执行前，因此获取不到返回值）

    

  - 同时观察函数调用前和函数返回后

    ```shell
    $ watch demo.MathGame primeFactors "{params,target,returnObj}" -x 2 -b -s -n 2
    Press Ctrl+C to abort.
    Affect(class-cnt:1 , method-cnt:1) cost in 46 ms.
    
    
    ts=2018-12-03 19:29:54; [cost=0.01696ms] result=@ArrayList[
        @Object[][
            @Integer[1],
        ],
        @MathGame[
            random=@Random[java.util.Random@522b408a],
            illegalArgumentCount=@Integer[13038],
        ],
        null,
    ]
    
    
    
    ts=2018-12-03 19:29:54; [cost=4.277392ms] result=@ArrayList[
        @Object[][
            @Integer[1],
        ],
        @MathGame[
            random=@Random[java.util.Random@522b408a],
            illegalArgumentCount=@Integer[13038],
        ],
        @ArrayList[
            @Integer[2],
            @Integer[2],
            @Integer[2],
            @Integer[5],
            @Integer[5],
            @Integer[73],
            @Integer[241],
            @Integer[439],
        ],
    ]
    
    ```

    参数里-n 2，表示只执行两次。这里输出结果中，第一次输出的是函数调用前的观察表达式的结果，第二次输出的是函数返回后的表达式的结果。结果的输出顺序和事件发生的先后顺序一致，和命令中 -s -b 的顺序无关

    

  - 调整-x的值，观察具体的函数参数值

    ```shell
    $ watch demo.MathGame primeFactors "{params,target}" -x 3
    Press Ctrl+C to abort.
    Affect(class-cnt:1 , method-cnt:1) cost in 58 ms.
    
    
    ts=2018-12-03 19:34:19; [cost=0.587833ms] result=@ArrayList[
        @Object[][
            @Integer[1],
        ],
        @MathGame[
            random=@Random[
                serialVersionUID=@Long[3905348978240129619],
                seed=@AtomicLong[3133719055989],
                multiplier=@Long[25214903917],
                addend=@Long[11],
                mask=@Long[281474976710655],
                DOUBLE_UNIT=@Double[1.1102230246251565E-16],
                BadBound=@String[bound must be positive],
                BadRange=@String[bound must be greater than origin],
                BadSize=@String[size must be non-negative],
                seedUniquifier=@AtomicLong[-3282039941672302964],
                nextNextGaussian=@Double[0.0],
                haveNextNextGaussian=@Boolean[false],
                serialPersistentFields=@ObjectStreamField[][isEmpty=false;size=3],
                unsafe=@Unsafe[sun.misc.Unsafe@2eaa1027],
                seedOffset=@Long[24],
            ],
            illegalArgumentCount=@Integer[13159],
        ],
    ]
    
    ```

    `-x`表示遍历深度，可以调整来打印具体的参数和结果内容，默认值是 1。



