## 1 EventLoop线程的创建时机

在netty的启动过程中，如何将`Channel`注册到`EventLoop`上。我们看`AbstractChannel`的`register`方法，其中有`eventLoop.execute`调用，是不是很熟悉。**大多数情况下（并非绝对），这里就是`EventLoop`线程开始的地方**。

```java
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    if (eventLoop.inEventLoop()) {
        register0(promise);
    } else {
        try {
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    register0(promise);
                }
            });
        } catch (Throwable t) {

        }
    }
}
```

咱们跟进去`execute`方法，这个方法的实现在`SingleThreadEventExecutor`中，我们看到这里有一个`startThread`方法，看名字就知道，这里是线程真正开始的地方，一起来看看吧。

后面的`!addTaskWakesUp && wakesUpForTask(task)`是怎么回事呢？

`EventLoop`的实现中，有的`EventLoop`实现会阻塞在任务队列上，对于这样的`EventLoop`唤醒方法是向任务队列中添加一个比较特殊的任务，这样的`EventLoop`中`addTaskWakesUp`为`ture`。

而有的`EventLoop`比如`NioEventLoop`不会阻塞在任务队列上，但是会阻塞在`selector`上，对于这样的`EventLoop`通过调用`wakeup`方法唤醒，这样的`EventLoop`中`addTaskWakesUp`为`false`。



```java
 public void execute(Runnable task) {
        boolean inEventLoop = inEventLoop();
        if (inEventLoop) {
            addTask(task);
        } else {
            startThread();
            addTask(task);
            if (isShutdown() && removeTask(task)) {
                reject();
            }
        }
        
        if (!addTaskWakesUp && wakesUpForTask(task)) {
            wakeup(inEventLoop);
        }
    }
```

我们看看`SingleThreadEventExecutor`中的`wakeup`方法，这里通过向队列中添加一个特殊的`task`来唤醒`EventLoop`线程。



```csharp
protected void wakeup(boolean inEventLoop) {
    if (!inEventLoop || STATE_UPDATER.get(this) == ST_SHUTTING_DOWN) {
        taskQueue.offer(WAKEUP_TASK);
    }
}
```

而`NioEventLoop`中覆盖了这个`wakeup`方法，通过调用`selector.wakeup`方法来唤醒`EventLoop`线程，因为`wakeup`是个重量级操作，所以netty用了一个`AtomicBoolean`类型的`wakenUp`变量来减少`wakeup`的次数，如果已经被`wakeup`了，就不再调用`selector.wakeup`。



```java
protected void wakeup(boolean inEventLoop) {
    if (!inEventLoop && wakenUp.compareAndSet(false, true)) {
        selector.wakeup();
    }
}
```

`startThread`方法中首先对`EventLoop`的状态做了判断，如果为`ST_NOT_STARTED`（未开始）状态，才调用`doStartThread`方法，接着跟下去看`doStartThread`方法。



```java
private void startThread() {
    if (STATE_UPDATER.get(this) == ST_NOT_STARTED) {
        if (STATE_UPDATER.compareAndSet(this, ST_NOT_STARTED, ST_STARTED)) {
            doStartThread();
        }
    }
}
```

`doStartThread`接着调用了`SingleThreadEventExecutor.this.run()`方法，这个`run`方法是抽象的，在这里没有实现。我们重点关注`NioEventLoop`，所以我们去看`NioEventLoop`中`run`方法的实现。

我们前面已经讲过了这个`executor`是`ThreadPerTaskExecutor`，所以这里调用`execute`方法会创建出一个新的线程，这个线程就是`EventLoop`线程。



```java
private void doStartThread() {
    assert thread == null;
    executor.execute(new Runnable() {
        @Override
        public void run() {
            try {
                SingleThreadEventExecutor.this.run();
            } catch (Throwable t) {
            } finally {
               
            }
        }
    });
}
```

## 2 EventLoop线程的工作内容

