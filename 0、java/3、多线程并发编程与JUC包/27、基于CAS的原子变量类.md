## 基于CAS的原子属性类



在多线程并发下，对共享变量进行并发读、写操作的时候，如果该共享变量是一个原子变量类，那么可以保证这个共享变量的原子性、可见性。

<font color="red">原子属性类是 volatile + cas的结合体 </font>

原子变量类有12个：

1. 基础数据型：AtomicInteger、AtomicLong、AtomicBoolean
2. 数组型：AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray
3. 字段更新器：AtomicIntegerFieldUpdater、AtomicLongFieldUpdater、AtomicReferenceFieldUpdater
4. 引用型：AtomicReference、AtomicStampedReference、AtomicMarkableReference