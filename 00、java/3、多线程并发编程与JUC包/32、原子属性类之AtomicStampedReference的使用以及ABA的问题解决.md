## 原子属性类之AtomicStampedReference的使用以及ABA的问题解决



#### 1、ABA问题

在 `24、CAS的ABA问题`中我们已经演示过ABA的问题了！



#### 2、ABA问题的解决

准备线程操作资源类

```java
public class LockResouce {


    public AtomicStampedReference<String> atomicString = new AtomicStampedReference<>("a", 1); //初始化，"a"为初始值， 1为初始版本

    public void changeToB(){

        if(atomicString.compareAndSet("a", "b", 1, 2)){ //将数据从 a 改为 b，将版本从1改为2
            System.out.println(Thread.currentThread().getName() + "设置成功" +atomicString.getReference());
        }else{
            System.out.println(Thread.currentThread().getName() + "设置失败" +atomicString.getReference());
        }

    }

    public void changeToA(){
        atomicString.compareAndSet("b", "a", 2, 3); // 将数据从 b 改为 a，将版本从2改为3
        System.out.println(Thread.currentThread().getName() + "设置成功" +atomicString.getReference());
    }



}
```

准备多个线程

```java
public static void main(String[] args) throws InterruptedException {

  LockResouce lockResouce = new LockResouce();

  Thread t1 = new Thread(()->{

    try {
      Thread.sleep(10000L);

      lockResouce.changeToB();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }, "A");
  t1.start();

  Thread t2 = new Thread(()->{
    lockResouce.changeToB();

    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    lockResouce.changeToA();

  }, "B");
  t2.start();
}
```

