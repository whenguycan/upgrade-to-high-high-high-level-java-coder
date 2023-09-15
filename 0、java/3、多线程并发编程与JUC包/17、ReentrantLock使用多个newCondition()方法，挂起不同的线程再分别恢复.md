## ReentrantLock使用多个newCondition()方法，挂起不同的线程，分别恢复



线程操作的资源类

```java
public class LockResouce {

    Lock lock = new ReentrantLock(false);
    Condition conditionA = lock.newCondition(); //newCondition创建一个condition
    Condition conditionB = lock.newCondition(); //newCondition创建一个condition

    public void get() throws InterruptedException {
        //上锁，并等待5秒
        if(lock.tryLock(1, TimeUnit.SECONDS)){

            System.out.println(Thread.currentThread().getName() + "---lock");

            if(Thread.currentThread().getName().equals("t1")){
                System.out.println(Thread.currentThread().getName()+"---await");
                //挂起线程
                conditionA.await();

            }else{
                System.out.println(Thread.currentThread().getName()+"---await");
                //挂起线程
                conditionB.await();
            }

            //释放锁
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "---unlock");

        }else{
            System.out.println("锁等待超时！");
        }

    }

}
```

多线程操作

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

  //先获取锁，才能唤醒
  Thread.sleep(3000L);
  userResource.lock.lock();
  userResource.conditionA.signal();
  userResource.lock.unlock();

  Thread.sleep(3000L);
  userResource.lock.lock();
  userResource.conditionB.signal();
  userResource.lock.unlock();
}
```



