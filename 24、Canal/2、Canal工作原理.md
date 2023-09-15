## Canal的工作原理

Canal将自己伪装成Mysql slave（从库），向Mysql master（主库）发送dump协议，Mysql master（主库）收到dump请求，开始推送binary log给Canal，Canal接收并解析Binlog日志，得到变更的数据，执行后续逻辑！

![avatar](./images/WechatIMG692.png)