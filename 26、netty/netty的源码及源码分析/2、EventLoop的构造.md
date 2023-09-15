## EventLoop的构造

`EventLoop`在netty中发挥着驱动引擎的作用，我们以`NioEventLoopGroup`和`NioEventLoop`为例着重分析一下`EventLoopGroup`和`EventLoop`的创建、一些重要的数据结构和netty的一些优化。



## 1 NioEventLoopGroup

咱们以`NioEventLoopGroup`为例进行分析。`NioEventLoopGroup`有很多构造方法，咱们不再一一贴出，只贴出2个关键的构造方法。

`NioEventLoopGroup( int nThreads, Executor executor, final SelectorProvider selectorProvider)`该构造方法给出了`SelectStrategyFactory`的默认值为`DefaultSelectStrategyFactory.INSTANCE`。`EventLoop`在每次循环时需要调用该类的`calculateStrategy`方法来决定循环的策略，具体咱们后边讲。

我们通过`new NioEventLoopGroup()`或者`new NioEventLoopGroup(32)`创建`EventLoopGroup`时最终都会调用到这个构造方法。接着又调用了另一个构造方法，咱们跟下去。



```java
public NioEventLoopGroup(
        int nThreads, Executor executor, final SelectorProvider selectorProvider) {
    this(nThreads, executor, selectorProvider, DefaultSelectStrategyFactory.INSTANCE);
}
```

这里调用了父类 `MultithreadEventLoopGroup`的构造方法，咱们继续跟下去。

```java
public NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider,
                         final SelectStrategyFactory selectStrategyFactory) {
    super(nThreads, executor, selectorProvider, selectStrategyFactory, RejectedExecutionHandlers.reject());
}
```

`MultithreadEventLoopGroup`的构造方法如下，这里给出了`nThreads`的默认值，为`MultithreadEventLoopGroup`的静态属性`DEFAULT_EVENT_LOOP_THREADS`。接着调用父类`MultithreadEventExecutorGroup`的构造方法。

```java
protected MultithreadEventLoopGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
    super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, threadFactory, args);
}
```

`DEFAULT_EVENT_LOOP_THREADS`的赋值在`MultithreadEventLoopGroup`的静态代码块中，我们看到该值默认为cpu核数×2。

```jsx
static {
    DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
    
```

咱们接着跟到`MultithreadEventExecutorGroup`的构造方法，这里继续调用本类的构造方法`MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args)`，并且为`executor`赋默认值。

```csharp
protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
    this(nThreads, threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory), args);
}
```

`executor`的默认值为`ThreadPerTaskExecutor`，顾名思义就是为每一个任务创建一个线程，我们看一下它的实现，代码如下，`execute`方法中为每一个任务创建了一个线程。

```java
public final class ThreadPerTaskExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        threadFactory.newThread(command).start();
    }
}
```

我们继续跟到`MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args)`，这里接着调用另一个构造方法`MultithreadEventExecutorGroup(int nThreads, Executor executor,EventExecutorChooserFactory chooserFactory, Object... args)`并且为`chooserFacotry`赋默认值，`chooserFactory`咱们在“服务端的启动流程”和“客户端的启动流程”中均有涉及，作用是在为`Channel`绑定`EventLoop`时轮询地从`EventLoopGroup`里选择`EventLoop`，这里不再赘述。

```cpp
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
    this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
}
```

接着看下一个构造方法`MultithreadEventExecutorGroup(int nThreads, Executor executor,EventExecutorChooserFactory chooserFactory, Object... args)`，**这里就是创建`EventLoopGroup`的核心逻辑了**。

首先创建了一个`EventExecutor`数组，并赋值给`children`属性，数组长度和`nThreads`相等，很容易想到，这里应该就是保存`EventLoop`的数组了。

紧接着循环调用`newChild`方法为数组的每一个元素赋值。

最后调用`chooserFactory.newChooser(children)`为`chooser`赋值。



