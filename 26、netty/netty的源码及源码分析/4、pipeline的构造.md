## pipeline的构造



## 1 和Pipeline相关的其他组件

#### 1.1 ChannnelHandler

这是`ChannelHandler`中的注释，翻译过来就是“处理IO事件或者拦截IO操作，并且将其向`ChannelPipeline`中的下一个`handler`传递”，说白了就是在责任链中注册的一系列回调方法。

> Handles an I/O event or intercepts an I/O operation, and forwards it to its next handler in
>  its ChannelPipeline

这里的`I/O event`就是很多书中提到的“入站事件”，而`I/O operation`就是很多书中提到的“出站事件”，前面我说过，这里我并不准备这么叫，按我的理解我习惯把这两者称之为“事件”和“命令”。很显然这里`event`和`operation`的含义是不一样的，`event`更多地多表示事件发生了，我们被动地收到，而`operation`则表示我们主动地发起一个动作或者命令。

#### 1.2 ChannelHandlerContext

每一个`ChannelHandler`在被添加进`ChannelPipeline`时会被包装进一个`ChannelHandlerContext`。有两个特殊的`ChannelHandlerContext`除外，分别是`HeadContext`和`TailContext`，`HeadContext`继承了`ChannelInBoundHandler`和`ChannelOutBoundHandler`，而`TailContext`继承了`ChannelInBoundHandler`。
 每个`ChannelHandlerContext`中有两个指针`next`和`prev`，这是用来将多个`ChannelHandlerContext`构成双向链表的。

## 2 Pipeline的构造方法

我们以`DefaultChannelPipeline`为例，从它的构造方法开始。这里首先将`Channel`保存到`Pipeline`的属性中，又初始化了两个属性`succeedFuture`和`voidPromise`。这是两个特殊的可以共享的`Promise`，这两个`Promise`不是重点，不理解也没关系。

接下来的`tail`和`head`是两个特殊的`ChannelHandlerContext`，这两个是`Pipeline`中的重要组件。



```csharp
protected DefaultChannelPipeline(Channel channel) {
    this.channel = ObjectUtil.checkNotNull(channel, "channel");
    succeededFuture = new SucceededChannelFuture(channel, null);
    voidPromise =  new VoidChannelPromise(channel, true);

    tail = new TailContext(this);
    head = new HeadContext(this);

    head.next = tail;
    tail.prev = head;
}
```

Pipeline在执行完构造方法以后的结构如下图所示，`head`和`tail`构成了最简单的双向链表。

