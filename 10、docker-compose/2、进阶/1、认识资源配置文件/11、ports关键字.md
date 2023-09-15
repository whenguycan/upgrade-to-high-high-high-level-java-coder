## ports关键字



#### 1、使用范例

```yaml
version: '3'
services:
  auth:
    image: 'harbocto.xxx.com.cn/crust/crust-auth:1.0.0'
    restart: always
    container_name: auth
    networks:
      - crust-net
    volumes:
      - ./logs:/app/logs/
    expose:
      - "3000"
      - "6000"
    ports:
      - 10001:10001
    environment:
      JAVA_OPTS: "-Djava.security.egd=file:/dev/./urandom"
      WAIT_HOSTS: "redis:6379,postgre:5432,nacos:8848"
      NACOS_SERVER: "nacos:8848"
      NACOS_SERVER_USERNAME: "nacos"
      NACOS_SERVER_PASSWORD: "nacos"
```

ports中可以将容器端口映射到127.0.0.1的网卡的端口上，即 127.0.0.1:port:port

#### 2、范例详述

ports是用来配置宿主机与容器之间的端口映射的，设置规则为：“宿主机端口:容器端口” 。使用字符串格式即 “1000:1000”这样。

