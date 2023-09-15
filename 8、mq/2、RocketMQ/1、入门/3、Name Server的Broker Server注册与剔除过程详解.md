## Name Server的Broker Server注册过程详解.md



#### Broker Server注册到Name Server

Name Server通常也是以集群的方式部署,不过，NameServer是无状态的， 即NameServer集群中的各个节点间是无差异的，各节点间相互不进行信息通讯。那各节点中的数据是如何进行数据同步的呢?在Broker节点启动时，轮询NameServer列表（前提是，在Boker中要配置好NameServer的所有地址），与每个NameServer节点建立长连接，发起注册请求。在NameServer内部维护着一个Broker列表， 用来动态存储Broker的信息。

Broker Server注册完成后，Broker中的Topic信息就会被Name Server收集，维护在Name Server中！

这种Name Server的无状态方式，有什么优缺点:

- 优点：NameServer集群搭建简单，扩容简单。
- 缺点：对于Broker，必须明确指出所有NameServer地址。 否则未指出的将不会去注册。也正因为如此，NameServer并不能随便扩容。



Broker节点为了证明自己是活着的，为了维护与Name Server间的长连接，会将最新的信息以心跳包的方式上报给Name Server，每30秒发送一次心跳。心跳包中包含Brokerld、Broker地址、 Broker名称、Broker所属集群名称等等。NameServer在接收到心跳包后， 会更新心跳时间戳，记录这个Broker的最新存活时间。

![avatar](../images/196.jpeg)





#### Broker Server从Name Server中剔除

由于Broker Server关机、宕机或网络抖动等原因，Name Server没有收到Broker Server的心跳， Name Server可能会将其从Broker Server列表中剔除。Name Server中有一个定时任务，每隔10秒就会扫描一次Broker Server表， 查看每一个Broker Server的最新心跳时间戳距离当前时间是否超过120秒，如果超过，则会判定Broker Server失效，然后将其从Broker Server列表中剔除。并将对应的Broker Server的Topic信息从路由表中剔除！



