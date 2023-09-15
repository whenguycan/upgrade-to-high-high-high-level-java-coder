## Message在广播模式下的消费过程详细分析



再次贴出消费者代码

```java
public class TopicConsumer {

    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cpg");
        consumer.setNamesrvAddr("10.10.210.24:9876;10.10.210.24:9877");
        consumer.subscribe("t2", "*");//订阅消费的Topic和tag
        consumer.setPullBatchSize(50); //设置一次拉取最多多少条消息，实际拉取可能不到这个数值
        consumer.setPollTimeoutMillis(5000); //拉取超时时间
      	consumer.setMessageModel(MessageModel.BROADCASTING); //使用广播模式
        consumer.start();

        try {
            List<MessageExt> msgs = consumer.poll(5000); //执行一次拉取，超时设置为5秒
            if (CollectionUtils.isEmpty(msgs)){
                System.out.println("没有拉取到消息，休息10S");
            }else{
                msgs.forEach((msg)->{ //打印拉取到的消息
                    System.out.println(msg);
                });
                consumer.commitSync(); //消息消费完成，一定要告诉Broker Server消息已经被消费了，然后Broker server会同步队列的消费位置！
            }
        }catch (Exception e){
            consumer.shutdown();
            e.printStackTrace();
        }

    }

}
```



#### Consumer广播模式下消费消息的过程

在集群模式下，消息的offset被保存在Broker Server中store/config/consumerOffset.json文件中，而广播模式下，消息的offset是保存在本地的，/home/${user}/.rocketmq_offsets/${clientId}/${groupName}/offsets.json文件中，内容如下：

````json
{
	"offsetTable":{
    {
			"brokerName":"dev-k8s-234",
			"queueId":3,
			"topic":"onewayTopic"
		}:100,
  {
			"brokerName":"dev-k8s-234",
			"queueId":2,
			"topic":"onewayTopic"
		}:100,
{
			"brokerName":"dev-k8s-234",
			"queueId":1,
			"topic":"onewayTopic"
		}:100,
{
			"brokerName":"dev-k8s-234",
			"queueId":0,
			"topic":"onewayTopic"
		}:100
	}
}
````

每次Consumer需要拉取消息了，就解析本地的便宜量文件，带着消息偏移量请求Broker Server，从而获取消息！Consumer获取到消息后，再更新本地的消息偏移量文件。

