## HashMap在线程并发下的异常与解决



#### 1、问题复现

```java
public static void main(String[] args) throws InterruptedException {


  HashMap<String, String> list = new HashMap<>();

  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
  for (int i=0; i< 80; i++){
    threadPoolExecutor.execute(()->{
      list.put(Thread.currentThread().getName(), UUID.randomUUID().toString());
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



#### 2、如何解决

- 使用Collections.synchronizedMap(new HashMap<>())包裹HashMap

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    Map<String, String> list = Collections.synchronizedMap(new HashMap<>());
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    for (int i=0; i< 80; i++){
      threadPoolExecutor.execute(()->{
        list.put(Thread.currentThread().getName(), UUID.randomUUID().toString());
        System.out.println(list);
      });
    }
  }
  ```

  

- 使用ConcurrentHashMap替代HashMap：

  ```java
  public static void main(String[] args) throws InterruptedException {
  
  
    ConcurrentHashMap<String, String> list = new ConcurrentHashMap<>();
  
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    for (int i=0; i< 80; i++){
      threadPoolExecutor.execute(()->{
        list.put(Thread.currentThread().getName(), UUID.randomUUID().toString());
        System.out.println(list);
      });
    }
  }
  ```

  