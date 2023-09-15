## 以集群模式创建的路由，Topic、队列会在所有的Broker Server中被创建、但是发送到该Topic的一条Message，只会被存储到某一个Broker Server中



![avatar](../images/8.jpg)

我们按照上图去发送消息，发了100条Message，得到如下图的发送反馈

![avatar](../images/WechatIMG752.png)

是往2个broker发送的，总量也只发了100条！