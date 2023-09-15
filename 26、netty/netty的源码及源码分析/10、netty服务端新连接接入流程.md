## 服务端新连接接入

## 1 服务端的启动过程中涉及到的新连接接入

### 1.1 两个EventLoopGroup

我们在“服务端的启动过程”这篇文章中看到过`ServerBootstrap`在启动时需要传入两个`EventLoop`，一个叫`bossGroup`，一个叫`workerGroup`，`bossGroup`处理监听端口的`Channel`，而`workerGroup`为新建立的连接提供服务，具体如何提供服务呢，咱们往下看。



```java
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

### 1.2 ServerBootstrapAcceptor

在`ServerBootstrap`的`init`方法中为`NioServerSocketChannel`添加了一个特殊的`Handler`，就是`ServerBootstrapAcceptor`，这个`Handler`就是服务端新连接接入过程中的主要逻辑所在了。

```java
void init(Channel channel) throws Exception {
    
    p.addLast(new ChannelInitializer<Channel>() {
        @Override
        public void initChannel(Channel ch) throws Exception {
            final ChannelPipeline pipeline = ch.pipeline();
            ChannelHandler handler = config.handler();
            if (handler != null) {
                pipeline.addLast(handler);
            }

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

## 2 新连接接入

我们在“`EventLoop`的工作原理”这篇文章中提到中过一个特殊的事件`OP_ACCEPT`，并且比较奇怪的是，在发生`OP_ACCEPT`兴趣事件时，调用了`unsafe.read`方法。今天咱们就从这里开始跟踪一下服务端新连接接入的过程。



```java
private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
    final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
    try {

        //OP_ACCEPT事件却调用unsafe.read()方法
        //参考io.netty.channel.nio.AbstractNioMessageChannel.NioMessageUnsafe.read和io.netty.channel.socket.nio.NioServerSocketChannel.doReadMessages
        if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
            unsafe.read();

        }
    } catch (CancelledKeyException ignored) {
        unsafe.close(unsafe.voidPromise());
    }
}
```

`NioServerSocketChannel`继承了`AbstractNioMessageChannel`，所以，此处调用的`unsafe.read()`实现在`NioMessageUnsafe`中，该类中有一个`readBuf`属性，比较特殊的是这个`readBuf`中的泛型不是`ByteBuf`而是`Object`，为什么呢，咱们接着往下看，到`doReadMessages`这里传入了这个`readBuf`属性。



```java
private final class NioMessageUnsafe extends AbstractNioUnsafe {

        private final List<Object> readBuf = new ArrayList<Object>();
        @Override
        public void read() {

            try {
                try {
                    do {
                        int localRead = doReadMessages(readBuf);
                        if (localRead == 0) {
                            break;
                        }
                        if (localRead < 0) {
                            closed = true;
                            break;
                        }

                        allocHandle.incMessagesRead(localRead);
                    } while (allocHandle.continueReading());
                } catch (Throwable t) {
                    exception = t;
                }

                int size = readBuf.size();
                for (int i = 0; i < size; i ++) {
                    readPending = false;
                    pipeline.fireChannelRead(readBuf.get(i));
                }
                readBuf.clear();
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();

            } finally {
               
            }
        }
    }
```

跟到`doReadMessages`方法去看看，这里调用`javaChannel.accept()`方法拿到了一个`SocketChannel`，随后封装成`NioSocketChannel`并加入到入参`buf`（即是前面提到的`AbstractNioMessageChannel.NioMessageUnsafe#readBuf`）中。



```java
@Override
protected int doReadMessages(List<Object> buf) throws Exception {
    SocketChannel ch = javaChannel().accept();
    try {
        if (ch != null) {
            buf.add(new NioSocketChannel(this, ch));
            return 1;
        }
    } catch (Throwable t) {
       
    }
    return 0;
}
```

咱们接着回到`NioMessageUnsafe`的`read`方法中，在调用完`doReadMessages`方法之后又调用了`pipeline.fireChannelRead`方法，参数就是`doReadMessages`中所加入的`NioSocketChannel`。

在`pipeline`中的传播过程咱们不再赘述，因为我们刚刚分析过`pipeline`的工作原理，最终这个事件会传播到`ServerBootstrapAcceptor`中。我们来看一下`ServerBootsrapAcceptor`的`channelRead`方法。



```java
 public void channelRead(ChannelHandlerContext ctx, Object msg) {
    final Channel child = (Channel) msg;

    child.pipeline().addLast(childHandler);

    for (Entry<ChannelOption<?>, Object> e : childOptions) {
        try {
            if (!child.config().setOption((ChannelOption<Object>) e.getKey(), e.getValue())) {
                logger.warn("Unknown channel option: " + e);
            }
        } catch (Throwable t) {
            logger.warn("Failed to set a channel option: " + child, t);
        }
    }

    for (Entry<AttributeKey<?>, Object> e : childAttrs) {
        child.attr((AttributeKey<Object>) e.getKey()).set(e.getValue());
    }

    try {
        childGroup.register(child).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    forceClose(child, future.cause());
                }
            }
        });
    } catch (Throwable t) {
        forceClose(child, t);
    }
}
```

`ServerBootsrapAcceptor`的`channelRead`方法中首先为新连接添加`handler`，设置连接参数、连接属性，最后最为关键的一步就是将连接注册到`childGroup`中，这里的`childGroup`就是引导代码中的`wokerGroup`，注册过程我们在“服务端启动过程”和“客户端启动过程”中都详细讲过，此处不再赘述。



```java
 public void channelRead(ChannelHandlerContext ctx, Object msg) {
    //这里的msg实际上是NioSocketChannel，请参考io.netty.channel.nio.NioEventLoop.processSelectedKey(java.nio.channels.SelectionKey, io.netty.channel.nio.AbstractNioChannel)方法
    final Channel child = (Channel) msg;
    //为新连接添加handler
    child.pipeline().addLast(childHandler);
    //为新连接设置连接参数
    for (Entry<ChannelOption<?>, Object> e : childOptions) {
        try {
            if (!child.config().setOption((ChannelOption<Object>) e.getKey(), e.getValue())) {
                logger.warn("Unknown channel option: " + e);
            }
        } catch (Throwable t) {
            logger.warn("Failed to set a channel option: " + child, t);
        }
    }
    //为新连接设置属性
    for (Entry<AttributeKey<?>, Object> e : childAttrs) {
        child.attr((AttributeKey<Object>) e.getKey()).set(e.getValue());
    }
    //将新连接注册到workerGroup上
    try {
        childGroup.register(child).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    forceClose(child, future.cause());
                }
            }
        });
    } catch (Throwable t) {
        forceClose(child, t);
    }
}
```

## 3 总结

## 服务端新连接接入的逻辑比较简单，在`ServerSocketChannel`上发生`OP_ACCEPT`事件后，触发`ChannelRead`事件，`ChannelRead`事件传播过程中携带的参数就是新接入的连接，事件传播至`ServerBootsrapAcceptor`后被注册到`workerGroup`。

