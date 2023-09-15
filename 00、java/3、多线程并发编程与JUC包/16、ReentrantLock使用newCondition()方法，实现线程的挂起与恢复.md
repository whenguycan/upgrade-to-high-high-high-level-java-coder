## 线程的挂起与恢复



#### 1、什么是挂起线程

线程的挂起实质上就是使线程进入“非可执行”状态，在这个状态下CPU不会分给线程时间片，进入这个状态可以用来暂停一个线程的运行。

在线程挂起后，可以通过重新唤醒线程来使之恢复运行。



#### 2、为什么要挂起线程

如果系统中的线程过多，会导致CPU的负载过高，如果有些线程可以暂停不执行，那么就挂起，珍惜CPU资源。



#### 3、如何挂起线程、唤醒线程

- 挂起线程：await()，会先放弃本线程占用的锁，然后进入挂起状态。<font color="red">挂起的时候必须持有锁，才能释放锁。</font>
- 线程唤醒：<font color="red">唤醒线程的时候必须持有线程挂起的锁才能唤醒挂起的线程。</font>
  - signal()，随机唤醒一个挂起的线程，去获取锁
  - signalAll()，唤醒所有挂起的线程，让它们自己去争抢锁



#### 4、案例演示：

1. 准备线程操作资源类

   ```java
   
   public class WaitAndNotifyThreadResource {
   
       public Object obj = new Object();
   
       public ReentrantLock reentrantLock = new ReentrantLock(false);
   
       public Condition condition = reentrantLock.newCondition();//会形成一个队列
   
       public void test() throws InterruptedException {
   				
         	//必须持有锁，才能在挂起的时候释放锁
           reentrantLock.lock();
           System.out.println(Thread.currentThread().getName() + "开始运行");
         	//线程挂起
           condition.await(); //await挂起的线程都会进入上面的队列
           System.out.println(Thread.currentThread().getName() + "开始完毕");
           reentrantLock.unlock();
   
       }
   
   }
   
   ```

2. 准备多线程

   ```java
   package com.example.demolog.thread;
   
   import com.example.demolog.resource.WaitAndNotifyThreadResource;
   import lombok.extern.slf4j.Slf4j;
   
   @Slf4j
   public class MyThreadRun {
   
       public static void main(String[] args) throws InterruptedException {
   
           WaitAndNotifyThreadResource waitAndNotifyThreadResource = new WaitAndNotifyThreadResource();
   
           Thread t1 = new Thread(()->{
               try {
                   waitAndNotifyThreadResource.test();
   
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }, "t1");
           t1.start();
   
           Thread t2 = new Thread(()->{
           try {
                   waitAndNotifyThreadResource.test();
   
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }, "t2");
           t2.start();
   
           Thread.sleep(1000L);//主线程休眠一会
   
         	//必须持有锁，才能唤醒线程
           waitAndNotifyThreadResource.reentrantLock.lock();
           System.out.println("尝试唤醒");
         	//使用signalAll唤醒所有线程
           waitAndNotifyThreadResource.condition.signalAll();
   
           waitAndNotifyThreadResource.reentrantLock.unlock();
   
       }
   
   }
   
   ```

   

