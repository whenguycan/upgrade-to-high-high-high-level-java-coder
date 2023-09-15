## 多线程start后执行是无序的

因为线程在start后，各个线程之间没有执行优先级，谁先执行谁后执行全凭cpu调度，所以多线程执行是无序的，每次的执行顺序都不一样

```java
public class MyT1 {
    public static void main(String[] args) {

        for (int i = 0; i <= 20; i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "通过实现Runnable接口的方式创建线程");
            }, "T" + i).start();
        }

    }
}
```

正式因为线程的执行是无序的，所以我们后续才会学习各种锁（ReentrantLoad、Synchronized、CountdownLatch）、线程数据隔离ThreadLocal、Cas、Jdk内置队列（LinkedBlockingQueue、ArrayBlockingQueue等）

