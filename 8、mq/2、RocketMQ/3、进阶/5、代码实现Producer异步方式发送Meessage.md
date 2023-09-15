## 代码实现Producer异步方式发送Meessage

> **异步发送消息，rocketmq-client中是开线程去发送的！**



#### 创建项目，引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-client -->
<dependency>
  <groupId>org.apache.rocketmq</groupId>
  <artifactId>rocketmq-client</artifactId>
  <version>4.8.0</version>
</dependency>
```

版本最好跟RocketMQ 的Broker Server版本一致



#### 编写代码发送消息

![avatar](../images/8.jpg)

图上代码中的休眠时间不需要。

图上代码中的producer.shutdown()应该是不需要写的，因为producer一旦连接到了nameserver上就要保持一个长连接，所以无需手动shutdown。



#### 发送成功后

可以到dashboard中查询消息

![avatar](../images/WechatIMG751.png)



同时，我们再去看下Broker Server中存储消息的文件，是否有了变化，会发现之前没有创建的文件都被创建出来了！

