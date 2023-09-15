## JDK中关于线程池的API



API图解：

![avatar](../images/WechatIMG683.png)



1. Executor 是一个接口，内部只提供execute(Runnable command) 方法且没有方法体，可以提交一个Runnable任务

2. ExecutorService是一个接口，内部提供shutdown()、submit(Callable task)、submit(Runnable task)方法且都没有方法体

3. ThreadPoolExecutor类，<font color="red">我们一般使用最多的就是这个！</font>

4. Executors是一个工具类，提供一些快捷创建线程池的方法 （<font color="red">我们一般不用这个</font>）

   ```java
   ExecutorService threadPool = Executors.newFixedThreadPool(5);	//固定容量
   ExecutorService threadPool = Executors.newSingleThreadExecutor(); 	//单例的、单个线程的线程池
   ExecutorService threadPool = Executors.newCachedThreadPool(); 	//缓存的 即超出就自动创建线程的
   ```

   
