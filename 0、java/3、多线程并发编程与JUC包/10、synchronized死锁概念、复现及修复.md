## synchronized死锁概念、复现及修复



#### 1、死锁概念

A线程和B线程，A在等待B的所释放、B也在等待A的锁释放，两个线程在死等，就形成了死锁。



#### 2、代码示例

线程操作资源类

```java
public class LockResouce {

    private final static Object lock1 = new Object();
    private final static Object lock2 = new Object();

    public void mm(){
        synchronized (lock1){
            System.out.println(Thread.currentThread().getName() + "get lock1");
            synchronized (lock2){
                System.out.println(Thread.currentThread().getName() + "get lock2");
            }
        }
    }

    public void mm2(){
        synchronized (lock2){
            System.out.println(Thread.currentThread().getName() + "get lock2");
            synchronized (lock1){
                System.out.println(Thread.currentThread().getName() + "get lock1");
            }
        }

    }

}
```

开启2个线程，去操作资源类

```java
public static void main(String[] args) {

  LockResouce res = new LockResouce();
  new Thread(()->{
    res.mm();
  }).start();

  new Thread(()->{
    res.mm2();
  }).start();
}
```

出现，如下的情况

```java
Thread-0get lock1
Thread-1get lock2
```

然后2个线程一直在运行，不会退出！



#### 3、死锁解决

调整2个线程获取锁的顺序。



