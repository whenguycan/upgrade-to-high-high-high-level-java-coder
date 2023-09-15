## 原子属性类之数组型之AtomicIntegerArray使用详解及使用案例



#### 1、AtomicIntegerArray的基本使用方法

```java
public static void main(String[] args) throws InterruptedException {

        //创建一个指定长度的原子数组
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        System.out.println(atomicIntegerArray); //初始化后的原子数组为[0,0,0,0,0,0,0,0,0,0]

        //返回指定位置的元素
        System.out.println(atomicIntegerArray.get(5));

        //设置指定位置的元素
        atomicIntegerArray.set(0, 10);
        System.out.println(atomicIntegerArray);

        //在设置元素的新值时，返回数组元素的旧值
        System.out.println(atomicIntegerArray.getAndSet(1, 11));//返回的是0
        System.out.println(atomicIntegerArray);

        //修改宿主元素的值，把数组元素加上某个值
        System.out.println(atomicIntegerArray.addAndGet(0, 22));//得到返回值32
        System.out.println(atomicIntegerArray.getAndAdd(0, 22));//得到返回值32
        System.out.println(atomicIntegerArray);

        //cas操作
        System.out.println(atomicIntegerArray.compareAndSet(0, 54, 56));//如果下标为0的元素，初始值如果为54，那么就改成56，得到返回值为true
        System.out.println(atomicIntegerArray);

        //自增、自减操作
        System.out.println(atomicIntegerArray.incrementAndGet(0)); //得到返回值57
        System.out.println(atomicIntegerArray.getAndIncrement(0)); //得到返回值57

        System.out.println(atomicIntegerArray.decrementAndGet(0));//得到返回值57
        System.out.println(atomicIntegerArray.getAndDecrement(0));//得到返回值57
        System.out.println(atomicIntegerArray);
}
```



#### 2、配合多线程使用

略