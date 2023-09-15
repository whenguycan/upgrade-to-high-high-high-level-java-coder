## netty服务端启动流程



## 1 服务端引导代码

以下代码引导启动一个服务端，在以下文章中我们以“引导代码”指代这段程序。这段代码很简单，创建两个`EventLoopGroup`分别为`bossGroup`和`workerGroup`。创建一个`ServerBoostrap`并将`bossGroup`和`workerGroup`传入，配置一个`handler`，该`handler`为监听端口这条连接所使用的`handler`。接着又设置了一个`childHandler`即新连接所使用的`handler`，本篇文章我们不讲新连接的接入，所以这里的`childHandler`里什么也没做。

运行这段这段代码将在控制台打出如下结果。

> HandlerAdded
>  ChannelRegistered
>  ChannelActive

```java
/**
 * 
 */
public class ServerBoot {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .attr(AttributeKey.valueOf("ChannelName"), "ServerChannel")
                    .handler(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("ChannelRegistered");
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("ChannelActive");
                        }

                        @Override
                        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("HandlerAdded");
                        }
                    }).childHandler(new ChannelInboundHandlerAdapter(){

            });
            ChannelFuture f = b.bind(8000).sync();
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

## 2 启动过程

我们从`ChannelFuture f = b.bind(8000).sync()`的`bind`方法往下跟到`AbstractBootStrap`的`doBind`方法，这中间的过程很简单，就是将端口号封装为`SocketAddress`。

在`doBind`内的关键代码有第一行的`initAddRegister`方法，还有后面的`doBind0`方法。

```java
private ChannelFuture doBind(final SocketAddress localAddress) {
        final ChannelFuture regFuture = initAndRegister();
        if (regFuture.isDone()) {
             doBind0(regFuture, channel, localAddress, promise);
        } else {
            
            final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
            regFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Throwable cause = future.cause();
                    if (cause != null) {
                    } else {
                        doBind0(regFuture, channel, localAddress, promise);
                    }
                }
            });
            return promise;
        }
}
```

进入到`initAndRegister`方法，`initAddResgiter`方法中有3个关键步骤，1是`channelFactory.newChannel()`，2是`init(channel)`，3是`config().group().register(channel)`。

```java
final ChannelFuture initAndRegister() {
        Channel channel = null;
        try {
            //创建一个Channel
            channel = channelFactory.newChannel();
            //初始化Channel
            init(channel);
        } catch (Throwable t) {
           
        }
        //注册Channel
        ChannelFuture regFuture = config().group().register(channel);
        ...
}
```

整个`doBind`方法被分成4个关键步骤，分别是：

1. `channelFacotry.newChannel()`
2. `init(channel)`
3. `config().group().register(channel)`
4. `doBind0`

接下来咱们分别来看这4个关键步骤。

#### 2.1 `channelFacotry.newChannel()` 新创建一个Channel

`channelFacotry`是`AbstractBootStrap`的一个属性，这个属性在哪里被赋值呢，其实是在我们在启动时调用`b.channel(NioServerSocketChannel)`时赋的值，这个方法在`AbstractBootStrap`里，非常简单，我们不再分析。最后的结果是`channelFactory`被赋值为`ReflectiveChannelFactory`，顾名思义就是用反射的方法创建`Channel`，我们看一下其中的`newChannel()`方法，很简单，`clazz.newInstance`调用无参构造方法创建实例。

```java
public class ReflectiveChannelFactory<T extends Channel> implements ChannelFactory<T> {
    private final Class<? extends T> clazz;

    @Override
    public T newChannel() {
        try {
            return clazz.newInstance();
        } catch (Throwable t) {
            throw new ChannelException("Unable to create Channel from class " + clazz, t);
        }
    }
}
```

接下来咱们就看一下`NioServerSocketChannel`的无参构造方法，其中调用`newSocket`方法创建了一个jdk的`ServerSocketChannel`。好了，咱们已经看到了**导读中提到的第1步“创建一个ServerSocketChannel”**，紧着把这个`channel`传递给了父类的构造方法，**还传递一个参数`SelectionKey.OP_ACCEPT`，记住这个参数后面会提到**。

```java
public NioServerSocketChannel() {
        this(newSocket(DEFAULT_SELECTOR_PROVIDER));
}

