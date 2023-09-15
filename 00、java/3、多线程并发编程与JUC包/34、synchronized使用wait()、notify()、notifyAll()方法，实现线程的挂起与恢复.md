## synchronized使用wait()、notify()、notifyAll()方法，实现线程的挂起与恢复



#### 1、什么是挂起线程

线程的挂起实质上就是使线程进入“非可执行”状态，在这个状态下CPU不会分给线程时间片，进入这个状态可以用来暂停一个线程的运行。

在线程挂起后，可以通过重新唤醒线程来使之恢复运行。



#### 2、为什么要挂起线程

如果系统中的线程过多，会导致CPU的负载过高，如果有些线程可以暂停不执行，那么就挂起，珍惜CPU资源。



#### 3、如何挂起线程、唤醒线程

- 挂起线程：wait()，会先放弃本线程占用的锁，然后进入挂起状态。挂起状态直到被唤醒或被中断。<font color="red">只能由锁对象调用wait()方法。挂起的时候必须持有锁，才能释放锁。</font>
- 线程唤醒：<font color="red">唤醒线程的时候必须持有线程挂起的锁才能唤醒挂起的线程。</font>
  - notify()，随机唤醒一个挂起的线程，调用notify()方法后不会立即释放锁！
  - notifyAll()，唤醒所有挂起的线程，让它们自己去争抢锁，调用notifyAll()方法后不会立即释放锁！



#### 4、案例演示：

1. 准备线程操作资源类

   ```java
   public class WaitAndNotifyThreadResource {
   
       public Object obj = new Object();
   
       public void test() throws InterruptedException {
         	//线程挂起操作必须持有锁
           synchronized (obj){
               System.out.println(Thread.currentThread().getName() + "开始运行");
             	//挂起操作会释放锁
               obj.wait();
               System.out.println(Thread.currentThread().getName() + "开始完毕");
           }
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
   				//new出被操作的资源类
           WaitAndNotifyThreadResource waitAndNotifyThreadResource = new WaitAndNotifyThreadResource();
   
         	//启动第一个线程
           Thread t1 = new Thread(()->{
               try {
                   waitAndNotifyThreadResource.test();
   
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }, "t1");
           t1.start();
   
         	//启动第二个线程
           Thread t2 = new Thread(()->{
           try {
                   waitAndNotifyThreadResource.test();
   
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }, "t2");
           t2.start();
   
         	//主线程休眠一会
           Thread.sleep(1000L);
   
         	//唤醒操作
           synchronized (waitAndNotifyThreadResource.obj){
               System.out.println("尝试唤醒");
               waitAndNotifyThreadResource.obj.notifyAll();
           }
   
       }
   }
   ```

   

