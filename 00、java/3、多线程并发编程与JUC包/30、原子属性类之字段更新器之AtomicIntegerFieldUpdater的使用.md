## 原子属性类之字段更新器之AtomicIntegerFieldUpdater的使用

AtomicIntegerFieldUpdater可以对原子整数字段进行更新，要求：

1. 字段必须使用volatile修饰，使线程之间可见
2. 只能是实例变量，不能是静态变量，也不能使用final修饰





使用案例

线程操作资源类

```java
public class LockResouce {


    volatile public int age;

    public int getAge() {
        return age;
    }

    //初始化更新器
    AtomicIntegerFieldUpdater<LockResouce> updater = AtomicIntegerFieldUpdater.newUpdater(LockResouce.class, "age"); //指定更新器是对LockResouce类的age字段进行操作的



}
```

多线程操作类

```java
public static void main(String[] args) throws InterruptedException {

  LockResouce res = new LockResouce();
  for (int i=0; i<=100; i++){
    new Thread(()->{
      res.updater.incrementAndGet(res);
    }).start();
  }

  System.out.println(res.getAge());
}
```