```java
 protected MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                            EventExecutorChooserFactory chooserFactory, Object... args) {

        children = new EventExecutor[nThreads];

        for (int i = 0; i < nThreads; i ++) {
            boolean success = false;
            try {
                children[i] = newChild(executor, args);
                success = true;
            } catch (Exception e) {
              
            } finally {
            }
        }
        chooser = chooserFactory.newChooser(children);
}
```

我们跟着看一下`newChild`方法，该方法为抽象的，这里`newChild`方法的实现在`NioEventLoopGroup`中。好了，到这里咱们看到了`NioEventLoop`的构造方法，继续跟进去。

```java
protected EventLoop newChild(Executor executor, Object... args) throws Exception {
    return new NioEventLoop(this, executor, (SelectorProvider) args[0],
        ((SelectStrategyFactory) args[1]).newSelectStrategy(), (RejectedExecutionHandler) args[2]);
}
```

## 2 NioEventLoop

来看`NioEventLoop`的构造方法，这里`NioEventLoop`保存了3个属性。

- `provider`: `selector`的工厂类，从`provider`可以得到`selector`。
- `selector`: 调用`provider.openSelector()`得到的`selector`，这里netty做了优化，咱们后边讲。
- `selectStrategy`：这个的`calculateStrategy`方法返回值，决定了`EventLoop`的下一步动作，咱们也是后边讲。



```php
NioEventLoop(NioEventLoopGroup parent, Executor executor, SelectorProvider selectorProvider,
             SelectStrategy strategy, RejectedExecutionHandler rejectedExecutionHandler) {
    super(parent, executor, false, DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
    if (selectorProvider == null) {
        throw new NullPointerException("selectorProvider");
    }
    if (strategy == null) {
        throw new NullPointerException("selectStrategy");
    }
    provider = selectorProvider;
    selector = openSelector();
    selectStrategy = strategy;
}
```

继续跟到父类`SingleThreadEventLoop`的构造方法，这里初始化了一个属性`tailTasks`，具体有什么用，咱们一会儿说，**先记住这里有一个任务队列叫`tailTasks`**。



```java
protected SingleThreadEventLoop(EventLoopGroup parent, Executor executor,
                                boolean addTaskWakesUp, int maxPendingTasks,
                                RejectedExecutionHandler rejectedExecutionHandler) {
    super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
    tailTasks = newTaskQueue(maxPendingTasks);
}
```

继续跟到父类`SingleThreadEventExecutor`的构造方法，这里有几个属性赋值。

- `addTaskWakesUp`：这个是用来唤醒阻塞在`taskQueue`上的`EventLoop`线程的，在`NioEventLoop`中`EventLoop`不会阻塞在`taskQueue`上，这里用处不是很大，可以先不研究它。
- `maxPendingTasks`：后面紧接着就用到了，任务队列的最大长度。
- `executor`：真正生成线程的类，默认是`ThreadPerTaskExecutor`。
- `taskQueue`：**任务队列，还记得上边已经有一个`tailTask`队列了吗，这里是另外一个队列**。
- `rejectedExecutionHandler`：拒绝策略。



```kotlin
protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor,
                                    boolean addTaskWakesUp, int maxPendingTasks,
                                    RejectedExecutionHandler rejectedHandler) {
    super(parent);
    this.addTaskWakesUp = addTaskWakesUp;
    this.maxPendingTasks = Math.max(16, maxPendingTasks);
    this.executor = ObjectUtil.checkNotNull(executor, "executor");
    taskQueue = newTaskQueue(this.maxPendingTasks);
    rejectedExecutionHandler = ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
}
```

继续跟到父类`AbstractScheduledEventExecutor`的构造方法，这里什么也没干，又继续调用父类的构造方法，但是我们注意到该类中也有一个**队列`scheduledTaskQueue`，顾名思义是定时任务队列**，只是该队列没有在构造方法中初始化，等到有定时任务加入时才初始化。



```php
public abstract class AbstractScheduledEventExecutor extends AbstractEventExecutor {

    Queue<ScheduledFutureTask<?>> scheduledTaskQueue;

    protected AbstractScheduledEventExecutor(EventExecutorGroup parent) {
        super(parent);
    }
}
```

