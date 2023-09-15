## ThreadPoolExecutor创建线程池



使用ThreadPoolExecutor创建一个线程池

```java
public static void main(String[] args) throws InterruptedException {

  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
    5,
    10,
    3
    , TimeUnit.SECONDS,
    new LinkedBlockingDeque<>(),
    Executors.defaultThreadFactory(),
    new ThreadPoolExecutor.CallerRunsPolicy()
  );
  for (int i=0; i< 18; i++){
    threadPoolExecutor.execute(()->{
      System.out.println(Thread.currentThread().getId() + "编号的任务正在执行：开始时间为：" + System.currentTimeMillis() );
      try {
        Thread.sleep(3000L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
```

运行的方式为：

```java
19编号的任务正在执行：开始时间为：1691646060019
17编号的任务正在执行：开始时间为：1691646060019
18编号的任务正在执行：开始时间为：1691646060019
16编号的任务正在执行：开始时间为：1691646060019
15编号的任务正在执行：开始时间为：1691646060018
休息3秒钟
  
18编号的任务正在执行：开始时间为：1691646076897
19编号的任务正在执行：开始时间为：1691646076897
16编号的任务正在执行：开始时间为：1691646076897
17编号的任务正在执行：开始时间为：1691646076897
15编号的任务正在执行：开始时间为：1691646076897
休息3秒钟

19编号的任务正在执行：开始时间为：1691646095125
18编号的任务正在执行：开始时间为：1691646095125
16编号的任务正在执行：开始时间为：1691646095125
17编号的任务正在执行：开始时间为：1691646095125
15编号的任务正在执行：开始时间为：1691646095125
休息3秒钟
  
19编号的任务正在执行：开始时间为：1691646098129
18编号的任务正在执行：开始时间为：1691646098129
16编号的任务正在执行：开始时间为：1691646098129
休息3秒钟
```

为什么是这样的运行方式呢？我们后面再分析！