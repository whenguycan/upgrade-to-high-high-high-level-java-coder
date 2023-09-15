## ReentrantLock的常用方法



1. getHoldCount() 可以获取当前线程获取到锁的次数（调用lock()、tryLock()等都算），在重入性下也会+1次数

   ```java
   ReentrantLock lock = new ReentrantLock();
   System.out.println(Thread.currentThread().getName() + "获取锁的次数为：" + lock.getHoldCount());
   ```

   

2. getQueueLength() 获取估算的在阻塞等待获取锁的线程数，是估算的不准确

   ```java
   ReentrantLock lock = new ReentrantLock();
   System.out.println(lock.getQueueLength());
   ```

   

3. getWaitQueueLength(Condition condition) 获取在指定condition中挂起的线程数，是估算的不准确，<font color="red">该操作需要先获取锁</font>

   ```java
   ReentrantLock lock = new ReentrantLock();
   Condition conditionB = lock.newCondition();
   
   lock.lock();//tryLock()也可以
   System.out.println(lock.getWaitQueueLength(conditionB));
   lock.unlock();
   ```

   

4. hasQueuedThread(Thread thread) 查看指定的线程是不是在等待获取锁

   ```java
   public static void main(String[] args) throws InterruptedException {
   
     ReentrantLock lock = new ReentrantLock();
   
     Thread t1 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           Thread.sleep(4000L);
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t1");
     t1.start();
   
   
     Thread t2 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           Thread.sleep(2000L);
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t2");
     t2.start();
   
     Thread.sleep(3000L);
     System.out.println(lock.hasQueuedThread(t2)); //判断t1线程是不是在等待获取锁
   }
   ```

   

5. hasQueuedThreads() 查看是否还有线程在等待获得锁

   ```java
   public static void main(String[] args) throws InterruptedException {
   
     ReentrantLock lock = new ReentrantLock();
   
     Thread t1 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           Thread.sleep(4000L);
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t1");
     t1.start();
   
   
     Thread t2 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           Thread.sleep(2000L);
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t2");
     t2.start();
   
     Thread.sleep(3000L);
     System.out.println(lock.hasQueuedThreads()); //判断是否还有线程在等待获得锁
   }
   ```

   

6. hasWaiters(Condition condition) 查询是否有线程在指定的Condition挂起

   ```java
   public static void main(String[] args) throws InterruptedException {
   
     ReentrantLock lock = new ReentrantLock();
     Condition condition = lock.newCondition();
   
     Thread t1 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           condition.await();
   
           Thread.sleep(4000L);
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t1");
     t1.start();
   
   
     Thread t2 = new Thread(()->{
       try {
         if(lock.tryLock(5, TimeUnit.SECONDS)){
   
           System.out.println(Thread.currentThread().getName() + "---get lock");
   
           Thread.sleep(2000L);
   
           condition.await();
   
         }
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
     }, "t2");
     t2.start();
   
     Thread.sleep(3000L);
     lock.lock();
     System.out.println(lock.hasWaiters(condition)); //判断是否有线程在condition中挂起着
     lock.unlock();
   }
   ```

   

7. isFair()方法，判断当前的lock是不是公平锁

8. isHeldByCurrentThread()方法，判断当前线程是否持有锁

9. isLocked()方法，判断锁是否被某个线程持有

   

   