接下来我们要分析的逻辑中有很多关于`wakenup`的magic操作，我也看不懂，非常难以理解，很多操作我觉得是没有必要的。这些地方在后来的版本中经过一次比较大的重构，逻辑更加清晰了，感兴趣的同学可以看一下这次github上的代码提交，[Clean Up NioEventLoop](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fnetty%2Fnetty%2Fpull%2F9799%2Fcommits%2Ff3721e285ca49049428f32f37c2612559e0a90f2)。下面贴出的代码中已经删除了很多难以理解的，又不影响我们理解整个`EvenLoop`主体逻辑的代码。

`run`方法中是一个死循环，这就是`EventLoop`线程的主要逻辑内容了。

每次循环之前要先调用一下`selectStrategy.calculateStrategy(selectNowSupplier, hasTasks())`方法判断下一步的动作，默认实现在`DefaultSelectStrategy`中。这里如果`hasTasks`为`true`也就是说`taskQueue`中有任务要执行的话，会调用一下`selectSupplier.get()`，这个`selectSupplier`是`NioEventLoop`中的`selectNowSupplier`属性，`get`逻辑非常简单，就是调用一下非阻塞的`selectNow`方法。

`selectStrategy.calculateStrategy`这里的整体逻辑就是，如果当前有任务要执行，就立即调用`selectNow`返回一个>=0的值，这将导致`run`方法直接跳出`switch`去执行下面的逻辑。否则就返回`SelectStrategy#SELECT`，这样`run`方法将进入`select(wakenUp.getAndSet(false))`执行。



```java
public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
    return hasTasks ? selectSupplier.get() : SelectStrategy.SELECT;
}

private final IntSupplier selectNowSupplier = new IntSupplier() {
    @Override
    public int get() throws Exception {
        return selectNow();
    }
};
```

咱们接着看`run`方法，`run`方法中的三个重要操作：

- `select(wakenUp.getAndSet(false))`
- `processSelectedKeys()`
- `runAllTasks()`

`EventLoop`的一生都在为这3件事奔波，咱们一起来看一下。



```java
 protected void run() {
        for (;;) {
            try {
                switch (selectStrategy.calculateStrategy(selectNowSupplier, hasTasks())) {
                    case SelectStrategy.CONTINUE:
                        continue;
                    case SelectStrategy.SELECT:
                        //select操作
                        select(wakenUp.getAndSet(false));

                    default:
                        // fallthrough
                }

                cancelledKeys = 0;
                needsToSelectAgain = false;
                final int ioRatio = this.ioRatio;
                if (ioRatio == 100) {
                    try {
                        //处理Channel事件
                        processSelectedKeys();
                    } finally {
                        // Ensure we always run tasks.
                        //处理队列中的任务
                        runAllTasks();
                    }
                } else {
                    final long ioStartTime = System.nanoTime();
                    try {
                        processSelectedKeys();
                    } finally {
                        // Ensure we always run tasks.
                        final long ioTime = System.nanoTime() - ioStartTime;
                        runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                    }
                }
            } catch (Throwable t) {

            }

        }
    }
```

#### 2.1 select

`select`中也有一个死循环操作，在循环之前首先计算出一个`selectDeadLineNanos`，这是`select`操作的最迟返回时间，是当前时间+下一个定时任务距离现在的时间。

`deleayNanos`方法会到定时任务队列（`EventLoop`的创建这篇文章中的`scheduledTaskQueue`）查看队首的任务距离现在还有多久，如果没有定时任务的话，默认返回1秒。

`timeoutMillis`即`select`操作的超时时间，至于这里为什么加上500微秒，我也觉得很奇怪，没有理解，咱们暂且不去管它，接着往下看。

如果发现`timeoutMillis`<=0说明现在有定时任务要执行了，立即调用非阻塞的`selector.selectNow`方法，并且跳出循环。

