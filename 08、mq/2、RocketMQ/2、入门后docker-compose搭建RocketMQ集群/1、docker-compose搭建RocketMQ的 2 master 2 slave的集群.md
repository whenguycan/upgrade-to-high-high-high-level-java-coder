## docker-compose搭建RocketMQ的2 master 2 slave的集群

> 参考官方：https://github.com/apache/rocketmq-docker



#### 第一步：了解Broker Server和Name Server的端口占用

- Name Server
  1. 9876 对外提供服务的端口
- Broker Server
  1. 10911 指定broker对外提供服务的端口，即Broker与producer/consumer通信的端口。默认10911。
  2. 10912 主从同步端口，由指定的对外提供服务的端口 + 1自动得到，这儿是10911 + 1
  3. 10909 用于slave同步master，由指定的对外提供服务的端口 - 2自动得到，这儿是10911 - 2



#### 第二步：Broker Server配置文件，逐条解释

```properties
#当前broker的IP地址，docker部署也选择填
brokerIP1=
#指定整个broker集群的名称，或者说是RocketMQ集群的名称
brokerClusterName = 
#Master与Slave的对应关系是通过指定相同的BrokerName、不同的BrokerId 来确定的。BrokerId为0表示Master,非0表示Slave
brokerName = 
#Master与Slave的对应关系是通过指定相同的BrokerName、不同的BrokerId 来确定的。BrokerId为0表示Master,非0表示Slave
brokerId = 
#指定删除消息存储过期文件的时间为凌晨4点
deleteWhen = 04 
#指定未发生更新的消息存储文件的保留时长为48小时，48小时后过期，将会被删除
fileReservedTime = 48 
#指定当前broker为异步复制master，ASYNC_MASTER=异步复制Master，SYNC_MASTER=同步双写Master，SLAVE=slave节点
brokerRole = ASYNC_MASTER  
#指定刷盘策略为异步刷盘，ASYNC_FLUSH=异步刷盘，SYNC_FLUSH=同步刷盘 
flushDiskType = ASYNC_FLUSH 
#指定name server的地址
namesrvAddr = ip:port;ip:port  
#指定broker对外提供服务的端口，即Broker与producer/consumer通信的端口。默认10911。会自动算出10912和10909端口。
listenPort = 10911 

#每个topic对应队列的数量，默认为4，实际应参考consumer实例的数量，值过小不利于consumer负载均衡
defaultTopicQueueNums=8
#是否允许 Broker 自动创建Topic，生产建议关闭
autoCreateTopicEnable=true
#是否允许 Broker 自动创建订阅组，生产建议关闭
autoCreateSubscriptionGroup=true

#这行往下，docker部署可以不指定，因为rocketmq都有自带的默认值
storePathRootDir = ~/store  
storePathCommitLog = ~/store/commitLog
storePathConsumeQueue = ~/store/consumequeue
storePathIndex = ~/store/index
storeCheckpoint = ~/store/checkpoint
abortFile = ~/store/abort
```





#### 第三步：运行如下命令创建好目录

```shell
mkdir -p /data/rocketmq/{logs-nameserver-m,logs-nameserver-s,logs-a,logs-a-s,logs-b,logs-b-s,store-a,store-a-s,store-b,store-b-s,conf}
```



#### 第四步：准备4个Broker Server的配置文件

进入到/data/rocketmq/conf目录

- broker-a.conf

  ```properties
  #所属集群名字，同一个集群名字相同
  brokerClusterName=rocketmq-cluster
  #broker名字
  brokerName=broker-a
  #0表示master >0 表示slave
  brokerId=0
  #删除文件的时间点，凌晨4点
  deleteWhen=04
  #文件保留时间 默认是48小时
  fileReservedTime=168
  #异步复制Master
  brokerRole=ASYNC_MASTER
  #刷盘方式，ASYNC_FLUSH=异步刷盘，SYNC_FLUSH=同步刷盘 
  flushDiskType=ASYNC_FLUSH
  #Broker 对外服务的监听端口
  listenPort=10911
  #nameServer地址，这里nameserver是单台，如果nameserver是多台集群的话，就用分号分割（即namesrvAddr=ip1:port1;ip2:port2;ip3:port3）
  namesrvAddr=10.10.210.24:9876;10.10.210.24:9877
  #每个topic对应队列的数量，默认为4，实际应参考consumer实例的数量，值过小不利于consumer负载均衡
  defaultTopicQueueNums=8
  #是否允许 Broker 自动创建Topic，生产建议关闭
  autoCreateTopicEnable=true
  #是否允许 Broker 自动创建订阅组，生产建议关闭
  autoCreateSubscriptionGroup=true
  #设置BrokerIP
  brokerIP1=10.10.210.24
  ```

  

