## Broker Server集群的复制策略、刷盘策略



#### 复制策略

指master与slave之间的数据同步。

- 同步复制：消息写入master后，master会等待slave同步数据成功后才向producer返回成功ACK
- 异步复制：消息写入master后，master立即向producer返回成功ACK，无需等待slave同步数据成功



#### 刷盘策略

指master或slave内部，数据从内存写入到磁盘。

- 同步刷盘：当消息持久化到broker的磁盘后才算消息写入成功。
- 异步刷盘：当消息写入到broker的内存后表示消息写入成功，无需等待消息持久化到磁盘。

对于异步刷盘，消息会写入到PageCache后立即返回成功ACK，但并不会立即做落盘操作，而是当PageCache到达一定量之后才会去落盘。