![img](https:////upload-images.jianshu.io/upload_images/26213884-7eddcbf69179132f.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

Pipeline的初始化



图中蓝色填充的就是`ChannelHandlerContext`，目前只有`HeadContext`和`TailContext`，`ChannelHandlerContext`中的较窄的矩形表示`ChannelHandler`，由于`HeadContext`和`TailContext`并没有包含`ChannelHandler`，而是继承`ChannelHandler`，所以这里我们用虚线表示。上下贯通的`ChannelHandler`表示既是`ChannelInBoundHandler`又是`ChannelOutBoundHandler`，只有上半部分的表示是`ChannelInBoundHandler`，只有下半部分的表示是`ChannelOutBoundHandler`。

## 3 添加ChannelHandler

在`ChannelPipeline`中有很多以`add`开头的方法，这些方法就是向`ChannelPipeline`中添加`ChannelHandler`的方法。

- `addAfter`：向某个`ChannelHandler`后边添加
- `addBefore`：向某个`ChannelHandler`前面添加
- `addFirst`：添加到头部，不能在`head`的前面，而是紧挨着`head`，在`head`的后面
- `addLast`：添加到尾部，不能在`tail`的后面，而是紧挨着`tail`，在`tail`的前面

我们以最常用的的`addLast`方法为例来分析一下`Pipeline`中添加`ChannelHandler`的操作。
 这里所贴出的`addLast`方法其实我们已经在“服务端启动流程”这篇文章中打过照面了。方法参数中的`EventExecutorGroup`意味着我们可以为这个`ChannelHandler`单独设置`Excutor`而不使用`Channel`所绑定的`EventLoop`，一般情况下我们不这么做，所以`group`参数为`null`。

这里先把`ChannelHandler`包装成`ChannelHandlerContext`，再添加到尾部，随后调用`ChannelHandler`的`HandlerAdded`方法。

在调用`HandlerAdded`方法时有一点问题，添加`ChannelHandler`的操作不需要在`EventLoop`线程中进行，而`HandlerAdded`方法则必须在`EventLoop`线程中进行。也就是说存在添加`Handler`时还未绑定`EventLoop`的情况，此时则调用`newCtx.setAddPending()`将当前`HandlerContext`设置为`ADD_PENDING`状态，并且调用`callHandlerCallbackLater(newCtx, true)`将一个异步任务添加到一个单向队链表中，即`pendingHandlerCallbackHead`这个链表。

如果当前已经绑定了`EventLoop`，则看当前调用线程是否为`EventLoop`线程，如果不是则向`EventLoop`提交一个异步任务调用`callHandlerAdded0`方法，否则直接调用`callHandlerAdded0`方法。

下面咱们依次分析一下`newContext`，`callHandlerCallbackLater`和`callHandlerAdd0`方法。



```java
@Override
    public final ChannelPipeline addLast(EventExecutorGroup group, String name, ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
           //先把`handler`包装成`HandlerContext`
            newCtx = newContext(group, filterName(name, handler), handler);
            //添加到尾部
            addLast0(newCtx);
            //如果还未绑定`EventLoop`则稍后再发起对`HandlerAdded`方法的调用。
            if (!registered) {
                newCtx.setAddPending();
                callHandlerCallbackLater(newCtx, true);
                return this;
            }
            //如果已经绑定了EventLoop，并且当前线程非EventLoop线程的话就提交一个异步任务，就发起一个异步任务去调用HandlerAdded方法。
            EventExecutor executor = newCtx.executor();
            if (!executor.inEventLoop()) {
                newCtx.setAddPending();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callHandlerAdded0(newCtx);
                    }
                });
                return this;
            }
        }
        //如果当前线程是EventLoop线程，就直接调用HandlerAdded方法。
        callHandlerAdded0(newCtx);
        return this;
    }
```

#### 3.1 newContext

先看来一下`newContext`方法，这里直接调用了`DefaultChannelHandlerContext`的构造方法，咱们跟进去看看。



```java
private AbstractChannelHandlerContext newContext(EventExecutorGroup group, String name, ChannelHandler handler) {
    return new DefaultChannelHandlerContext(this, childExecutor(group), name, handler);
}
```

在`DefaultChannelHandlerContext`的构造方法中又调用了父类`AbstractChannelHandlerContext`的构造方法，保存了`handler`属性。在调用父类构造方法之前调用了`isInboud`和`isOutbound`方法判断当前的`Handler`是否为`ChannelInBoundHandler`或者`ChannelOutBoundHandler`，这两个方法很简单，不再展开。



```java
DefaultChannelHandlerContext(
        DefaultChannelPipeline pipeline, EventExecutor executor, String name, ChannelHandler handler) {
    super(pipeline, executor, name, isInbound(handler), isOutbound(handler));
    if (handler == null) {
        throw new NullPointerException("handler");
    }
    this.handler = handler;
}
```

接下来看`AbstractChannelHandlerContext`的构造方法，这里非常简单，保存了几个属性，咱们看一下`ordered`这个属性。`ordered`表示`EventExecutor`在执行异步任务时是否按添加顺序执行，这里一般情况下`executor`为`null`，表示使用`Channel`所绑定的`EventLoop`线程，而`EventLoop`线程都是`OrderedEventExecutor`的实现类。所以这里我们不考虑`ordered`为`false`的情况。



```java
AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name,
                              boolean inbound, boolean outbound) {
    this.name = ObjectUtil.checkNotNull(name, "name");
    this.pipeline = pipeline;
    this.executor = executor;
    this.inbound = inbound;
    this.outbound = outbound;
    // Its ordered if its driven by the EventLoop or the given Executor is an instanceof OrderedEventExecutor.
    ordered = executor == null || executor instanceof OrderedEventExecutor;
}
```

上面提到了`ChannelHandlerContext`可以在构造方法里单独指定`EventExecutor`，如果没有单独指定的话就使用`Channel`所绑定的`EventLoop`，代码在哪里呢，就在`AbstractChannelHandlerContext#executor`方法，非常简单，如果没有为当前`ChannelHandler`指定`excutor`则返回`Channel`所绑定的`EventLoop`。



```java
@Override
public EventExecutor executor() {
    if (executor == null) {
        return channel().eventLoop();
    } else {
        return executor;
    }
}
```

#### 3.2 callHandlerCallbackLater

在添加完`ChannelHandler`之后将调用`ChannledHandler`的`handlerAdded`方法，但是此时有可能还未绑定`EventLoop`，而`handlerAdded`方法的调用必须在`EventLoop`线程内执行，此时就需要调用`callHandlerCallbackLater`方法在`pendingHandlerCallbackHead`链表中添加一个`PendingHandlerAddedTask`。



```java
private void callHandlerCallbackLater(AbstractChannelHandlerContext ctx, boolean added) {
    assert !registered;

    PendingHandlerCallback task = added ? new PendingHandlerAddedTask(ctx) : new PendingHandlerRemovedTask(ctx);
    PendingHandlerCallback pending = pendingHandlerCallbackHead;
    if (pending == null) {
        pendingHandlerCallbackHead = task;
    } else {
        // Find the tail of the linked-list.
        while (pending.next != null) {
            pending = pending.next;
        }
        pending.next = task;
    }
}
```

接下来咱们看一下`PendingHandlerAddedTask`的代码，逻辑在`execute`方法里，这里直接调用了`callHandlerAdded0`。



```java
private final class PendingHandlerAddedTask extends PendingHandlerCallback {

    PendingHandlerAddedTask(AbstractChannelHandlerContext ctx) {
        super(ctx);
    }

    @Override
    public void run() {
        callHandlerAdded0(ctx);
    }

    @Override
    void execute() {
        EventExecutor executor = ctx.executor();
        if (executor.inEventLoop()) {
            callHandlerAdded0(ctx);
        } else {
            try {
                executor.execute(this);
            } catch (RejectedExecutionException e) {
                
            }
        }
    }
}
```

#### 3.3 callHandlerAdded0

不管是在未绑定`EventLoop`的情况下延迟调用`handlerAdded`还是在已经绑定了`EventLoop`的情况下立即调用`HandlerAdded`，最终都会调用到`callHandlerAdded0`方法。这里干了两件事，一是调用`ChannelHandler`的`handlerAdded`方法，二是将`HandlerContext`的状态设置为`ADD_COMPLETE`状态。



```java
private void callHandlerAdded0(final AbstractChannelHandlerContext ctx) {
    try {
        ctx.handler().handlerAdded(ctx);
        ctx.setAddComplete();
    } catch (Throwable t) {
           
}
```

#### 3.4 添加多个ChannelHandler后的Pipeline

还记得咱们的“Netty整体架构图”吗，在这里咱们把`Pipeline`部分单独放大拿出来看一下，在添加完多个`ChannelHandler`之后，`Pipeline`的结构是这样的。

![img](https:////upload-images.jianshu.io/upload_images/26213884-69e7a0ee58a845fd.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

多ChannelHandler的Pipeline



## 4 删除ChannelHandler

在`Pipeline`中有几个以`remove`开头的方法，这些方法的作用就是删除`ChannelHandler`。

- `remove(ChannelHandler handler)`：从`head`向`tail`查找，用`==`判断是否为同一实例，只删除第1个。
- `remove(Class<T> handlerType)`：从`head`向`tail`查找，用`isAssignableFrom`方法判断是否为符合条件的类型，只删除第1个。
- `remove(String name)`：从`head`向`tail`查找，用`name`精确匹配查找，只删除第1个，因为`name`不能重复，所以这里删除第1个也是唯一的1个。
- `removeFirst`：删除`head`的后一个，不能删除`tail`。
- `removeLast`：删除`tail`的前一个，不能删除`head`。

上述无论哪种删除方式在查找到对应的`HandlerContext`后都会调用到`remove(final AbstractChannelHandlerContext ctx)`方法，查找过程比较简单，咱们不再展开，直接看`remove(final AbstractChannelHandlerContext ctx)`方法。

看看这个方法的实现，是不是和`addLast(EventExecutorGroup group, String name, ChannelHandler handler)`很相似，非常相似。首先从双向链表中删除`ChannelHandlerContext`，再调用`callHandlerRemoved0`方法，`callHandlerRemoved0`方法内会调用`handlerRemoved`方法，这个调用必须在`EventLoop`线程内进行。如果删除时还未绑定`EventLoop`则添加一个异步任务到链表`pendingHandlerCallbackHead`中。

如果已经绑定了`EventLoop`并且当前线程非`EventLoop`线程则向`EventLoop`提交一个异步任务，否则直接调用`callHandlerRemoved0`方法。



```java
private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
    synchronized (this) {
        //从双向链表中删除`ChannelHandlerContext`
        remove0(ctx);
        
        //如果还未绑定`EventLoop`，则稍后调用`handlerRemoved`方法
        if (!registered) {
            callHandlerCallbackLater(ctx, false);
            return ctx;
        }
        //如果已经绑定了`EventLoop`，但是当前线程非`EventLoop`线程的话，就发起一个异步任务调用callHandlerRemoved0方法
        EventExecutor executor = ctx.executor();
        if (!executor.inEventLoop()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    callHandlerRemoved0(ctx);
                }
            });
            return ctx;
        }
    }
    //如果当前线程就是`EventLoop`线程，则直接调用callHandlerRemoved0方法。
    callHandlerRemoved0(ctx);
    return ctx;
}
```

`callHandlerCallbackLater`方法咱们前面已经分析过，和添加`ChannelHandler`时不同的是，这里向链表添加的是`PendingHandlerRemovedTask`，这个类也很简单，不再展开。

这里咱们只看一下`callHandlerRemoved0`方法。这个方法很简单，调用`handlerRemoved`方法，再把`ChannelHandlerContext`的状态设置为`REMOVE_COMPLETE`。



```java
private void callHandlerRemoved0(final AbstractChannelHandlerContext ctx) {
    // Notify the complete removal.
    try {
        try {
            ctx.handler().handlerRemoved(ctx);
        } finally {
            ctx.setRemoved();
        }
    } catch (Throwable t) {
        fireExceptionCaught(new ChannelPipelineException(
                ctx.handler().getClass().getName() + ".handlerRemoved() has thrown an exception.", t));
    }
}
```

## 4 pendingHandlerCallbackHead链表中的任务什么时候调用

在`AbstractUnsafe`的`register0`方法中，在绑定`EventLoop`以后，会调用`pipeline.invokeHandlerAddedIfNeeded()`方法，我们看一下`pipeline.invokeHandlerAddedIfNeeded()`方法。



```java
private void register0(ChannelPromise promise) {
try {
    
    // 去完成那些在绑定EventLoop之前触发的添加handler操作，这些操作被放在pipeline中的pendingHandlerCallbackHead中，是个链表
    pipeline.invokeHandlerAddedIfNeeded();
    
}
```

`invokeHandlerAddedIfNeeded`方法调用了`callHandlerAddedForAllHandlers`方法，咱们接着看下去。



```java
final void invokeHandlerAddedIfNeeded() {
    assert channel.eventLoop().inEventLoop();
    if (firstRegistration) {
        firstRegistration = false;

        callHandlerAddedForAllHandlers();
    }
}
```

`callHandlerAddedForAllHandlers`方法的逻辑咱就不再展开来说了，非常简单，就是遍历`pendingHandlerCallbackHead`这个单向链表，依次调用每个元素的`execute`方法，并且清空这个单向链表。



```java
private void callHandlerAddedForAllHandlers() {
    final PendingHandlerCallback pendingHandlerCallbackHead;
    synchronized (this) {
        assert !registered;

        registered = true;

        pendingHandlerCallbackHead = this.pendingHandlerCallbackHead;

        this.pendingHandlerCallbackHead = null;
    }

    PendingHandlerCallback task = pendingHandlerCallbackHead;
    while (task != null) {
        task.execute();
        task = task.next;
    }
}
```

## 5 总结

`Pipeline`中的最重要的数据结构就是由多个`ChannelHandlerContext`组成的双向链表，而每个`ChannelHandlerContext`中包含一个`ChannelHandler`，`ChannelHandler`既可以添加也可以删除。在`Pipeline`中有两个特殊的`ChannelHandlerContext`分别是`HeadContext`及`TailContext`，这两个`ChannelHandlerContext`中不包含`ChannelHandler`，而是采用继承的方式。`HeadContext`实现了`ChannelOutBoundHandler`和`ChannelInBoundHandler`，而`TailContext`实现了`ChannelInBoundHandler`。