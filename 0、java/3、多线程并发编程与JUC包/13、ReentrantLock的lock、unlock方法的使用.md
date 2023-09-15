## ReentrantLock的lock、unlock方法的使用

> 线程可重入锁，就是它是一把互斥锁，但是它只互斥别的线程，不互斥自己的线程。



#### 1、lock()方法的作用

获取锁，获取不到就一直等，即便是线程中断了，照样等待锁，这样的写法容器产生死锁



#### 2、unlock()方法的作用

释放锁



例子：

准备线程操作的资源类

```java
public class UserResource {

    Lock lock = null;

    public UserResource(){
      	//初始化ReentrantLock
        lock = new ReentrantLock(false);
    }

    public void get() throws InterruptedException {
      	//上锁
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "get");

        Thread.sleep(2000);
      
      	//调用set方法，因为ReentrantLock有可重入性
      	set();
      
				//释放锁
        lock.unlock();
      	
    }

    public void set() throws InterruptedException {
      	//上锁
        lock.lock(); //因为ReentrantLock有可重入性，同一个线程进来不会阻塞
        System.out.println(Thread.currentThread().getName() + "set");

        Thread.sleep(5000);
				//释放锁
        lock.unlock();
    }
}
```

准备多个线程去操作资源类

```java
 public static void main(String[] args) {
        UserResource userResource = new UserResource();

   			//线程1 去操作资源类
        new Thread(()->{
            try {
                userResource.get();//虽然这儿只调用了get方法，get方法中会调用set方法，但是set方法的锁已经被激活了，所以其它线程调用set会阻塞等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1").start();


        new Thread(()->{
            try {
                userResource.set(); //线程2调用set方法，会排队等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
```

