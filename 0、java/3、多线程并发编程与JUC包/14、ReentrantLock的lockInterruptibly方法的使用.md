## ReentrantLock的lockInterruptibly方法的使用



#### 1、lockInterruptibly()方法的作用

如果当前线程未被中断则获得锁，如果当前线程被中断则抛出异常。



#### 2、为什么要使用lockInterruptibly()方法

我们使用lock()方法去获取锁，如果我们在线程启动后，手动调用interrupt()方法去中断线程，lock()是不会受影响的，而使用lockInterruptly()方法会捕获线程的中断信号，随即抛出线程中断异常！从而停止获取锁！

例子：

- 例子1：使用lock()方法，不使用lockInterruptibly()方法

  准备线程操作的资源类

  ```java
  public class LockResouce {
  
      Lock lock = null;
  
      public LockResouce(){
          //初始化ReentrantLock
          lock = new ReentrantLock(false);
      }
  
      public void get() throws InterruptedException {
          //上锁
          lock.lock();
  
          System.out.println(Thread.currentThread().getName() + "---lock");
  
  
          for (int i = 0; i < Integer.MAX_VALUE; i++){
              new StringBuilder();
          }
          //释放锁
          lock.unlock();
  
          System.out.println(Thread.currentThread().getName() + "---unlock");
  
      }
  
  
  
  }
  ```

  准备多线程操作资源类

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
    t2.interrupt(); //即便这儿中断了t2线程，但是t2线程还是能照常获取到锁，并执行完毕
  }
  ```



- 例子2：使用lockInterruptibly()方法获取锁

  准备线程操作的资源类

  ```java
  public class LockResouce {
  
      Lock lock = null;
  
      public LockResouce(){
          //初始化ReentrantLock
          lock = new ReentrantLock(false);
      }
  
      public void get() throws InterruptedException {
          //上锁
          lock.lockInterruptibly();
  
          System.out.println(Thread.currentThread().getName() + "---lock");
  
  
          for (int i = 0; i < Integer.MAX_VALUE; i++){
              new StringBuilder();
          }
          //释放锁
          lock.unlock();
  
          System.out.println(Thread.currentThread().getName() + "---unlock");
  
      }
  
  
  
  }
  ```

  准备多线程操作资源类

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
    t2.interrupt(); //这儿中断了t2线程，随后抛出了线程中断的异常
  }
  ```

  





