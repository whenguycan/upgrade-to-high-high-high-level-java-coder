## Consumer在广播模式下消费消息如何选择Broker Server和Queue



#### 1、Consumer如何选择Broker Server

通过[这儿](../入门/4、Name Server集群中如何选择哪个Name Server向客户端（Producer或Consumer）提供服务？.md)我们已经知道了，如何选择NameServer，确定了Name Server后就会经历如下过程选择Broker Server：

从Name Server 拉取路由表和Broker列表大概长如下的样子

![avatar](../images/WechatIMG753.png)

![avatar](../images/WechatIMG754.png)

然后Consumer会跟指定Topic下所有的Broker Server建立长连接。



#### Consumer如何选择Queue

那么Consumer会消费该Topic下的所有Queue。