- broker-b.conf

  ```properties
  #所属集群名字
  brokerClusterName=rocketmq-cluster
  #broker名字，注意此处不同的配置文件填写的不一样  例如：在a.properties 文件中写 broker-a  在b.properties 文件中写 broker-b
  brokerName=broker-b
  #0 表示 Master，>0 表示 Slave
  brokerId=0
  #删除文件时间点，默认凌晨 4点
  deleteWhen=04
  #文件保留时间，默认 48 小时
  fileReservedTime=168
  #Broker 的角色，ASYNC_MASTER=异步复制Master，SYNC_MASTER=同步双写Master，SLAVE=slave节点
  brokerRole=ASYNC_MASTER
  #刷盘方式，ASYNC_FLUSH=异步刷盘，SYNC_FLUSH=同步刷盘 
  flushDiskType=SYNC_FLUSH
  #Broker 对外服务的监听端口
  listenPort=11911
  #nameServer地址，这里nameserver是单台，如果nameserver是多台集群的话，就用分号分割（即namesrvAddr=ip1:port1;ip2:port2;ip3:port3）
  namesrvAddr=10.10.210.24:9876;10.10.210.24:9877
  #每个topic对应队列的数量，默认为4，实际应参考consumer实例的数量，值过小不利于consumer负载均衡
  defaultTopicQueueNums=8
  #是否允许 Broker 自动创建Topic，生产建议关闭
  autoCreateTopicEnable=true
  #是否允许 Broker 自动创建订阅组，生产建议关闭
  autoCreateSubscriptionGroup=true
  #设置BrokerIP
  brokerIP1=10.10.210.24
  ```

  

- broker-a-slave.conf

  ```properties
  #所属集群名字
  brokerClusterName=rocketmq-cluster
  #broker名字，注意此处不同的配置文件填写的不一样  例如：在a.properties 文件中写 broker-a  在b.properties 文件中写 broker-b
  brokerName=broker-a
  #0 表示 Master，>0 表示 Slave
  brokerId=1
  #删除文件时间点，默认凌晨 4点
  deleteWhen=04
  #文件保留时间，默认 48 小时
  fileReservedTime=168
  #Broker 的角色，ASYNC_MASTER=异步复制Master，SYNC_MASTER=同步双写Master，SLAVE=slave节点
  brokerRole=SLAVE
  #刷盘方式，ASYNC_FLUSH=异步刷盘，SYNC_FLUSH=同步刷盘 
  flushDiskType=SYNC_FLUSH
  #Broker 对外服务的监听端口
  listenPort=12911
  #nameServer地址，这里nameserver是单台，如果nameserver是多台集群的话，就用分号分割（即namesrvAddr=ip1:port1;ip2:port2;ip3:port3）
  namesrvAddr=10.10.210.24:9876;10.10.210.24:9877
  #每个topic对应队列的数量，默认为4，实际应参考consumer实例的数量，值过小不利于consumer负载均衡
  defaultTopicQueueNums=8
  #是否允许 Broker 自动创建Topic，生产建议关闭
  autoCreateTopicEnable=true
  #是否允许 Broker 自动创建订阅组，生产建议关闭
  autoCreateSubscriptionGroup=true
  #设置BrokerIP
  brokerIP1=10.10.210.24
  ```

  

- broker-b-slave.conf

  ```properties
  brokerClusterName=rocketmq-cluster
  brokerName=broker-b
  #slave
  brokerId=1
  deleteWhen=04
  fileReservedTime=168
  brokerRole=SLAVE
  flushDiskType=ASYNC_FLUSH
  #Broker 对外服务的监听端口
  listenPort=13911
  #nameServer地址，这里nameserver是单台，如果nameserver是多台集群的话，就用分号分割（即namesrvAddr=ip1:port1;ip2:port2;ip3:port3）
  namesrvAddr=10.10.210.24:9876;10.10.210.24:9877
  #每个topic对应队列的数量，默认为4，实际应参考consumer实例的数量，值过小不利于consumer负载均衡
  defaultTopicQueueNums=8
  #是否允许 Broker 自动创建Topic，生产建议关闭
  autoCreateTopicEnable=true
  #是否允许 Broker 自动创建订阅组，生产建议关闭
  autoCreateSubscriptionGroup=true
  #设置BrokerIP
  brokerIP1=10.10.210.24
  ```

