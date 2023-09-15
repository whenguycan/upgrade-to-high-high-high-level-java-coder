## Name Server集群中如何选择哪个Name Server向客户端（Producer或Consumer）提供服务？

客户端在配置时必须要写上NameServer集群的地址，那么客户端到底连接的是哪个NameServer节点呢?客户端首先会生产一个随机数，然后再与NameServer节点数量取模，此时得到的就是所要连接的节点索引，然后就会进行连接。如果连接失败，则会采用round robin策略,逐个尝试着去连接其它节点。

即，首先采用的是`随机策略`进行选择，如果失败采用的是`轮询策略`。