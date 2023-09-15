## SynchronousQueue的使用

队列中只能存放一个元素的阻塞队列。每次put都会阻塞等待一次take，同样的一次take必须等待一次put，也就是说put和take是一组操作。

```java
SynchronousQueue synchronousQueue = new SynchronousQueue();

new Thread(()->{
  try {
    synchronousQueue.put(1);//一次put，在这儿阻塞等待一次take

    System.out.println("put success");

    synchronousQueue.put(2);//一次put，在这儿阻塞等待一次take

    System.out.println("put finish");
  } catch (InterruptedException e) {
    e.printStackTrace();
  }
}).start();

Thread.sleep(2000);

new Thread(()->{
  try {
    System.out.println("first" + synchronousQueue.take());//跟第一次的put形成一组，没有put到来之前阻塞等待

    System.out.println("second" + synchronousQueue.take());//跟第二次的put形成一组，没有put到来之前阻塞等待

  } catch (InterruptedException e) {
    e.printStackTrace();
  }
}).start();
```

