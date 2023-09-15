## 自己写的springboot的ApplicationListener生命周期监听器如何执行的源码分析



![avatar](../../images/WechatIMG629.png)

从上图，我们可以看出，每一个事件都伴随这一个SpringApplicationRunListener的方法，所以猜测，事件的执行肯定跟SpringApplicationRunListener分不开，在`4、各个监听器被springboot加载的源码分析`中，我们分析过所有的监听器是怎么被加载的，最后加载了系统中所有的SpringApplicationRunListener，其中就有一个很关键的Listener叫`EventPublishingRunListener`，看名字就知道了，这个类就是专门用来发布事件的，我们进到这个类的内部去看，就只要看starting方法就行了，其它方法类似

```java
@Override
public void starting(ConfigurableBootstrapContext bootstrapContext) {
  multicastInitialEvent(new ApplicationStartingEvent(bootstrapContext, this.application, this.args));
}
```

new出了ApplicationStartingEvent类了，然后`multicastInitialEvent`方法具体内容

```java
private void multicastInitialEvent(ApplicationEvent event) {
  refreshApplicationListeners();
  this.initialMulticaster.multicastEvent(event);
}
```

`multicastEvent`方法最终走到了如下的方法内

```java
@Override
public void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType) {
  ResolvableType type = (eventType != null ? eventType : ResolvableType.forInstance(event));
  Executor executor = getTaskExecutor();
  for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
    if (executor != null) {
      executor.execute(() -> invokeListener(listener, event));
    }
    else {
      invokeListener(listener, event);
    }
  }
}
```

`getApplicationListeners(event, type)`获取所有的跟event匹配的ApplicationListeners，然后开线程去执行每个ApplicationListeners中的onApplicationEvent方法。<font color="red">至于，getApplicationListeners(event, type)的ApplicationListeners为什么能取到，自己打断点去看！</font>

