## ReentrantReadWriteLock的使用

> 当一个线程获取到读锁，其它线程也可以获取到读锁，所有的线程包括自己都获取不到写锁！
>
> 当一个线程获取到写锁，只有自己能再去获取读锁、写锁（即读写只能是自己），其它线程获取写锁、读锁都需要等待，写的时候的功能其实就是ReentrantLock。

具体原理，需要去看AQS的部分！



#### 1、演示一个线程获取到写锁后，只有自己能获取到读锁、写锁，后续所有线程获取读锁需要等这个线程的写锁释放才能获取到读锁。

准备线程操作的资源类

```java
public class LockResouce {

    ReadWriteLock lock = null;

    public LockResouce(){
        //初始化ReentrantLock
        lock = new ReentrantReadWriteLock(false);
    }

    public void set() throws InterruptedException {
        //获取到写锁
        lock.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + " 正在写入");
        Thread.sleep(5000);
        get(); //获取到写锁后，随即再去获取读锁，也没问题

        //释放写锁
        lock.writeLock().unlock();
    }

    public void get() throws InterruptedException {
        //获取到读锁
        lock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + " 正在读取");
        Thread.sleep(5000);

        //释放读锁
        lock.readLock().unlock();
    }

}
```

开启多个线程去操作资源类

```java
public static void main(String[] args) {

  LockResouce res = new LockResouce();
  new Thread(()->{
    try {
      res.set(); //当前线程获取到写锁
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }).start();

  for (int j = 0; j< 10; j++){
    new Thread(()->{
      try {
        res.get(); //遍历开启10个线程去获取读锁，会等待上面的线程释放写锁
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

}
```

运行结果

```java
Thread-0 正在写入
Thread-0 正在读取 //只有获取到写锁的线程释放了，后续线程才会获取到读锁
Thread-1 正在读取
Thread-2 正在读取
Thread-3 正在读取
Thread-4 正在读取
Thread-5 正在读取
Thread-6 正在读取
Thread-7 正在读取
Thread-8 正在读取
Thread-10 正在读取
Thread-9 正在读取
```



#### 2、演示一个线程获取到读锁后，后续所有的线程都可以获取到读锁，但是所有线程包括自己获取写锁需要排队

准备线程操作的资源类

```java
package com.czdx.locl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Auther: tangwei
 * @Date: 2023/7/31 9:26 AM
 * @Description: 类描述信息
 */
public class LockResouce {

    ReadWriteLock lock = null;

    public LockResouce(){
        //初始化ReentrantLock
        lock = new ReentrantReadWriteLock(false);
    }

    public void set() throws InterruptedException {
        //获取到写锁
        lock.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + " 正在写入");
        Thread.sleep(5000);

        //释放写锁
        lock.writeLock().unlock();
    }

    public void get() throws InterruptedException {
        //获取到读锁
        lock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + " 正在读取");

        Thread.sleep(5000);

        set(); //注意 这行是有问题的，因为读锁中，即便是自己也获取不到写锁，所以这儿会卡死

        //释放读锁
        lock.readLock().unlock();
    }

}

```

开启多个线程去操作资源类

```java
public static void main(String[] args) {

  LockResouce res = new LockResouce();
  for (int j = 0; j< 10; j++) {
    new Thread(() -> {
      try {
        res.get();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  for (int j = 0; j< 2; j++){
    new Thread(()->{
      try {
        res.set();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

}
```

运行结果

```java
Thread-7 正在读取
Thread-9 正在读取
Thread-1 正在读取
Thread-4 正在读取
Thread-8 正在读取
Thread-6 正在读取
Thread-5 正在读取
Thread-0 正在读取
Thread-3 正在读取
Thread-2 正在读取
Thread-10 正在写入
Thread-11 正在写入
```









