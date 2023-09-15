## 通知过早（A线程尚未调用wait()进入挂起状态、B线程已经调用了notify()唤醒A线程）



#### 通知过早会有什么问题

A线程尚未挂起，B线程就尝试唤醒，然后A线程再挂起，会导致B线程一直处于挂起状态，永远没有线程再去唤醒B线程。



#### 通知过早问题复现

```java
public static void main(String[] args) throws InterruptedException {

  //new出被操作的资源类
  Object obj = new Object();

  //启动第一个线程
  Thread t1 = new Thread(()->{
    try {
      synchronized (obj){
        System.out.println(Thread.currentThread().getName() + "开始运行");
        //挂起操作会释放锁
        obj.wait();
        System.out.println(Thread.currentThread().getName() + "开始完毕");
      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t1");


  //启动第二个线程
  Thread t2 = new Thread(()->{
    synchronized (obj){
      System.out.println("t2 start notify");
      obj.notify();
      System.out.println("t2 end notify");
    }
  }, "t2");
  
  //注意这儿是t2先运行，再运行t1
  t2.start();
  t1.start();
}
```



#### 通知过早问题避免

如果t1线程一定要先运行，如果不是t1线程先运行，则t1线程不调用wait()进入挂起状态

```java
static boolean isFirst = true; //这儿加不加volatile？

public static void main(String[] args) throws InterruptedException {

  //new出被操作的资源类
  Object obj = new Object();



  //启动第一个线程
  Thread t1 = new Thread(()->{
    try {
      synchronized (obj){
        System.out.println(Thread.currentThread().getName() + "开始运行");
        //挂起操作会释放锁
        while(isFirst){ //如果不是第一个运行则不要进入挂起状态
          obj.wait();
        }
        System.out.println(Thread.currentThread().getName() + "开始完毕");
      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t1");


  //启动第二个线程
  Thread t2 = new Thread(()->{
    synchronized (obj){
      isFirst = false;
      System.out.println("t2 start notify");
      obj.notify();
      System.out.println("t2 end notify");
    }
  }, "t2");

  t1.start();
  t2.start();
}
```