继续跟到父类`AbstractEventExecutor`的构造方法，这里为`parent`赋值，即该`EventLoop`所属的`EventLoopGroup`。



```php
protected AbstractEventExecutor(EventExecutorGroup parent) {
    this.parent = parent;
}
```



## 3 netty的一些优化

#### 3.1 对selector的优化

我们回到`NioEventLoop`类中的`openSelector`方法，这个方法在干什么呢，它在通过反射替换`sun.nio.ch.SelectorImpl`类的两个属性`selectedKeys`和`publicSelectedKeys`，将这两个属性都替换成了`SelectedSelectionKeySet`类的实例。为什么要这么换呢，咱们接着往下看。



```dart
 private Selector openSelector() {
        final Selector selector;
        try {
            selector = provider.openSelector();
        } catch (IOException e) {
        }

        final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();

        Object maybeSelectorImplClass = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    return Class.forName(
                            "sun.nio.ch.SelectorImpl",
                            false,
                            PlatformDependent.getSystemClassLoader());
                } catch (ClassNotFoundException e) {
                } catch (SecurityException e) {
                }
            }
        });

        final Class<?> selectorImplClass = (Class<?>) maybeSelectorImplClass;

        Object maybeException = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                    Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");

                    selectedKeysField.setAccessible(true);
                    publicSelectedKeysField.setAccessible(true);

                    selectedKeysField.set(selector, selectedKeySet);
                    publicSelectedKeysField.set(selector, selectedKeySet);
                    return null;
                } catch (NoSuchFieldException e) {
 
                } catch (IllegalAccessException e) {

                } catch (RuntimeException e) {
                   
                }
            }
        });
        if (maybeException instanceof Exception) {

        } else {
            //将selectedKeySet保存到selectedKeys属性中
            selectedKeys = selectedKeySet;
        }

        return selector;
    }
```

`sun.nio.ch.SelectorImpl`类中的`selectedKeys`和`publicSelectedKeys`的作用是保存有兴趣事件发生的`Selectionkey`，其中`publicSelectedKeys`是`selectedKeys`的包装类，`Util.ungrowableSet(this.selectedKeys)`，看方法名`ungrowableSet`表示包装之后这个`set`就不能添加元素了，但是可以删除元素。也就是说jdk内部使用`selectedKeys`（注意这是个`protected`字段）进行添加和删除操作，而暴露给用户的是`publicSelectedKeys`只能进行删除操作（看`selectedKeys`方法）。

默认实现用的是`HashSet`，



```kotlin
public abstract class SelectorImpl extends AbstractSelector {
    protected Set<SelectionKey> selectedKeys = new HashSet();
    protected HashSet<SelectionKey> keys = new HashSet();
    private Set<SelectionKey> publicKeys;
    private Set<SelectionKey> publicSelectedKeys;

    protected SelectorImpl(SelectorProvider var1) {
        super(var1);
        if (Util.atBugLevel("1.4")) {
            this.publicKeys = this.keys;
            this.publicSelectedKeys = this.selectedKeys;
        } else {
            this.publicKeys = Collections.unmodifiableSet(this.keys);
            this.publicSelectedKeys = Util.ungrowableSet(this.selectedKeys);
        }

    }
    
    public Set<SelectionKey> selectedKeys() {
        if (!this.isOpen() && !Util.atBugLevel("1.4")) {
            throw new ClosedSelectorException();
        } else {
            return this.publicSelectedKeys;
        }
    }
}
```

再来看看`SelectedSelectionKeySet`，这里用的数据结构是数组，很显然netty这里的优化是因为**数组的元素添加和遍历操作比`HashSet`更快**。有同学对这里有两个数组的存在表示疑问，大家不用操心这个问题了，因为我也没看懂为什么会有两个数组，还要交替使用。netty在后来的版本中做了优化，只用一个数组就实现了。所以这里只要知道netty用数组进行优化就可以了。

