## java中Callable与Runnable的区别



#### Runnable

- runnable没有返回值，可以直接放入线程中运行

```java
new Thread(()->{
  System.out.println("xxxx");
}).start();
```

- runnable没有返回值，可以直接放入线程池中运行，不管是submit还是execute方式。

```java
ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingDeque());
threadPoolExecutor.submit(()->{
  System.out.println("xxxxx");
});
```



#### Callable

- 有返回值，放入线程需要使用FutureTask包装

```java
class MyCallable implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        return 10;
    }
}
FutureTask<Integer> integerFutureTask = new FutureTask<>(new MyCallable());

new Thread(integerFutureTask).start();

System.out.println(integerFutureTask.get());
```

- 有返回值，放入线程池可以使用FutureTask包装也可以不使用FutureTask包装

  - 不使用FutureTask包装，只能使用submit的方式

  ```java
  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingDeque());
  Future<Integer> submit = threadPoolExecutor.submit(new Callable<Integer>() {
    @Override
    public Integer call() throws Exception {
      int m = 1/0;
      return 5;
    }
  });
  submit.get()
  ```

  - 使用FutureTask包装，submit的方式、execute方式都行。

  ```java
  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingDeque());
  
  class MyCallable implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
      return 10;
    }
  }
  FutureTask<Integer> integerFutureTask = new FutureTask<>(new MyCallable());
  
  threadPoolExecutor.execute(integerFutureTask);
  ```

  

