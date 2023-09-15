## synchronized关键字的锁具有可重入性



#### 1、什么是可重入性

当一个线程获得一个对象锁后，再次请求该对象锁的时候，是可以得到该对象的锁的。



#### 2、示例

线程操作的资源类

```java
public class LockResouce {

    public synchronized void m1(){ //调用m1，会直接调用m2方法
        System.out.println(Thread.currentThread().getName() + "--m1");
        m2();
    }

    public synchronized void m2(){ //调用m2先要获取 this对象的锁
        System.out.println(Thread.currentThread().getName() + "--m2");
        m3();
    }

    public synchronized void m3(){ //调用m3先要获取 this对象的锁
        System.out.println(Thread.currentThread().getName() + "--m3");
    }

}
```



线程操作

```java
public static void main(String[] args) {

  LockResouce res = new LockResouce();
  new Thread(()->{
    res.m1(); 
  }).start();


}
```

