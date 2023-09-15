## 自己写一个Listener+event体验事件驱动开发



#### 1、Event即事件，它是Listener所监听的目标

> 每一个Event都需要继承ApplicationEvent，同时每个Event必有至少一个Object类型参数的构造函数，且该构造函数需要实现父类的一个Object类型参数的构造函数

代码如下：

```java
public class MyEvent extends ApplicationEvent {

    private String msg;

    //构造函数，
    // source是一个参数为Object类型，具体的类型自己去修改。该参数就是发布时间传递的参数。
    public MyEvent(Object source) {

      	//实现父类的构造函数
        super(source);
        System.out.println("事件执行了...");
        this.msg = msg;
    }

    
}
```



#### 2、Listener即监听器，就是用来监听感兴趣的Event的。

- 实现接口的方式

  >  每一个Listener都需要实现ApplicationListener<ApplicationEvent的子类>，且重写onApplicationEvent()方法

  ```java
  //Component注解不能少，因为需要在springboot内部调用，需要变成一个bean
  @Component
  public class MyListener implements ApplicationListener<MyEvent> {
      @Override
      public void onApplicationEvent(MyEvent event) {
          System.out.println("我的监听器执行了...");
      }
  }
  ```

- 使用注解的方式（<font color="red">**推荐使用，我们的demo中也使用这个方案**</font>）

  > 在任意方法上使用@EventListener注解，指定其中要监听的事件的class类型

  ```java
  @Component
  public class MyListener{
      @EventListener(classes = {MyEvent.class})
      public void listen(MyEvent event) {
          System.out.println("我的监听器执行了...");
      }
  }
  ```

  <font color="red">使用注解的方式，可以将Listener设置为异步执行，需要在入口文件上加`@EnableAsync` 在Listener类上加`@Async`注解就行了，怎么证明？使用log.info(....)分别在event和listener中打印日志，看线程名称是否相同！</font>



#### 3、EventPublisher事件发布，只有触发事件发布才能触发监听

> 使用ApplicationEventPublisher的实现类，调用其publishEvent()来发布事件

```java
@Component
public class MyEventPublisher {

    @Autowired
    private ApplicationContext applicationEventPublisher;

    public void pushMyEvent(){
        applicationEventPublisher.publishEvent(new MyEvent(this, "测试"));
    }
}
```



#### 4、调用

```java
@Autowired
private MyEventPublisher myEventPublisher;

@GetMapping("/index")
public void index(){
  myEventPublisher.pushMyEvent();
}
```

