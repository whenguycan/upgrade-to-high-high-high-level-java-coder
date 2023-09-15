## 线程数据隔离之ThreadLocal



#### 1、什么是ThreadLocal

在多线程并发下，各个线程有各自需要存储的数据，那么就需要使用ThreadLocal来对数据进行隔离！



#### 2、ThreadLocal有哪些方法

- set() 往ThreadLocal中设置一个数据
- get() 获取ThreadLocal中的一个数据
- remove() 删除ThreadLocal中的数据一个数据



#### 3、ThreadLocal的基本使用

```java
static ThreadLocal<String> threadLocal = new ThreadLocal<>(); //用于存放各个线程的数据

public static void main(String[] args) throws InterruptedException {
  for (int i =0; i< 20; i++){
    final int t = i;
    new Thread(()->{
      threadLocal.set("t" + t); //各自线程执行到这儿，就往threadLocal中放一个自己线程的数据

      System.out.println(Thread.currentThread().getName() + "设置的ThreadLocal的值为" + threadLocal.get());
      
      threadLocal.remove(); //线程退出一定要remove掉自己线程的数据
    }, "t" + t).start();
  }
}
```