private static ServerSocketChannel newSocket(SelectorProvider provider) {
    try {
        return provider.openServerSocketChannel();
    } catch (IOException e) {

    }
}

public NioServerSocketChannel(ServerSocketChannel channel) {
    super(null, channel, SelectionKey.OP_ACCEPT);
    config = new NioServerSocketChannelConfig(this, javaChannel().socket());
}
```

咱们接着跟到父类`AbstractNioMessageChannel`的构造方法，没什么其他操作，继续调用父类的构造方法。

```java
protected AbstractNioMessageChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
    super(parent, ch, readInterestOp);
}
```

接着跟下去，到了`AbstractNioChannel`的构造方法，在这里我们看到了`ch.configureBlocking(false)`，至此我们看到了**导读中提到的第2步“将Channel设置为非阻塞的”**。`AbstractNioChannel`里又调用了父类的构造方法，接着看下去。

```java
    protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent);
        this.ch = ch;
        this.readInterestOp = readInterestOp;
        try {
            //将Channel设置为非阻塞的
            ch.configureBlocking(false);
        } catch (IOException e) {
            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }
```

到了`AbstractChannel`的构造方法，这里为`Channel`创建了一个id，一个`Unsafe`还有一个`PipeLine`。`Unsafe`和`PipeLine`咱们后面再讲。

```csharp
protected AbstractChannel(Channel parent) {
    this.parent = parent;
    id = newId();
    unsafe = newUnsafe();
    pipeline = newChannelPipeline();
}
```

#### 2.2 `init(channel)` 初始化Channel

我们回到`AbstractBootstrap`的`initAndRegister`方法，接着往下看`init(channel)`，这是个抽象方法，实现在`ServerBootstrap`里。

`init`方法的主要逻辑是设置`Channel`参数、属性，并将我们在引导代码中所配置的`Handler`添加进去，最后又添加了一个`ServerBootStrapAccptor`，顾名思义这是一个处理新连接接入的`Handler`。

这个`ServerBootStrapAccptor`在随后的章节中我们会讲，这里先略过。至于为什么调用`ch.eventLoop().execute`而不是直接添加，这个我在代码里有简要提示，其实目前的版本，直接添加也是没有问题的。

```java
void init(Channel channel) throws Exception {
        //设置Channel参数，我们在引导代码中通过.option(ChannelOption.TCP_NODELAY, true)所设置的参数
        final Map<ChannelOption<?>, Object> options = options0();
        synchronized (options) {
            channel.config().setOptions(options);
        }
        //设置Channel属性，我们在引导代码中通过.attr(AttributeKey.valueOf("ChannelName"), "ServerChannel")所设置的属性
        final Map<AttributeKey<?>, Object> attrs = attrs0();
        synchronized (attrs) {
            for (Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
                @SuppressWarnings("unchecked")
                AttributeKey<Object> key = (AttributeKey<Object>) e.getKey();
                channel.attr(key).set(e.getValue());
            }
        }

        ChannelPipeline p = channel.pipeline();

        //p.addLast是同步调用，不管是不是EventLoop线程在执行，这个匿名的ChannelInitializer被立即添加进PipeLine中
        //但是这个匿名的ChannelInitializer的initChannel方法是被channelAdded方法调用的，而channelAdded方法只能被EventLoop线程调用，但是此时这个Channel还没绑定EventLoop线程，所以这个匿名的ChannelInitializer的channelAdded方法的调用会被封装成异步任务添加到PipeLine的pendingHandlerCallback链表中
        //当Channel绑定EventLoop以后会从pendingHandlerCallback链表中取出任务执行。
        p.addLast(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                ChannelHandler handler = config.handler();
                if (handler != null) {
                    //添加我们在引导代码中所配置的handler
                    pipeline.addLast(handler);
                }

                //有些同学对这个有疑问，为什么不直接pipeline.addLast，可以参考下面的issue，其实现在的版本已经可以直接改成pipeline.addLast
                //issue链接https://github.com/netty/netty/issues/5566
                //为什么现在的版本可以直接改成pipeline.adLast呢，关键在于ChannelInitializer的handlerAdded方法
                //大家可以对比4.0.39.Final版本和4.1.6.Final版本的区别
                //添加ServerBootStrapAcceptor
                ch.eventLoop().execute(new Runnable() {
                    @Override
                    public void run() {
                        pipeline.addLast(new ServerBootstrapAcceptor(
                                currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
                    }
                });
            }
        });
    }
