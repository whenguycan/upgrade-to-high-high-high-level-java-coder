## 线程池execute和submit两种提交任务方式的区别



execute()提交的任务：

- 接收的是Runnable接口的实现类时，没有返回值，且有异常直接抛出。
- 不能直接接收Callable接口的实现类。
- 接收被FutureTask包装后的Callable，没有返回值，且有异常不抛出，因为Callable中的返回值被FutureTask拿走了，所以只有future.get()的时候才会抛出异常和拿到返回值。



submit()提交的任务：

- 接收的是Runnable接口的实现类时，没有返回值，有异常了不会抛出。
- 也可以提交Callable接口的实现类，返回值为Future类型，有异常了不会抛出，因为Callable中的返回值被Future拿走了，所以只有future.get()的时候才会抛出异常和拿到返回值。
- 接收被FutureTask包装后的Callable，没有返回值，且有异常不抛出，因为Callable中的返回值被FutureTask拿走了，所以只有future.get()的时候才会抛出异常和拿到返回值。





以异常处理方式为例，看看execute()和submit()的区别

```java
public class SynchTest01 {

    static class MyTask implements Runnable{

        private int x;

        private int y;

        public MyTask(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + "计算：" + x + "/" + y +"得到：" + (x/y));
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5,
                10,
                3
                , TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),

                Executors.defaultThreadFactory(),

        new ThreadPoolExecutor.DiscardOldestPolicy()
                );

        for (int i=0; i< 5; i++){
            threadPoolExecutor.execute(new MyTask(10, i)); //这儿切换成submit()方法提交任务，看看是否会抛出异常
        }

    }
```

