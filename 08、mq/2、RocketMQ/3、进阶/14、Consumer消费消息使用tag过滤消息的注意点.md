## Consumer消费消息使用tag过滤消息的注意点



消费代码如下

```java
@GetMapping("/index2")
public void index2() throws MQClientException {

  DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cpg");
  consumer.setNamesrvAddr("10.10.210.24:9876;10.10.210.24:9877");
  consumer.subscribe("t5", "mtYagLast");//订阅消费的Topic和tag
  consumer.setPullBatchSize(50); //设置一次拉取最多多少条消息，实际拉取可能不到这个数值
  consumer.setMessageModel(MessageModel.CLUSTERING);


  consumer.registerMessageListener(new MessageListenerConcurrently() {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

      System.out.println(list);

      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }
  });


  consumer.start();

}
```

在` consumer.subscribe("t5", "mtYagLast");` 指出当前消费者订阅了t5这个Topic，并且使用`mtYagLast` 过滤消息