```

#### 2.3 `config().group().register(channel)`绑定`EventLoop`并向`Selector`注册Channel

我们回到`AbstractBootstrap`的`initAndRegister`方法，接着往下看到`ChannelFuture regFuture = config().group().register(channel);`，这里就是注册Channel的地方了，咱们跟进去看看。

`config.group()`的返回是我们在引导代码中所设置的`bossGroup`，由于这里只有一个`Channel`，所以`bossEventLoopGroup`里面只需要1个`EventLoop`就够了。

跟到`register(channel)`方法里看看，这个`register`方法是抽象的，具体实现在`MultithreadEventLoopGroup`中，跟进去。

```java
@Override
public ChannelFuture register(Channel channel) {
    return next().register(channel);
}
```

`next()`方法调用`EventExecutorChooser`的`next()`方法选择一个`EventLoop`。`EventExecutorChooser`有两个实现，分别是`PowerOfTowEventExecutorChooser`和`GenericEventExecutorChooser`，这两个`Chooser`用的都是轮询策略，只是轮询算法不一样。如果`EventLoopGroup`内的`EventLoop`个数是2的幂，则用`PowerOfTowEventExecutorChooser`，否则用`GenericEventExecutorChooser`。

`PowerOfTowEventExecutorChooser`使用位操作。

```java
@Override
public EventExecutor next() {
    return executors[idx.getAndIncrement() & executors.length - 1];
}
```

而`GenericEventExecutorChooser`使用取余操作。

```java
@Override
public EventExecutor next() {
    return executors[Math.abs(idx.getAndIncrement() % executors.length)];
}
```

从`EventLoop`的选择算法上我们可以看出，**netty为了性能，无所不用其极**。

`chooser`属性的赋值在`MultithreadEventExecutorGroup`的构造方法内通过`chooserFactory`创建的。

```cpp
protected MultithreadEventExecutorGroup(int nThreads, Executor executor,
                                        EventExecutorChooserFactory chooserFactory, Object... args) {
    chooser = chooserFactory.newChooser(children);
}
```

而`chooserFactory`的赋值在`MultithreadEventExecutorGroup`的另一个构造方法内。当我们在引导代码中通过`new NioEventLoopGroup(1)`创建`EventLoopGroup`时最终会调用到这个构造方法内，默认值为`DefaultEventExecutorChooserFactory.INSTANCE`。

```cpp
protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
    this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
}
```

`next()`方法选出的`EventLoop`就是个`SingleThreadEventLoop`了，我们跟到`SingleThreadEventLoop`的`register`方法，最终调用的是`unsafe`的`register`方法。

```java
@Override
public ChannelFuture register(Channel channel) {
    return register(new DefaultChannelPromise(channel, this));
}

@Override
public ChannelFuture register(final ChannelPromise promise) {
    ObjectUtil.checkNotNull(promise, "promise");
    promise.channel().unsafe().register(this, promise);
    return promise;
}
```

`unsafe.register`方法在`io.netty.channel.AbstractChannel.AbstractUnsafe`内，我们跟下去看看。在`register`方法中最主要的有两件事，一是绑定`eventloop`，二是调用`register0`方法。此时的调用线程不是`EventLoop`线程，会发起一个异步任务。

```java
 @Override
public final void register(EventLoop eventLoop, final ChannelPromise promise) {
    //绑定eventloop
    AbstractChannel.this.eventLoop = eventLoop;
    
    if (eventLoop.inEventLoop()) {
        register0(promise);
        //此时我们不在EventLoop内，也就是当前线程非EventLoop线程，不会走到这个分支
    } else {
        try {
            eventLoop.execute(new Runnable() {
                @Override
                public void run() {
                    //调用子类的register0方法
                    register0(promise);
                }
            });
        } catch (Throwable t) {
           
        }
    }
}
        
