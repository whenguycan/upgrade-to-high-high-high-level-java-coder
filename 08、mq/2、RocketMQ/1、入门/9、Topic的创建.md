## Topic的创建



#### 1、使用RocketMQ手动创建

- 集群模式创建

  <font color="red">保证topic的高可用</font>

  根据集群从Name Server中获取集群下所有Broker Server的master地址，然后再循环发送topic信息到集群中的每个Broker Server中。**当用集群模式去创建topic时，集群里面每个broker的queue的数量相同**		![avatar](../images/18.jpg?lastModify=1693961686)

  这种模式适用于消息量较小，需要高可用性的场景。因为Topic存在于整个集群中，所以即使某个Broker宕机，其他Broker依然可以处理这个Topic的消息。

- BrokerName模式创建 

  在BrokerName模式下，创建的Topic仅存在于特定的Broker。这意味着只有发送到这个Broker的消息才能被路由到这个Topic。这种模式适用于消息量较大，需要分布式处理的场景。但是，如果特定的Broker宕机，那么该Broker上的Topic将无法使用，直到Broker恢复。

  ![avatar](../images/15.jpg?lastModify=1693961686)

  

  perm的值有2、4、6三种。2为只能写不能读、4为只能读不能写、6为读写都可以



#### 2、开发中使用代码创建

在发送消息时，自动创建Topic，自动创建的Topic默认采用的是ClusterName模式。这样做的目的是为了确保消息的高可用性，即使某个或某些Broker宕机，其他的Broker仍然可以处理这个Topic的消息。

<img src="../images/8.jpg" alt="avatar"  />



