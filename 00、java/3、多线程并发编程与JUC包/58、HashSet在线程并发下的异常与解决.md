## HashSet在线程并发下的异常与解决



#### 1、问题复现

```java
public static void main(String[] args) throws InterruptedException {


  HashSet<String> list = new HashSet<>();

  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20));
  for (int i=0; i< 80; i++){
    threadPoolExecutor.execute(()->{
      list.add(Thread.currentThread().getName());
      System.out.println(list);
    });
  }
}
```

出现如下异常

```java
java.util.ConcurrentModificationException
  ......
  ......
  ......
```



#### 2、问题解决

- 使用Collections.synchronizedSet(new HashSet<String>())包裹 HashSet

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    Set<String> list = Collections.synchronizedSet(new HashSet<>());
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20));
    for (int i=0; i< 80; i++){
      threadPoolExecutor.execute(()->{
        list.add(Thread.currentThread().getName());
        System.out.println(list);
      });
    }
  }
  ```

  

- 使用java.util.concurrent包中的CopyOnWriteArraySet(写时复制，当一个线程写的时候会复制出一个Set的副本，对副本进行操作之后，把对Set的指针指向新的Set，丢掉原Set的数据)替代HashSet

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    CopyOnWriteArraySet<String> list = new CopyOnWriteArraySet<>();
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(20));
    for (int i=0; i< 80; i++){
      threadPoolExecutor.execute(()->{
        list.add(Thread.currentThread().getName());
        System.out.println(list);
      });
    }
  }
  ```

  