```

`register0`方法内主要有3步操作。

第1步是`doRegister()`，这个咱们稍后说。

第2步是`pipeline.invokeHandlerAddedIfNeeded()`这一步是去完成那些在绑定`EventLoop`之前触发的添加`handler`操作，比如我们添加了一个`ChannelInitializer`，在`ChannelInitalizer`的`initChannel`方法中添加的`Handler`，而`initChannel`被`channelAdded`方法调用，`channelAdded`方法的调用必须在`EventLoop`内，未绑定`EventLoop`之前这个调用会被封装成异步任务。

这些操作被放在`pipeline`中的`pendingHandlerCallbackHead`中，是个双向链表，具体请参考`DefaultChannelPipeLine`的`addLast(EventExecutorGroup group, String name, ChannelHandler handler)`方法。

这一步调用了咱们的引导程序中的`System.out.println("HandlerAdded")`，在控制台打出`"HandlerAdded"`。

第3步触发`ChannelRegistered`事件。这一步调用了咱们的引导程序中的`System.out.println("ChannelRegistered")`，在控制台打出`"ChannelRegistered"`。

好了，到这里我们已经知道了，为什么我们的引导程会先打出`"HandlerAdded"`和`"ChannelRegistered"`。

接着往下`isActive()`最终调用是的jdk `ServerSocket`类的`isBound`方法，咱们不再贴出代码，读者自行查看，很简单，显然这里我们还没有完成端口绑定，所以这个`if`分支的代码并不会执行。

```java
    private void register0(ChannelPromise promise) {
        try {
            //向Selector注册Channel
            doRegister();
           
            //去完成那些在绑定EventLoop之前触发的添加handler操作，这些操作被放在pipeline中的pendingHandlerCallbackHead中，是个链表，具体请参考`DefaultChannelPipeLine`的`addLast(EventExecutorGroup group, String name, ChannelHandler handler)`方法。
            pipeline.invokeHandlerAddedIfNeeded();
            
            //将promise设置为成功的
            safeSetSuccess(promise);

            //触发ChannelRegistered事件
            pipeline.fireChannelRegistered();
            
            //这里并没有Active，因为此时还没完成端口绑定，所以这个if分支的代码都不会执行
            if (isActive()) {
                if (firstRegistration) {
                    pipeline.fireChannelActive();
                } else if (config().isAutoRead()) {
                    beginRead();
                }
            }
        } catch (Throwable t) {
        }
    }
```

接下来咱们跟进去`doRegister`方法，这是个抽象方法，本例中方法实现在`AbstractNioChannel`中。好了，到这里我们终于看到了**导读中提到的第4步“向`Selector`注册`Channel`”的操作**。

```java
    @Override
    protected void doRegister() throws Exception {
        boolean selected = false;
        for (;;) {
            try {
                selectionKey = javaChannel().register(eventLoop().selector, 0, this);
                return;
            } catch (CancelledKeyException e) {
                
            }
        }
    }
