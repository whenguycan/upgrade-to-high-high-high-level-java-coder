## CompletableFuture的常用方法

API非常非常多，我们这儿只是举例使用！



#### 1、supplyAsync()方法

> 异步执行任务，任务有返回值

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>()); //初始化一个线程池

//调用CompletableFuture.supplyAsync()方法
CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  return "这儿放的是一个Supplier接口的实现类"; //这儿必须return一个返回值
}, threadPoolExecutor); //threadPoolExecutor是上面初始化的线程池
System.out.println(taskA.get()); //获取到线程执行的返回！
```



#### 2、runAsync()方法

>  异步执行任务，任务没有返回值

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());//初始化一个线程池

//调用 CompletableFuture.runAsync()方法
CompletableFuture taskB = CompletableFuture.runAsync(()->{
  System.out.println("这儿放的是一个Runnable接口的实现类"); //线程执行没有返回值
}, threadPoolExecutor);//threadPoolExecutor是上面初始化的线程池
```



#### 3、thenApply()方法

> 前一个任务执行完，然后执行本同步任务，任务的执行有返回值
>
> 注意：
>
> 1. <font color="red">当前执行thenApply()方法的线程负责执行本任务，比如main线程，但是如果前一个异步任务还没执行完毕，那么main线程就不能执行本任务了，得等前一个任务执行完后才能执行本任务，这个时候就会在执行前一个任务的线程上执行本任务，这样才能保证顺序执行！</font>
> 2. thenApply()，将Function < T,R >作为参数，Function < T,R >是一个简单的函数式接口，表示一个接受 T 类型参数并产生一个 R 类型结果的函数
> 3. 前一个任务的返回值，会被thenApply()直接获取

案例1：

测试到底哪个线程来执行thenApply()

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture taskA = CompletableFuture.runAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
}, threadPoolExecutor);

Thread.sleep(2000L);//可以把这行去掉了和保留，看下thenApply线程都由哪个线程执行
CompletableFuture taskB = taskA.thenApply((param)->{
  return Thread.currentThread().getName() + "这儿放的是一个Function";
});

System.out.println(taskB.get());
```



案例2：

前一个任务的返回值，会被thenApply()直接拿到

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

CompletableFuture taskB = taskA.thenApply((param)->{
  System.out.println(param); //获取到taskA的执行结果
  return Thread.currentThread().getName() + "这儿放的是一个Function"; //thenApply()的执行必须有一个返回值
});

System.out.println(taskB.get());//获取到thenApply()的执行结果
```



#### 4、thenApplyAsync()方法

> 前一个任务执行完后，才能执行本异步任务，任务的执行有返回值
>
> 注意：
>
> 1. 内部使用的是ForkJoinPool线程池新开的线程来执行任务
> 2. thenApplyAsync()，将Function < T,R >作为参数，Function < T,R >是一个简单的函数式接口，表示一个接受 T 类型参数并产生一个 R 类型结果的函数
> 3. 前一个任务的返回值，会被thenApply()直接获取

例子：略



#### 5、thenAccept()方法

> 前一个任务执行完，然后执行本同步任务，任务的执行没有返回值
>
> 注意：
>
> 1. <font color="red">当前执行thenAccept()方法的线程负责执行本任务，比如main线程，但是如果前一个异步任务还没执行完毕，那么main线程就不能执行本任务了，得等前一个任务执行完后才能执行本任务，这个时候就会在执行前一个任务的线程上执行本任务，这样才能保证顺序执行！</font>
> 2. thenApply()，将Consumer<? super T>作为参数
> 3. 前一个任务的返回值，会被thenAccept()直接获取

案例1：

测试到底哪个线程执行thenAccept()

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

Thread.sleep(2000L);//可以把这行去掉了和保留，看下thenApply线程都由哪个线程执行
CompletableFuture taskB = taskA.thenAccept((param)->{
  System.out.println(param);
  System.out.println(Thread.currentThread().getName());
});
```



案例2：

前一个任务的返回值，会被thenAccept()直接拿到

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

CompletableFuture taskB = taskA.thenAccept((param)->{
  System.out.println(param); //拿到上一个线程的执行返回值
  System.out.println(Thread.currentThread().getName());
});
```



#### 6、thenAcceptAsync()方法

> 前一个任务执行完后，才能执行本异步任务，任务的执行没有返回值
>
> 注意：
>
> 1. 内部使用的是ForkJoinPool线程池新开的线程来执行任务
> 2. thenAcceptAsync()，将Consumer<? super T>作为参数
> 3. 前一个任务的返回值，会被thenApply()直接获取

