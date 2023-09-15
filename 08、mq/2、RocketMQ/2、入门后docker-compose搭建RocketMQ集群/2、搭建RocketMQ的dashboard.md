## 搭建RocketMQ的dashboard



#### 拉取镜像

```shell
docker pull apacherocketmq/rocketmq-dashboard:latest
```



#### 启动dashboard

```shell
docker run -i -d -e "JAVA_OPTS=-Drocketmq.namesrv.addr=10.10.210.24:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 --name=rocketmq-dashboard apacherocketmq/rocketmq-dashboard:latest
```