```

到了这里，我们在导读中说的总共4步操作中，还有第3步没有看到，在哪里呢，接着往下看。

#### 2.4 绑定端口号 doBind0

前文中我们说过`doBind`方法内有两个重要调用`initAndRegister`和`doBind0`，`initAndRegister`我们已经分析完了，接下来看`doBind0`。由于`initAndRegister`中`register`是异步操作，当`initAndRegister`返回时，`register`操作有可能完成了，也有可能没完成，这里做了判断，如果已经完成则直接调用`doBind0`，如果未完成，则将`doBind0`放到`regFuture`的`Listener`中，等`register`操作完成后，由`EventLoop`线程来回调。

那么什么时候会回调`Listener`呢，当调用`promise`的`setSuccess`或者`setFailure`时回调。还记得上文中的`AbstractUnsafe.register0`方法吗，其中有一个调用`safeSetSuccess(promise)`，对，就是这里了，很简单，我们不再赘述。

```java
private ChannelFuture doBind(final SocketAddress localAddress) {
    final ChannelFuture regFuture = initAndRegister();
    if (regFuture.isDone()) {
        ChannelPromise promise = channel.newPromise();
        doBind0(regFuture, channel, localAddress, promise);
        return promise;
    } else {
        final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
        regFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                  
                } else {
                    doBind0(regFuture, channel, localAddress, promise);
                }
            }
        });
        return promise;
    }
}
```

那么又有读者疑问了，在这个`if`判断完成之后到添加`Listener`之间的这个时间，`promise`有可能已经完成了，`Listener`可能不会回调了， 奥秘在`DefaultPromise`的`addListener(GenericFutureListener<? extends Future<? super V>> listener)`方法里，这里注册完`Listener`之后，如果发现`promise`已经完成了，那么将直接调用`nofityListeners`方法向`EventLoop`提交异步任务（此时已经完成绑定`EventLoop`），该异步任务即是回调刚刚注册的`Listener`。

```kotlin
@Override
public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
    synchronized (this) {
        addListener0(listener);
    }
    if (isDone()) {
        notifyListeners();
    }
    return this;
}
```

咱们回归正题，去看`doBind0`方法，这里调用了`channel.bind`方法，具体实现在`AbstractChannel`里。

```java
private static void doBind0(
        final ChannelFuture regFuture, final Channel channel,
        final SocketAddress localAddress, final ChannelPromise promise) {

    channel.eventLoop().execute(new Runnable() {
        @Override
        public void run() {
            if (regFuture.isSuccess()) {
                channel.bind(localAddress, promise).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                promise.setFailure(regFuture.cause());
            }
        }
    });
}
```

`AbstractChannel`里的`bind`方法调用了`pipeline.bind`，还记得一篇“Netty整体架构”文章中的那张图吗，咱们再次放出来。

![avatar](../images/111.webp)

netty整体架构图

`bind`方法会首先调用Tail的`bind`方法，最终传播到`Head`的`bind`方法，具体怎么传播的，咱们讲`PipeLine`的时候再说。

```java
@Override
public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
    return pipeline.bind(localAddress, promise);
}
```

这里咱们直接跟到`HeadContext`的`bind`方法， 我们看到又调用了`unsafe`的`bind`方法，前面我们看到`Channel`在向`Selector`注册时最终也调用到了`unsafe`。这里先跟大家说一下`unsafe`是netty中最直接跟`Channel`接触的类，对`Channel`的所有操作最终都会落到`unsafe`上，具体详情咱们后面讲`unsafe`的时候再说。

```java
@Override
public void bind(
        ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
        throws Exception {
    unsafe.bind(localAddress, promise);
}
```

具体实现在`AbstractUnsafe`中，bind方法中两个重要操作，一是调用`doBind`方法绑定端口，这个稍后说。二是触发`ChannelActive`事件，这一步有一个`isActive`判断，到这里我们已经完成了端口绑定，所以是true。这一步调用了咱们引导程序中的`System.out.println("ChannelActive")`在控制台打印出`"ChannelActive"`。

```java
@Override
public final void bind(final SocketAddress localAddress, final ChannelPromise promise) {
   
    boolean wasActive = isActive();
    try {
        doBind(localAddress);
    } catch (Throwable t) {
    }

    if (!wasActive && isActive()) {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                pipeline.fireChannelActive();
            }
        });
    }
}
```

`doBind`方法的实现在`NioServerSocketChannel`中，我们一起来看一下，至此**导读中提到的第3步操作“绑定端口”**，我们已经看到了，服务端启动完成。

```java
protected void doBind(SocketAddress localAddress) throws Exception {
    if (PlatformDependent.javaVersion() >= 7) {
        javaChannel().bind(localAddress, config.getBacklog());
    } else {
        javaChannel().socket().bind(localAddress, config.getBacklog());
    }
}
```

##### 2.4.1 注册兴趣事件在哪里

但是似乎是不是少了点什么，我们在使用jdk api编写的时候，向`selector`注册的时候，传递了兴趣事件的，为什么我们没有看到这里有兴趣事件的注册呢。我们继续回到`AbstractUnsafe`的`bind`方法中，最后调用了`pipeline.fireChannelActive()`，下面是`PipeLine`的`fireChannleActive`方法，调用了`AbstractChannelHandlerContext.invokeChannelActive(head)`，而这个`head`就是我们的“netty整体架构图”中的`HeadContext`。

```java
@Override
public final ChannelPipeline fireChannelActive() {
    AbstractChannelHandlerContext.invokeChannelActive(head);
    return this;
}

