## wait()方法挂起线程的条件发生了变化



#### 复现线程挂起条件发生了变化，会出现什么问题

```java
public static void main(String[] args) throws InterruptedException {

  //new出被操作的资源类
  Object obj = new Object();

  List bucket = new ArrayList<>();



  //启动第一个线程
  Thread t1 = new Thread(()->{
    try {
      synchronized (obj){
        if (bucket.size() == 0){
          System.out.println("当前线程：" + Thread.currentThread().getName() + "获取到list中的数据量为：" + bucket.size() + "，所以进入挂起状态");
          obj.wait();
        }
        bucket.remove(0);
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出挂起状态");

      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t1");

  Thread t2 = new Thread(()->{
    try {
      synchronized (obj){
        if (bucket.size() == 0){
          System.out.println("当前线程：" + Thread.currentThread().getName() + "获取到list中的数据量为：" + bucket.size() + "，所以进入挂起状态");
          obj.wait();
        }
        bucket.remove(0);
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出挂起状态");

      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t2");


  //启动第二个线程
  Thread t3 = new Thread(()->{
    synchronized (obj){
      bucket.add(0);
      obj.notifyAll();
    }
  }, "t3");

  t1.start();
  t2.start();
  Thread.sleep(3000L);
  t3.start();
}
```

当线程t1、t2启动后，都进入了挂起状态，3秒后，t3线程启动，t3线程启动后理论上会获取到锁然后让bucket中放了一个数据，然后t3线程唤醒t1、t2线程。t1、t2线程，被唤醒后从唤醒的地方开始往下执行，t1或者t2线程中先执行的线程会调用bucket.remove(0)方法，删掉bucket里面的数据，那么后执行的线程（t1、t2不好确定），也去调用bucket.remove(0)方法就会报错！



#### 解决线程挂起条件发生了变化的判断问题

就是被唤醒后再次判断下，挂起条件，如果符合挂起条件，就再次挂起。

```java
public static void main(String[] args) throws InterruptedException {

  //new出被操作的资源类
  Object obj = new Object();

  List bucket = new ArrayList<>();



  //启动第一个线程
  Thread t1 = new Thread(()->{
    try {
      synchronized (obj){
        while (bucket.size() == 0){//被唤醒后，再次判断下挂起条件
          System.out.println("当前线程：" + Thread.currentThread().getName() + "获取到list中的数据量为：" + bucket.size() + "，所以进入挂起状态");
          obj.wait();
        }
        bucket.remove(0);
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出挂起状态");

      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t1");

  Thread t2 = new Thread(()->{
    try {
      synchronized (obj){
        while (bucket.size() == 0){//被唤醒后，再次判断下挂起条件
          System.out.println("当前线程：" + Thread.currentThread().getName() + "获取到list中的数据量为：" + bucket.size() + "，所以进入挂起状态");
          obj.wait();
        }
        bucket.remove(0);
        System.out.println("当前线程：" + Thread.currentThread().getName() + "退出挂起状态");

      }

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }, "t2");


  //启动第二个线程
  Thread t3 = new Thread(()->{
    synchronized (obj){
      bucket.add(0);
      obj.notifyAll();
    }
  }, "t3");

  t1.start();
  t2.start();
  Thread.sleep(3000L);
  t3.start();
}
```

