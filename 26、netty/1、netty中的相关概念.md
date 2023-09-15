## netty中的相关概念



#### 一、什么是netty

Netty是一个开源的异步事件驱动的网络应用程序框架，用于快速开发可维护的高性能协议服务器和客户端。



#### 二、Netty能做什么

根据各种通讯协议，写客户端、服务端的应用，实现自己的HTTP、FTP、UDP、RPC、WebSocket、Redis的Proxy、Mysql的Proxy的服务器等等。



#### 三、Netty的关键知识点：

1. I/O: 各种各样的流(文件、数组、缓冲、管道。。。)的数据(输入输出) ;

   NIO是非阻塞的IO

   BIO是阻塞的IO

2. Channel: 通道，代表一个连接， 每个Client请对会对应到具体的- - 个Channel;
3. ChannelPipeline: 责任链，每个Channel都有且仅有一个ChannelPipeline与之 对应，里面是各种各样的Handler
4. handler: 用于处理出入站消息及相应的事件，实现我们自己要的业务逻辑;
5. EventLoopGroup: I/O线程池， 负责处理Channe|对应的I/O事件;
6. ServerBootstrap: 服务器端启动辅助对象;
7. Bootstrap: 客户端启动辅助对象;
8. Channellnitializer: Channel初始化器;
9. ChannelFuture: 代表I/O操作的执行结果，通过事件机制（连接成功、连接断开、发送数据都是事件机制，而且都是异步处理），获取执行结果，通过添加监听器，执行我们想要的操作;
10. ByteBuf: 字节序列，通过ByteBuf操作基础的字节数组和缓冲区;