例子：略



#### 7、thenRun()方法

> 前一个任务执行完，然后执行本同步任务，任务的执行没有返回值
>
> 注意：
>
> 1. <font color="red">当前执行thenAccept()方法的线程负责执行本任务，比如main线程，但是如果前一个异步任务还没执行完毕，那么main线程就不能执行本任务了，得等前一个任务执行完后才能执行本任务，这个时候就会在执行前一个任务的线程上执行本任务，这样才能保证顺序执行！</font>
> 2. thenRun()，将Runnable作为参数，所以，thenRun()不能获取上一个任务的返回值，自身也没有返回值

案例：

测试到底哪个线程执行thenRun()

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

Thread.sleep(2000L);//可以把这行去掉了和保留，看下thenApply线程都由哪个线程执行
CompletableFuture taskB = taskA.thenRun(()->{
  System.out.println(Thread.currentThread().getName());
});
```



#### 8、thenRunAsync()方法

> 前一个任务执行完后，才能执行本异步任务，任务的执行没有返回值
>
> 注意：
>
> 1. 内部使用的是ForkJoinPool线程池新开的线程来执行任务
> 2. thenRunAsync()，将Runnable作为参数，所以，thenRun()不能获取上一个任务的返回值，自身也没有返回值

例子：略



#### 9、thenCompose()方法

> 前一个任务执行完，然后执行本同步任务，任务的执行有返回值
>
> 注意：
>
> 1. <font color="red">当前执行thenAccept()方法的线程负责执行本任务，比如main线程，但是如果前一个异步任务还没执行完毕，那么main线程就不能执行本任务了，得等前一个任务执行完后才能执行本任务，这个时候就会在执行前一个任务的线程上执行本任务，这样才能保证顺序执行！</font>
> 2. thenCompose()，将Function<? super T, ? extends CompletionStage<U>>做为参数，返回一个CompletionStage接口的实现类即CompletableFuture
> 3. 前一个任务的返回值，会被thenCompose()直接获取

案例1:

测试到底哪个线程执行thenCompose()

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

Thread.sleep(2000l);//可以把这行去掉了和保留，看下thenApply线程都由哪个线程执行
CompletableFuture taskB = taskA.thenCompose((param)->{
  System.out.println(Thread.currentThread().getName());
  return CompletableFuture.runAsync(()->{ //thenCompose()必须返回一个CompletionStage接口的实现类CompletableFuture
    System.out.println(Thread.currentThread().getName());
  }, threadPoolExecutor);
});
```



案例2：

前一个任务的返回值，会被thenCompose()直接拿到

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "tangwei";
}, threadPoolExecutor);

CompletableFuture taskB = taskA.thenCompose((param)->{
  System.out.println(param); //拿到前一个任务的返回值
  System.out.println(Thread.currentThread().getName());


  return CompletableFuture.runAsync(()->{//thenCompose()必须返回一个CompletionStage接口的实现类CompletableFuture
    System.out.println(Thread.currentThread().getName());
  }, threadPoolExecutor);
});
```



#### 10、thenComposeAsync()方法

自行了解



#### 11、thenCombine()方法

> 将2个CompletableFuture任务进行绑定整合，只有当2个都正常执行完了才会执行某个任务。
>
> 有2个参数
>
> - 第一个参数是一个CompletableFuture任务，被绑定的任务
> - 第二个参数是BiFunction接口的实现类，BiFunction需要提供2个参数，这2个参数正好来自2个CompletableFuture任务的返回值！且BiFunction的实现类需要有返回值！
>
> thenCombine()方法最终会返回一个CompletableFuture。

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskA";
}, threadPoolExecutor);

CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskB";
}, threadPoolExecutor);


CompletableFuture taskC = taskA.thenCombine(taskB, (resyltA, resultB)->{//将taskA、taskB通过thenCombine进行绑定，resultA、resultB就是taskA、taskB的执行结果

  System.out.println(resultB);
  System.out.println(resyltA);

  return "xxxxx";
});
```



#### 12、runAfterEither() 

> 将2个CompletableFuture任务进行绑定整合，两个任务完成其中一个，就会执行某个任务。
>
> 有2个参数：
>
> - 第一个参数是一个CompletableFuture任务，被绑定的任务
> - 第二个参数是一个Runnable的接口实现类，没有返回值
>
> runAfterEither()方法最终会返回一个CompletableFuture。

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskA";
}, threadPoolExecutor);

CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskB";
}, threadPoolExecutor);


