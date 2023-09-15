## synchronized关键字实现锁

#### 一、什么是synchronized

java中每个对象都有与之关联的内部锁，这个锁就是synchronized关键字实现的。



#### 二、判断synchrnoized能不能起作用的关键

查看多线程并发访问的资源synchronized的锁对象是不是同一个！是同一个就能保锁正常使用，否则不能保证锁的正常使用！

什么是锁对象？

```java
synchronized(锁对象){ // 这个锁对象可以是 this、对象的一个属性 等等
  ......
  业务逻辑
}

或 
  
public synchronized void xxx(){ //锁对象是 this
  ......
  业务逻辑
}

或

public synchronized static void xxx(){ //锁对象是 xxx.class
  ......
  业务逻辑
}
```





#### 三、synchronized的使用范例

1. 修饰代码块

   > 锁对象，自己选择。

   - 使用资源对象做为锁对象

     线程操作的资源类如下：

     ```java
     public class LockResouce {
     
         public void mm(){
     
             synchronized (this){
                 for (int i=0; i< 100; i++){
                     System.out.println(Thread.currentThread().getName() + "----->" + i);
                 }
             }
     
         }
     
     }
     ```

     开2个线程去操作资源类

     ```java
     public static void main(String[] args) {
     
       LockResouce res = new LockResouce();
     
       new Thread(()->{
         res.mm();
       }, "t1").start();
     
     
       new Thread(()->{
         res.mm();
       }, "t2").start();
     }
     
     
     ```

     <font color="red">**注意：如果我们把线程去操作资源类改成，如下的代码**</font>

     ```java
     public class SynchTest01 {
     
         public static void main(String[] args) {
     
             LockResouce res = new LockResouce();
             LockResouce res2 = new LockResouce(); //这儿new了2个对象
     
             new Thread(()->{
                 res.mm(); //调用res对象
             }, "t1").start();
     
     
             new Thread(()->{
                 res2.mm(); //调用res2对象
             }, "t2").start();
         }
     
     }
     ```

     此时锁是失效的！2个不同的锁对象之间不能实现线程同步。

     

   - 使用常量做为锁对象

     线程操作的资源类如下：

     ```java
     public class LockResouce {
     
         private final Object obj = new Object();
     
         public  void mm(){
     
             synchronized (obj){
                 for (int i=0; i< 100; i++){
                     System.out.println(Thread.currentThread().getName() + "----->" + i);
                 }
             }
             
         }
     
     }
     ```

     开2个线程去操作资源类

     ```java
     public static void main(String[] args) {
     
       LockResouce res = new LockResouce();
     
       new Thread(()->{
         res.mm();
       }, "t1").start();
     
     
       new Thread(()->{
         res.mm();
       }, "t2").start();
     }
     
     
     ```

     <font color="red">**注意：如果我们把线程去操作资源类改成，如下的代码**</font>

     ```java
     public class SynchTest01 {
     
         public static void main(String[] args) {
     
             LockResouce res = new LockResouce();
             LockResouce res2 = new LockResouce(); //这儿new了2个对象
     
             new Thread(()->{
                 res.mm(); //调用res对象
             }, "t1").start();
     
     
             new Thread(()->{
                 res2.mm(); //调用res2对象
             }, "t2").start();
         }
     
     }
     ```

     此时锁是失效的！2个不同的锁对象之间不能实现线程同步。<font color="red">那么有什么方法可以保证线程同步？</font>

     

2. 修饰非静态方法

   > 默认锁对象为this

   线程操作的资源类如下：

   ```java
   public class LockResouce {
   
       public synchronized void mm(){ //其实等同于上面 修饰代码块 的方式，默认的锁对象就是this
   
           for (int i=0; i< 100; i++){
               System.out.println(Thread.currentThread().getName() + "----->" + i);
           }
   
   
       }
   
   }
   ```

   开2个线程去操作资源类

   ```java
   public static void main(String[] args) {
   
     LockResouce res = new LockResouce();
   
     new Thread(()->{
       res.mm();
     }, "t1").start();
   
   
     new Thread(()->{
       res.mm();
     }, "t2").start();
   }
   
   ```

   <font color="red">**注意：如果我们把线程去操作资源类改成，如下的代码**</font>

   ```java
   public class SynchTest01 {
   
       public static void main(String[] args) {
   
           LockResouce res = new LockResouce();
           LockResouce res2 = new LockResouce(); //这儿new了2个对象
   
           new Thread(()->{
               res.mm(); //调用res对象
           }, "t1").start();
   
   
           new Thread(()->{
               res2.mm(); //调用res2对象
           }, "t2").start();
       }
   
   }
   ```

   此时锁是失效的！2个不同的锁对象之间不能实现线程同步。

   

   

3. 修饰静态方法

   > 默认锁对象为 运行时类（xxx.class），可以简单认为是把当前的字节码文件做为锁对象

   线程操作资源类如下：

   ```java
   public class LockResouce {
   
       public synchronized static void mm(){
           for (int i=0; i< 100; i++){
               System.out.println(Thread.currentThread().getName() + "----->" + i);
           }
       }
   
       public  void mm2(){
           synchronized (LockResouce.class){
               for (int i=0; i< 100; i++){
                   System.out.println(Thread.currentThread().getName() + "----->" + i);
               }
           }
   
       }
   }
   ```

   开2个线程去操作资源类

   ```java
   public static void main(String[] args) {
   
     new Thread(()->{
       LockResouce.mm();
     }, "t1").start();
   
   	LockResouce res = new LockResouce();
     new Thread(()->{
       res.mm2();
     }, "t2").start();
   }
   ```

   

   