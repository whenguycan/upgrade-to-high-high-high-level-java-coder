## Consumer的pull模式与push模式



#### 概述

- `DefaultLitePullConsumer` 为Pull类型的Consumer，即由自己去拉取消息。<font color="red">Consumer端会根据自身的消费情况去Broker Server端拉取消息，那么对于Consumer端来说，消息的<font size="5">实时性略差</font>！</font>

- `DefaultMQPushConsumer` 为Push类型的Consumer，底层其实还是使用pull的方式去Broker Server端拉取消息，当Consumer的Pull请求到达Broker Server后，如果Broker Server有数据，那么就立马返回，Consumer再次发送Pull请求；当Broker Server不存在数据的时候，Broker Server并不会给Consumer响应，而是将Pull请求给hold住，当Broker Server有数据的时候才会给Consumer响应，返回数据。

<font color="red">所以实际上，他们的底层实现都是 pull，即都是由客户端去 Broker 获取消息。即consumer轮询从broker拉取消息</font>。



#### 使用Pull模式写代码：DefaultLitePullConsumer

- 手动分配消息队列到当前Consumer（一般不用！）

  ```java
  @GetMapping("/index2")
  public void index2() throws MQClientException {
  
    DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cpg");
    consumer.setNamesrvAddr("10.10.210.24:9876;10.10.210.24:9877");
    consumer.setPullBatchSize(50); //设置一次拉取最多多少条消息，实际拉取可能不到这个数值
    consumer.setPollTimeoutMillis(5000); //拉取超时时间
    consumer.setMessageModel(MessageModel.CLUSTERING);
    consumer.start(); //先启动consumer
  
  
    Collection<MessageQueue> mqSet = consumer.fetchMessageQueues("t4"); //获取到t4主题下所有的队列
    System.out.println(mqSet);  //打印下t4的Topic下所有的queue信息：[MessageQueue [topic=t4, brokerName=broker-a, queueId=2], MessageQueue [topic=t4, brokerName=broker-a, queueId=1], MessageQueue [topic=t4, brokerName=broker-a, queueId=3], MessageQueue [topic=t4, brokerName=broker-a, queueId=0]]
    List<MessageQueue> list = new ArrayList<>(mqSet);
    List<MessageQueue> assignList = new ArrayList<>(); //初始化一个空的list，是用来存放当前Consumer需要消费的queue的
    assignList.add(list.get(0));  //获取到第一个队列
    consumer.assign(assignList); //手动设置当前Consumer需要消费的队列
  
  
    while(true){
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

  

- 只要订阅Topic，系统自动分配消息队列到当前Consumer，无需手动配置当前consumer消费哪个queue

  ```java
  @GetMapping("/index2")
  public void index2() throws MQClientException {
  
    DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cpg");
    consumer.setNamesrvAddr("10.10.210.24:9876;10.10.210.24:9877");
    consumer.subscribe("t4", "*");//订阅消费的Topic和tag
    consumer.setPullBatchSize(50); //设置一次拉取最多多少条消息，实际拉取可能不到这个数值
    consumer.setPollTimeoutMillis(5000); //拉取超时时间
    consumer.setMessageModel(MessageModel.CLUSTERING);
    consumer.start();
  
  
    while(true){
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
  
  ```

  



#### 使用Push模式写代码：DefaultMQPushConsumer

注册一个监听器，当pull到消息后，触发监听器的执行去消费消息！

![avatar](../images/4.jpg)