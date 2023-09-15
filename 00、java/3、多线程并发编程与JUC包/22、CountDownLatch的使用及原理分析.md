## CountDownLatch的使用及原理分析

> 给CountDownLatch一个初始值，每次调用countDown方法都会对初始值进行-1操作，当初始值到0之前，await方法都会阻塞等待，到0之后await方法才会放行



#### 代码使用示例

```java
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i=0; i<=10; i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + " 调用");
                countDownLatch.countDown();
            }, "t" + String.valueOf(i)).start();
        }

        countDownLatch.await();

        System.out.println("主线程执行！");

        System.out.println(countDownLatch.getCount());

    }
```



#### 原理分析

跟ReentrantLock一样，底层是基于AQS实现的！