## docker安装skywalking

> 使用的是es的存储方式



#### 1、安装skywalking专用的es

```yaml
version: '3.7'
services:
  elasticsearch:
    image: elasticsearch:7.12.0
    container_name: elasticsearch
    restart: always
    ports:
      - 9200:9200
    environment:
      discovery.type: single-node
      TZ: Asia/Shanghai
      ES_JAVA_OPTS: "-Xms512m -Xmx1024m"
```

搭建好es后，es内是空的，没有任何数据。

#### 2、安装skywalking的oap和ui模块

```yaml
version: '3.7'
services:
  oap:
    image: apache/skywalking-oap-server:8.8.0
    container_name: skywalking-oap
    restart: always
    ports:
      - 11800:11800
      - 12800:12800
    environment:
      SW_STORAGE: elasticsearch # 指定ES为数据存储
      SW_STORAGE_ES_CLUSTER_NODES: 117.80.145.253:9200
      TZ: Asia/Shanghai
  ui:
    image: apache/skywalking-ui:8.8.0
    container_name: skywalking-ui
    depends_on:
      - oap
    links:
      - oap
    restart: always
    ports:
      - 18080:8080
    environment:
      SW_OAP_ADDRESS: http://117.80.145.253:12800
      TZ: Asia/Shanghai

```

搭建好skywalking之后，es中会出现很多索引数据。



安装之后，注意使用`docker logs`命令观察oap和ui模块的日志，是否有报错！