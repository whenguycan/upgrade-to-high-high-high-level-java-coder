## Broker Server中消息被存储的文件概述

RocketMQ中的消息存储在本地文件系统中，这些相关文件默认在当前用户主目录下的store目录中。

![avatar](../images/3.jpg)

abort: 该文件在Broker启动后会自动创建， 正常关闭Broker, 该文件会自动消失。若在没有启动Broker的情况下，发现这个文件是存在的，则说明之前Broker的关闭是非正常关闭。

checkpoint: 其中存储着commitlog、 consumequeue、 index文件的最后刷盘时间戳

commitlog: 其中存放着commitlog文件，而消息是写在commitlog文件中的

config: 存放着Broker运行期间的一些配置数据

consumequeue: 其中存放着consumequeue文件， 队列就存放在这个目录中

index: 其中存放着消息索引文件indexFile

lock: 运行期间使用到的全局资源锁