CompletableFuture taskC = taskA.runAfterEither(taskB, ()->{//将taskA、taskB通过runAfterEither进行绑定，
  //taskA、taskB只要有一个任务执行完毕，就会执行这儿
  System.out.println("xxxxx");
});
```



#### 13、runAfterBoth()方法

> 将2个CompletableFuture任务进行绑定整合，两个任务都执行完，就会执行某个任务。
>
> 有2个参数：
>
> - 第一个参数是一个CompletableFuture任务，被绑定的任务
> - 第二个参数是一个Runnable的接口实现类，没有返回值
>
> runAfterBoth()方法最终会返回一个CompletableFuture。

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskA";
}, threadPoolExecutor);

CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskB";
}, threadPoolExecutor);


CompletableFuture taskC = taskA.runAfterBoth(taskB, ()->{
  System.out.println("xxxxx");
});
```



#### 14、allOf()方法

> 将2个及2个以上的任务组合起来，只有所有的任务执行完毕了，才会继续执行
>
> 参数是，所有要进行组合的CompletableFuture对象。
>
> 返回CompletableFuture<Void>对象

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskA";
}, threadPoolExecutor);

CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->{

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskB";
}, threadPoolExecutor);


CompletableFuture.allOf(taskA, taskB).join(); //必须要写join()在这儿等待！
System.out.println("yyyyy");
```



#### 15、anyOf()方法

> 将2个及2个以上的任务组合起来，只要一个任务执行完毕了，就会继续执行
>
> 参数是，所有要进行组合的CompletableFuture对象。
>
> 返回CompletableFuture<Object>对象

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);

CompletableFuture<String> taskB = CompletableFuture.supplyAsync(()->{

  try {
    Thread.sleep(3000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");
  return "TaskB";
}, threadPoolExecutor);


CompletableFuture.anyOf(taskA, taskB).join(); //必须要写join()在这儿等待！
System.out.println("yyyyy");
```



#### 16、getNow()方法

> 获取一个CompletableFuture的执行结果，如果该CompletableFuture已经执行完了，则可以拿到结果。如果CompletableFuture还没有执行完，就拿到getNow()内部设置的默认值。

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);
System.out.println(taskA.getNow("hhh"));
```



#### 17、get()方法

> 阻塞等待获取一个CompletableFuture的执行结果，会一直等待到CompletableFuture执行完成。



#### 18、get(long timeout, TimeUtil unit)方法

> 获取一个CompletableFuture的执行结果，不过不会一直阻塞等待，会阻塞一段时间



#### 19、whenComplete()方法

> 调用whenComplete()方法的任务执行完成（抛出异常也算执行完成），就会执行whenComplete()方法，whenComplete()方法需要有1个参数，是BiConsumer接口的实现类，BiConsumer接口的实现类要有2个参数，第一个参数是调用whenComplete()方法的任务执行成功的返回值，第二个参数是调用whenComplete()方法的任务执行失败的异常

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  int a = 1/0;

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);
taskA.whenComplete((result, error)->{
  System.out.println(result);

  if (error != null){
    error.printStackTrace();
  }
});
```





#### 20、exceptionally()方法

> 调用exceptionally()方法的任务执行抛出异常，就会执行exceptionally()方法，exceptionally()方法需要有1个参数，是Function接口的实现类，Function接口的实现类要有1个参数是调用exceptionally()方法的任务抛出的异常！Function接口的实现类必须有返回值！
>
> exceptionally()方法会返回CompletableFuture对象

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  int a = 1/0;

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);
taskA.exceptionally((error)->{

  if (error != null){
    error.printStackTrace();
  }

  return "xxx";
});
```



#### 21、complete()方法

> 强制让一个任务执行完，即便是这个任务还没有执行完，如果遇到了这行代码也会强制执行完！并返回一个数据！

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  int a = 1/0;

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);
taskA.complete("hhh");

System.out.println(taskA.get());
```



#### 22、cancel()方法

> 强制取消一个任务，即便是这个任务还没有执行完，如果遇到了这行代码也会强制取消！

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

CompletableFuture<String> taskA = CompletableFuture.supplyAsync(()->{
  try {
    Thread.sleep(2000L);
  } catch (InterruptedException e) {
    throw new RuntimeException(e);
  }

  int a = 1/0;

  System.out.println(Thread.currentThread().getName() + "这儿放的是一个Runnable接口的实现类");

  return "TaskA";
}, threadPoolExecutor);

taskA.cancel(false);
System.out.println(taskA.get()); //这儿获取一个已经取消的任务的结果，势必是拿不到的，所以会抛出异常来
```

