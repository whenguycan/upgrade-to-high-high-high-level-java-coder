## Producer发送消息失败会触发消息重投



#### 消息重投的条件

同步和异步方式发送消息，才有消息重投，单向发送没有！

顺序消息没有消息重投！

只是尽可能保证发送成功，不丢失，因为消息重投那么就可能出现消息重复！



#### 消息重投策略

- 同步发送失败策略：

  如果发送失败，默认重试2次，但在重试时，是不会选择上次发送失败的broker的。当然如果只有一个broker，那只能选择这个broker，但是会避开之前的Queue。如果超过重试次数之后，则抛出异常。

  

  producer.setRetryTimesWhenSendFailed(3);//修改重试次数为3次，默认为2次

  producer.setSendMsgTimeout(5000);//设置发送超时时限为5S，默认为3S

- 异步发送失败策略：

  如果发送失败，不会选择其他broker，仅在同一个broker上做重试。所以，无法保证消息丢失。

  producer.setRetryTimesWhenSendFailed(3);