咱们接着往下看是阻塞式的`selector.select`操作，如果阻塞期间有任务加入，会调用`wakeup`y方法，这个`select`操作会立即返回。

接下来的代码咱们只关注 `rebuildSelector`，这是为了修复jdk的空轮询bug而设计的，默认如果发生了512次空轮询就重建`selector`。
 `select`循环跳出的条件大致有以下几种情况：

- 有定时任务到期了
- `selector.select(timeoutMillis)`操作返回非0值
- 往`EventLoop`中添加任务时唤醒了阻塞的`selector.select(timeoutMillis)`操作
- 出现了512次空轮询



```java
private void select(boolean oldWakenUp) throws IOException {
    Selector selector = this.selector;
    try {
        int selectCnt = 0;
        long currentTimeNanos = System.nanoTime();
        //select操作的最迟返回时间
        long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);
        for (;;) {
            long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
            if (timeoutMillis <= 0) {
                if (selectCnt == 0) {
                    selector.selectNow();
                    selectCnt = 1;
                }
                break;
            }

            int selectedKeys = selector.select(timeoutMillis);
            selectCnt ++;
            
            if (selectedKeys != 0 || oldWakenUp || wakenUp.get() || hasTasks() || hasScheduledTasks()) {

                break;
            }

            if (time - TimeUnit.MILLISECONDS.toNanos(timeoutMillis) >= currentTimeNanos) {
 
            } else if (SELECTOR_AUTO_REBUILD_THRESHOLD > 0 &&
                    selectCnt >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
                //修复jdk空轮询bug
                rebuildSelector();
                selector = this.selector;

                selector.selectNow();
                selectCnt = 1;
                break;
            }

            currentTimeNanos = time;
        }

    } catch (CancelledKeyException e) {

    }
}
```

#### 2.2 processSelectedKeys

在跳出`select`循环之后又回到了`run`方法，咱们接着看`run`方法剩余的逻辑，首先把`callcedlledKeys`设置为0，并且把`needsToSelectAgain`设置为`false`，这两个是控制对已经取消的`SelectionKey`进行清理的变量，每次调用`selector.select`或者`selector.selectNow`方法都会导致`selector`驱逐出已经被取消的`Selectionkey`，代码执行到这里，因为刚刚执行过`select`或者`selectNow`方法，所以此时肯定不存在被取消的`Selectionkey`（**`SelectionKey`的取消操作必须被`EventLoop`线程执行**）。

接着判断`ioRatio`变量的值，`ioRatio`是表示`EventLoop`处理io事件的时间比例，默认值为50。这个只能大致控制处理io事件的时间和处理异步任务的时间比例，并非绝对值。

这里如果ioRatio等于100，就先处理所有的io事件，再处理所有的任务。
 如果ioRatio不等于100，就先处理所有的io事件，再处理异步任务，此时处理异步任务会有一个超时时间，是根据处理io事件所消耗的时间和ioRatio计算出来的。



```java
cancelledKeys = 0;
needsToSelectAgain = false;
final int ioRatio = this.ioRatio;
if (ioRatio == 100) {
    try {
        //处理Channel事件
        processSelectedKeys();
    } finally {
        // Ensure we always run tasks.
        //处理队列中的任务
        runAllTasks();
    }
} else {
    final long ioStartTime = System.nanoTime();
    try {
        processSelectedKeys();
    } finally {
        // Ensure we always run tasks.
        final long ioTime = System.nanoTime() - ioStartTime;
        runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
    }
}
```

我们先去看`processSelectedKeys`方法，这里首先判断`selectedKeys`是否为`null`，如果不为`null`说明`selectedKeys`被netty优化过，咱们直接去看第1个分支，也就是优化过的分支。



```csharp
private void processSelectedKeys() {
    if (selectedKeys != null) {
        processSelectedKeysOptimized(selectedKeys.flip());
    } else {
        processSelectedKeysPlain(selector.selectedKeys());
    }
}
```

