## 原子属性类之基础数据类型之AtomicInteger使用案例



准备线程操作的资源类

```java
public class CasTest {

    private Integer a = 1;

    public AtomicInteger atomicInteger = new AtomicInteger(a);

    public void plusOne(){
        atomicInteger.getAndIncrement();
        System.out.println(atomicInteger.get());
    }
}
```

开启多线程对资源类的操作

```java
public static void main(String[] args) {
  CasTest casTest = new CasTest();
  for(int i=0; i < 1000; i ++){ //开启1000个线程对资源类的数据进行操作
    Thread thread = new Thread(() -> {
      casTest.plusOne();
    });
    thread.start();
  }
}
```

得到结果，无论执行多少次结果都是一样的

```java
....
1004
1005
1006
1007
1008
1009
1010
```



#### 底层原理

能够实现线程安全，靠的是java底层的Unsafe类（位于jdk源码中的rt.jar中的sun.misc包中），通过Unsafe类java可以像C的指针一样操作内存数据（因为Unsafe中的很多方法都是native修饰的，交给操作系统底层完成的），并且调用Unsafe类中的方法JVM会对应实现出汇编指令，汇编指令会产生一条CPU原语，CPU原语是原子性的，即线程安全的。



源码分析：

用户调用新增getAndIncrement()方法

```java
atomicInteger.getAndIncrement();
```

进入AtomicInteger的getAndIncrement()方法

```java
public final int getAndIncrement() {
  return unsafe.getAndAddInt(this, valueOffset, 1);//this是指当前类，valueOffset是指内存偏移量，1是要增加的数值
}
```

点击getAndAddInt进入Unsafe类的getAndAddInt方法

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
  int var5;
  do {
    var5 = this.getIntVolatile(var1, var2); //先获取到偏移量的值
  } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4)); //并发设置值，当然只有一个会成功，失败了再去操作do的循环体

  return var5;
}
```

compareAndSwapInt方法就是被native修饰的，交给操作系统处理的方法。