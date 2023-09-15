## LinkedList与ArrayList在线程并发下的异常与解决



#### 1、问题复现

```java
public static void main(String[] args) throws InterruptedException {


  ArrayList<String> list = new ArrayList<>();

  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
  for (int i=0; i< 30; i++){
    threadPoolExecutor.execute(()->{
      list.add(Thread.currentThread().getName());
      System.out.println(list);
    });
  }
}
```

会出现如下异常

```java
java.util.ConcurrentModificationException
  ......
  ......
  ......
```



#### 2、如何解决

- 使用Vector替代 ArrayList，Vector的add方法是加了synchronized的锁的

  ````java
  public static void main(String[] args) throws InterruptedException {
  
  
    Vector<String> list = new Vector<>();
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    for (int i=0; i< 30; i++){
      threadPoolExecutor.execute(()->{
        list.add(Thread.currentThread().getName());
        System.out.println(list);
      });
    }
  }
  ````

  

- 使用Collections.synchronizedList(new ArrayList<String>()) 包裹 ArrayList

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    List<String> list = Collections.synchronizedList(new ArrayList<>());
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    for (int i=0; i< 30; i++){
      threadPoolExecutor.execute(()->{
        list.add(Thread.currentThread().getName());
        System.out.println(list);
      });
    }
  }
  ```

- 使用java.util.concurrent包中的CopyOnWriteArrayList(写时复制，当一个线程写的时候会复制出一个ArrayList的副本，对副本进行操作之后，把对ArrayList的指针指向新的ArrayList，丢掉原ArrayList的数据)替代ArrayList

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    for (int i=0; i< 30; i++){
      threadPoolExecutor.execute(()->{
        list.add(Thread.currentThread().getName());
        System.out.println(list);
      });
    }
  }
  ```

  