`processSelectedKeysOptimized`方法遍历`SelectionKey`数组，对每个`SelectionKey`调用`processSelectedKey(k, (AbstractNioChannel) a)`方法进行处理。

如果发现被取消的key过多，默认超过256，则清空数组中的剩余元素，重新`select`，重新`select`时`selector`会驱逐已经取消的key。



```java
private void processSelectedKeysOptimized(SelectionKey[] selectedKeys) {
    for (int i = 0;; i ++) {
        final SelectionKey k = selectedKeys[i];
        //key == null说明已经遍历完了
        if (k == null) {
            break;
        }
        //遍历过的元素设置为null
        selectedKeys[i] = null;

        final Object a = k.attachment();

        if (a instanceof AbstractNioChannel) {
            //处理SelectionKey
            processSelectedKey(k, (AbstractNioChannel) a);
        } else {

        }
        //如果发现被取消的Key过多，默认超过256，则清空数组中的剩余元素，重新select
        if (needsToSelectAgain) {
            //清空数组剩余的元素
            for (;;) {
                i++;
                if (selectedKeys[i] == null) {
                    break;
                }
                selectedKeys[i] = null;
            }
            //重新select，则selector会驱逐出已经取消的key
            selectAgain();

            selectedKeys = this.selectedKeys.flip();
            i = -1;
        }
    }
}
```

接下来咱们关注一下`processSelectedKey`方法，这里首先判断一下`Key`是否已经取消了，如果已经取消了，则调用`unsafe.close`关闭`Channel`。接着往下首先判断是否发生了`OP_CONNECT`事件，还记得`NioSocketChannel.doConnect`方法吗，如果`SocketChannel`的`connect`方法返回`false`，还要继续调用`SocketChannel`的`finishConnect`方法`才能真正完全连接，这里咱们在在“客户端的启动流程”这篇文章中已经讲过了，这里不再展开。

接下来是判断是否发生了`OP_WRITE`事件，`OP_WRITE`事件表明`TCP`缓冲区有空间可以写数据，此时调用`unsafe.forceFlush`方法将`AbstractUnsafe#outboundBuffer`中的数据写入到`TCP`缓冲区中。

最后判断如果发生了`OP_READ`或者`OP_ACCEPT`事件，表明`Channel`可读或者有新连接接入，此时调用`unsafe.read`方法读取数据。那么有人有疑问了，`OP_ACCEPT`事件为什么也能调用`unsafe.read`呢，此时并没有数据可以读取啊，这里就是一个特殊之处了，专为`AbstractNioMessageChannel`而设计，而`NioServerSocketChannel`也是`AbstractNioMessageChannel`的子类，感兴趣的同学去看一下`NioMessageUnsafe#read`方法和`NioServerSocketChannel#doReadMessages`方法。



```csharp
private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
    final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
    //key已经取消了
    if (!k.isValid()) {
        unsafe.close(unsafe.voidPromise());
        return;
    }
    try {
        //在调用SocketChannel的connect方法返回false时，这里需要处理OP_CONNECT事件，在unsafe.finishConnect()方法中调用SocketChannel的finishConnect方法
        //参考：io.netty.channel.socket.nio.NioSocketChannel.doConnect
        if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
            // remove OP_CONNECT as otherwise Selector.select(..) will always return without blocking
            // See https://github.com/netty/netty/issues/924
            int ops = k.interestOps();
            ops &= ~SelectionKey.OP_CONNECT;
            k.interestOps(ops);
            unsafe.finishConnect();
        }
        //这个表示当前tcp缓冲区可写
        if ((readyOps & SelectionKey.OP_WRITE) != 0) {
            //将未写入缓冲区的数据flush到缓冲区
            ch.unsafe().forceFlush();
        }
        //OP_ACCEPT专为`AbstractNioMessageChannel`而设计
        if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
            unsafe.read();
        }
    } catch (CancelledKeyException ignored) {
        unsafe.close(unsafe.voidPromise());
    }
}
```