static void invokeChannelActive(final AbstractChannelHandlerContext next) {
EventExecutor executor = next.executor();
if (executor.inEventLoop()) {
    next.invokeChannelActive();
} else {
    executor.execute(new Runnable() {
        @Override
        public void run() {
            next.invokeChannelActive();
        }
    });
}
}
```

`HeadContext`中的`channelActive`方法如下，奥秘在`readIfIsAutoRead`里，`readIfIsAutoRead`，最终调用了`channel.read`。

```java
@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.fireChannelActive();

    readIfIsAutoRead();
}

private void readIfIsAutoRead() {
    if (channel.config().isAutoRead()) {
        channel.read();
    }
}
```

`channel.read`方法的实现在`AbstractChannel`中，调用到了`pipeline.read`。

```java
@Override
public Channel read() {
    pipeline.read();
    return this;
}
```

`PipeLine`中的`read`方法如下，调用了`tail`的`read`方法，最终这个调用会传播到`head`的`read`方法，具体的传播过程，等咱们讲`PipeLine`的时候再说。咱们直接去看`HeadContext`的`read`方法。

```kotlin
@Override
public final ChannelPipeline read() {
    tail.read();
    return this;
}
```

`HeadContext`的`read`方法又调用到`unsafe.beginRead()`。

```csharp
@Override
public void read(ChannelHandlerContext ctx) {
    unsafe.beginRead();
}
```

`beginRead`方法的实现在`AbstractUnsafe`中，这里调用了`doBeginRead`。`doBeginRead`方法的实现在`AbstractNioChannel`中。

```java
@Override
public final void beginRead() {
    try {
        doBeginRead();
    } catch (final Exception e) {
    
    }
}
```

`doBeginRead`方法的实现在`AbstractNioChannel`中，这里修改了`selectionKey`的兴趣事件，把已有的兴趣事件`interestOps`和`readInterestOp`合并在一起重新设置。

`interestOps`是现有的兴趣事件，在上文中向`Selector`注册时的代码里`javaChannel().register(eventLoop().selector, 0, this)`，所以`interestOps`就是0。

`readInterestOp`在哪里设置的呢，还记得本篇文章中新创建一个`Channel`那一小节中吗？

```java
@Override
protected void doBeginRead() throws Exception {
    // Channel.read() or ChannelHandlerContext.read() was called
    final SelectionKey selectionKey = this.selectionKey;
    if (!selectionKey.isValid()) {
        return;
    }

    readPending = true;

    final int interestOps = selectionKey.interestOps();
    if ((interestOps & readInterestOp) == 0) {
        selectionKey.interestOps(interestOps | readInterestOp);
    }
}
```

在`NioServerSocketChannel`调用父类的构造方法时传递了一个兴趣事件参数，值为`SelectionKey.OP_ACCEPT`，至此，真相大白。

```java
public NioServerSocketChannel(ServerSocketChannel channel) {
    super(null, channel, SelectionKey.OP_ACCEPT);
    config = new NioServerSocketChannelConfig(this, javaChannel().socket());
}
```

九曲连环，我们终于找到了这么小小的一个点，为什么流程这么长呢，似乎很难理解，不要紧，继续关注我的文章，咱们讲`PipeLine`的时候会把这里讲明白。

## 3 总结

netty服务端启动流程：

1. 创建一个`Channel`实例，这个过程中将`Channel`设置为非阻塞的，为`Channel`创建了`PipeLine`和`Unsafe`。
2. 初始化`Channel`，为`Channel`设置参数和属性，并添加`ServerBootstrapAcceptor`这个特殊的`Handler`。
3. 注册`Channel`，为`Channel`绑定一个`EventLoop`并向`Selector`注册`Channel`。
4. 绑定端口。