那么又一个问题来了，为什么jdk里边不用数组或者`List`，而用`HashSet`呢，那是因为`selector`的`select`方法每次调用都会把已经准备好的`SelectionKey`放入`selectedKeys`中，如果用户在第一次调用`select`方法之后没有处理相应`Channel`的事件的，也没有删除`selectedKeys`中的元素，那么再次调用`select`之后，相同的`SelectionKey`会再次加入`selectedKeys`中，如果`selectedKeys`使用数组或者`List`实现将起不到去重的效果。

另一方面，如果使用`List`或者数组，删除成本也比较高。

而netty的实现中保证不会在连续调用两次`select`方法之间不删除`selectedKeys`中的元素，而且`netty`直接将`selectedKeys`暴露出来，在删除的时候可以直接将数组中对应索引的元素设置为null。



```java
final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {

    private SelectionKey[] keysA;
    private int keysASize;
    private SelectionKey[] keysB;
    private int keysBSize;
    private boolean isA = true;
}
```

## 3.2 对任务队列的优化

在`SingleThreadEventExecutor`的构造方法中`taskQueue`属性通过调用`newTaskQueue`方法产生，`newTaskQueue`方法在`NioEventLoop`中被覆盖了，我们看一下。

这里被优化成了`MpscQueue`，全称是`MultiProducerSingleConsumerQueue`，即**多生产者单消费者队列**，感兴趣的同学自己去看一下，既然单独做一个这样的队列出来，在当前场景下自然是比`BlockingQueue`性能更好了。

为什么可以做这样的优化呢，很显然，这里的队列消费者只有一个那就是`EventLoop`，而生产者会有多个。并且，这里有一行注释`This event loop never calls takeTask()`，这就说明这个`MultiProducerSingleConsumerQueue`并没有阻塞的`take`方法，而正好`NioEventLoop`也不需要调用阻塞的`take`方法，正好适合。



```java
@Override
protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
    // This event loop never calls takeTask()
    return PlatformDependent.newMpscQueue(maxPendingTasks);
}
```

同样的，`tailTask`这个队列也被优化成了`MpscQueue`。我们看一下`scheduledTaskQueue`是什么队列，答案在`AbstractScheduledEventExecutor#scheduledTaskQueue`方法中，我们看到`scheduledTaskQueue`仅仅是一个普通的优先级队列，甚至都不是一个线程安全的阻塞队列。



```php
Queue<ScheduledFutureTask<?>> scheduledTaskQueue() {
    if (scheduledTaskQueue == null) {
        scheduledTaskQueue = new PriorityQueue<ScheduledFutureTask<?>>();
    }
    return scheduledTaskQueue;
}
```

为什么呢，为什么`tailTasks`和`taskQueue`是`MpscQueue`，而`scheduledTaskQueue`是非线程安全的队列呢？可能是因为没有的带优先级功能的`MpsQueue`吧。

`scheduledTaskQueue`是非线程安全的队列，会不会有多线程安全问题呢，答案是不会。我们看一下`AbstractScheduledEventExecutor#schedule(io.netty.util.concurrent.ScheduledFutureTask<V>)`方法，这里在添加定时任务时，如果是非`EventLoop`线程调用，则会发起一个异步调用，最终往`scheduledTaskQueue`添加定时任务的还是`EventLoop`线程。所以呢，这里又有另一个优化，那就是可以延迟初始化`scheduledTaskQueue`。

## 4 总结

画重点来了，本文的重点就这两个。

- 每个`EventLoop`中有3个队列，分别是`tailTasks`、`taskQueue`和`scheduledTaskQueue`。而且`tailTasks`和`taskQueue`队列在`NioEventLoop`中的被优化为`MultiProducerSingleConsumerQueue`。
- netty对`selector`中的`selectedKeys`做了优化，从`HashSet`替换为`SelectedSelectionKeySet`，`SelectedSelectionKeySet`是用数组实现的`Set`，添加元素和遍历效率更高。