至此`run`方法中，`processSelectedKeys`咱们已经分析完了，接下来看另外两个调用，`runAllTasks()`和`runAllTasks(long timeoutNanos)`，这两个方法主体逻辑差不多，只不过其中一个带有超时时间控制。

#### 2.3 runAllTasks

咱们先看`runAllTasks()`方法，这里首先调用`fetchFromScheduledTaskQueue()`从`scheduledTaskQueue`中将到期的定时任务拉到`taskQueue`队列中，再调用`runAllTasksFrom(taskQueue)`将`taskQueue`队列中的所有任务执行完毕，最后调用`afterRunningAllTasks()`方法执行完所有`tailTasks`队列中的任务。这里用到了咱们在“EventLoop的构造”这篇中提到的3个重要的队列。

咱们还没说过`tailTasks`这个队列有什么用，其实我也还没发现它有什么用，有人说它可以用来统计`run`方法每次循环的时间。好吧，反正它不是重点，不必纠结。

`fetchFromScheduledTaskQueue()`、`runAllTasksFrom(taskQueue)`和`afterRunningAllTasks()`这3个方法比较简单，咱们不再展开。



```java
protected boolean runAllTasks() {
    assert inEventLoop();
    boolean fetchedAll;
    boolean ranAtLeastOne = false;

    do {
        //从`scheduledTaskQueue`中将到期的定时任务拉到`taskQueue`队列中
        fetchedAll = fetchFromScheduledTaskQueue();
        //将taskQueue中的所有任务执行完毕
        if (runAllTasksFrom(taskQueue)) {
            ranAtLeastOne = true;
        }
    } while (!fetchedAll); // keep on processing until we fetched all scheduled tasks.

    if (ranAtLeastOne) {
        lastExecutionTime = ScheduledFutureTask.nanoTime();
    }
    //将tailTasks中的所有任务执行完毕
    afterRunningAllTasks();
    return ranAtLeastOne;
}
```

接着看`runAllTasks(long timeoutNanos)`方法，这个方法咱们不多展开讲了，与`runAllTasks()`的区别就在于多了一个超时时间，Netty这里对超时的判断做了一些优化，因为`System.nanoTime`是一个很重的操作，所以这里并不是每执行一个任务就判断一下是否超时，而是每执行64个任务判断一下是否超时。



```java
protected boolean runAllTasks(long timeoutNanos) {
        //从scheduledTaskQueue将所有到期的任务拉到taskQueue中
        fetchFromScheduledTaskQueue();
        Runnable task = pollTask();
        if (task == null) {
            afterRunningAllTasks();
            return false;
        }
        //ScheduledFutureTask.nanoTime()是一个从ScheduledFutureTask的类加载开始的一个相对时间
        final long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
        long runTasks = 0;
        long lastExecutionTime;
        //遍历taskQueue执行任务，因为natoTime操作很重，所以每64次任务判断一次是否超过了超时时间
        for (; ; ) {
            safeExecute(task);

            runTasks++;

            if ((runTasks & 0x3F) == 0) {
                lastExecutionTime = ScheduledFutureTask.nanoTime();
                if (lastExecutionTime >= deadline) {
                    break;
                }
            }

            task = pollTask();
            if (task == null) {
                lastExecutionTime = ScheduledFutureTask.nanoTime();
                break;
            }
        }
        //执行所有tailTasks中的任务
        afterRunningAllTasks();
        this.lastExecutionTime = lastExecutionTime;
        return true;
    }
```

## 3 总结

`EventLoop`的一生就是一个死循环，这个死循环中每次循环它干了3件事。

- `select`：选出有兴趣事件发生的`Channel`
- `processSelectedKeys`：处理`Channel`上发生的io事件
- `runAllTasks`：执行异步任务，包括`scheduledTaskQueue`、`taskQueue`和`tailTasks`这3个队列中的任务。