<font color="red">确保，/data/rocketmq及其子目录777的权限！</font>

#### 第五步：创建目录存放docker-compose.yml

```shell
mkdir -p /usr/local/docker-compose/rocketmq
```



#### 第六步：vi docker-compose.yml 如下内容

```yaml
version: '2'
services:
  namesrv1:
    image: apache/rocketmq:4.8.0
    container_name: rmqnamesrv1
    ports:
      - 9876:9876
    volumes:
      - /data/rocketmq/logs-nameserver-m:/home/rocketmq/logs
    environment:
      JAVA_OPT_EXT: -server -Xms256M -Xmx256M -Xmn128m
    command: sh mqnamesrv
  namesrv2:
    image: apache/rocketmq:4.8.0
    container_name: rmqnamesrv2
    ports:
      - 9877:9877
    volumes:
      - /data/rocketmq/logs-nameserver-s:/home/rocketmq/logs
    environment:
      JAVA_OPT_EXT: -server -Xms256M -Xmx256M -Xmn128m
    command: sh mqnamesrv
  broker-a-m:
    image: apache/rocketmq:4.8.0
    container_name: rmqbroker-a-master
    ports:
      - 10909:10909
      - 10911:10911
      - 10912:10912
    volumes:
      - /data/rocketmq/logs-a:/home/rocketmq/logs
      - /data/rocketmq/store-a:/home/rocketmq/store
      - /data/rocketmq/conf/broker-a.conf:/home/rocketmq/rocketmq-4.8.0/conf/broker.conf
    environment:
      JAVA_OPT_EXT: -server -Xms256m -Xmx256m -Xmn128m
      NAMESRV_ADDR: 192.168.139.156:9876;192.168.139.156:9877
    command: sh mqbroker  -c /home/rocketmq/rocketmq-4.8.0/conf/broker.conf
  broker-b-m:
    image: apache/rocketmq:4.8.0
    container_name: rmqbroker-b-master
    ports:
      - 11909:11909
      - 11911:11911
      - 11912:11912
    volumes:
    	- /data/rocketmq/logs-b:/home/rocketmq/logs
    	- /data/rocketmq/store-b:/home/rocketmq/store
    	- /data/rocketmq/conf/broker-b.conf:/home/rocketmq/rocketmq-4.8.0/conf/broker.conf
    environment:
      JAVA_OPT_EXT: -server -Xms256m -Xmx256m -Xmn128m
      NAMESRV_ADDR: 192.168.139.156:9876;192.168.139.156:9877
    command: sh mqbroker  -c /home/rocketmq/rocketmq-4.8.0/conf/broker.conf
  broker-a-s:
    image: apache/rocketmq:4.8.0
    container_name: rmqbroker-a-slave
    ports:
      - 12909:12909
      - 12911:12911
      - 12912:12912
    volumes:
      - /data/rocketmq/logs-a-s:/home/rocketmq/logs
      - /data/rocketmq/store-a-s:/home/rocketmq/store
      - /data/rocketmq/conf/broker-a-slave.conf:/home/rocketmq/rocketmq-4.8.0/conf/broker.conf
    environment:
      JAVA_OPT_EXT: -server -Xms256m -Xmx256m -Xmn128m
      NAMESRV_ADDR: 192.168.139.156:9876;192.168.139.156:9877
    command: sh mqbroker  -c /home/rocketmq/rocketmq-4.8.0/conf/broker.conf
  broker-b-s:
    image: apache/rocketmq:4.8.0
    container_name: rmqbroker-b-slave
    ports:
      - 13909:13909
      - 13911:13911
      - 13912:13912
    volumes:
      - /data/rocketmq/logs-b-s:/home/rocketmq/logs
      - /data/rocketmq/store-b-s:/home/rocketmq/store
      - /data/rocketmq/conf/broker-b-slave.conf:/home/rocketmq/rocketmq-4.8.0/conf/broker.conf
    environment:
      JAVA_OPT_EXT: -server -Xms256m -Xmx256m -Xmn128m
      NAMESRV_ADDR: 192.168.139.156:9876;192.168.139.156:9877
    command: sh mqbroker  -c /home/rocketmq/rocketmq-4.8.0/conf/broker.conf
    depends_on:
      - namesrv1
      - namesrv2
```



#### 第七步：在docker-compose.yml的目录下启动

```shell
docker-compose up -d
```



#### 第八步：测试是否搭建成功

需要后面搭建好rocketmq-dashboard，然后进入Cluster菜单，能看到下图 就证明成功了！

![avatrar](../images/WechatIMG744.png)







