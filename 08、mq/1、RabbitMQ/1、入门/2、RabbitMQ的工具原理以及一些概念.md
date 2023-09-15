## RabbitMQ的工作原理以及一些概念



#### 1、工作原理

<img src="../images/WechatIMG498.jpeg" alt="avatar" style="zoom:67%;" />





#### 2、一些概念

- Virtual host：类似于租户的概念，每个租户分配一个Virtual host，不同的Virtual host下可以隔离对应的exchange和queue

- Producer：消息生产者，一般我们的client端就是为了生产消息的。
- Connection： 客户端（生产者、消费者）连接到RabbitMQ的server端的TCP连接
- Channel：客户端（生产者、消费者）跟服务端建立连接之后就会开辟信道，一个连接内部会开辟多个信道，提高收发效率！每一个信道是用来收发消息的。代码创建交换机、队列、banding都是通过信道操作的。
- Broker：就是指RabbitMQ的server
- Exchange：交换机，消息到了指定的交换机后会被交换机转发到需要的队列中。一个交换机可以绑定多个队列
- Queue：消息队列，其中的消息来自上层交换机。
- Binding：使用Routing key将exchange与Queue绑定在一起。
- Routing Key：将exchnage与Queue进行绑定时需要指定它，可以是表达式形式，比如`cz.#`，`cz.*`，`#.cz`，`*.cz`，表达式中的`*`可以匹配一个词，`#`能匹配都个词



