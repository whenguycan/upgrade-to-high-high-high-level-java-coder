## ReentrantLock的tryLock方法的使用



#### 1、tryLock()方法的用法

- tryLock(long time, TimeUtil unit)，设置等待锁的时间，如果等待超时了，会自动放弃等待锁，避免产生死锁！<font color="red">只有线程还在等待时间范围内，且线程没有被中断，才能获得锁！</font>
- tryLock()，尝试获取锁，如果锁被其它线程占用，则直接放弃



#### 2、为什么要使用tryLock()方法

避免死锁、避免线程中断了还能获取锁

例子：

- 例子1：锁等待超时

  准备线程操作的资源类

  ```java
  public class LockResouce {
  
      Lock lock = null;
  
      public LockResouce(){
          //初始化ReentrantLock
          lock = new ReentrantLock(false);
      }
  
      public void get() throws InterruptedException {
          //上锁，并等待5秒
          if(lock.tryLock(5, TimeUnit.SECONDS)){
  
              System.out.println(Thread.currentThread().getName() + "---lock");
              Thread.sleep(10000L); //手动休眠10S
              //释放锁
              lock.unlock();
              System.out.println(Thread.currentThread().getName() + "---unlock");
  
          }else{
              System.out.println("锁等待超时！");
          }
  
      }
  
  }
  ```

  准备多线程类

  ```java
  public static void main(String[] args) throws InterruptedException {
    LockResouce userResource = new LockResouce();
  
    //线程1 去操作资源类
    Thread t1 = new Thread(()->{
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t1");
    t1.start();
  
  
    Thread t2 = new Thread(()->{ //t2线程会等待超时
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t2");
    t2.start();
  }
  ```

  

- 例子2：线程中断tryLock(long time, TimeUnit unit)方法抛出线程中断异常

  准备线程操作的资源类

  ```java
  public class LockResouce {
  
      Lock lock = null;
  
      public LockResouce(){
          //初始化ReentrantLock
          lock = new ReentrantLock(false);
      }
  
      public void get() throws InterruptedException {
          //上锁，并等待5秒，如果线程在等待锁的时候中断了，那么会抛出异常
          if(lock.tryLock(5, TimeUnit.SECONDS)){
  
              System.out.println(Thread.currentThread().getName() + "---lock");
              Thread.sleep(10000L); //手动休眠10S
              //释放锁
              lock.unlock();
              System.out.println(Thread.currentThread().getName() + "---unlock");
  
          }else{
              System.out.println("锁等待超时！");
          }
  
      }
  
  
  
  }
  ```

  准备多线程类

  ```java
  public static void main(String[] args) throws InterruptedException {
    LockResouce userResource = new LockResouce();
  
    //线程1 去操作资源类
    Thread t1 = new Thread(()->{
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t1");
    t1.start();
  
  
    Thread t2 = new Thread(()->{
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t2");
    t2.start();
    Thread.sleep(50L);
    t2.interrupt();
  }
  ```

  

- 例子3： tryLock()尝试获取锁，获取不到直接放弃

  准备线程操作的资源类

  ```java
  public class LockResouce {
  
      Lock lock = null;
  
      public LockResouce(){
          //初始化ReentrantLock
          lock = new ReentrantLock(false);
      }
  
      public void get() throws InterruptedException {
          //获取锁，获取不到直接放弃
          if(lock.tryLock()){
  
              System.out.println(Thread.currentThread().getName() + "---lock");
              Thread.sleep(10000L); //手动休眠10S
              //释放锁
              lock.unlock();
              System.out.println(Thread.currentThread().getName() + "---unlock");
  
          }else{
              System.out.println("锁等待超时！");
          }
  
      }
  
  
  
  }
  ```

  准备多线程类

  ```java
  public static void main(String[] args) throws InterruptedException {
    LockResouce userResource = new LockResouce();
  
    //线程1 去操作资源类
    Thread t1 = new Thread(()->{
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t1");
    t1.start();
  
  
    Thread t2 = new Thread(()->{
      try {
        userResource.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }, "t2");
    t2.start();
  }
  ```

  