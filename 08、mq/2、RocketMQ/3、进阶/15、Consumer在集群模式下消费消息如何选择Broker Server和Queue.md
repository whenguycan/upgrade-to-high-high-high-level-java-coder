## Consumer消费消息如何选择Broker Server和Queue



#### 1、Consumer如何选择Broker Server

通过[这儿](../入门/4、Name Server集群中如何选择哪个Name Server向客户端（Producer或Consumer）提供服务？.md)我们已经知道了，如何选择NameServer，确定了Name Server后就会经历如下过程选择Broker Server：

从Name Server 拉取路由表和Broker列表大概长如下的样子

![avatar](../images/WechatIMG753.png)

![avatar](../images/WechatIMG754.png)

然后Consumer会跟指定Topic下所有的Broker Server建立长连接。



#### Consumer如何选择Queue

- 当前Consumer是该Topic下的第1个消费者

  那么该Consumer会消费该Topic下的所有Queue。

- 当前Consumer不是该Topic下的第1个消费者（1个消费者组中可以有多个消费者，这些消费者消费的Topic、tag都是一致的！）

  RocketMQ会遵循**队列负载的指导思想**：以**消费组为维度**，**一个消费者能分配多个队列，但一个队列只会分配给一个消费者**。此时新加入的Consumer会触发RocketMQ的消费者rebalance操作，重新分配queue到每个Consumer。<font color="red">如果Topic是使用集群方式创建的，所有的consumer也会均分所有Broker Server上的所有Queue</font>