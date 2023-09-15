## expose关键字

> 一般不用，因为设置容器暴露端口在镜像阶段已经做好了



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
      - '10001:10001'
    environment:
      JAVA_OPTS: "-Djava.security.egd=file:/dev/./urandom"
      WAIT_HOSTS: "redis:6379,postgre:5432,nacos:8848"
      NACOS_SERVER: "nacos:8848"
      NACOS_SERVER_USERNAME: "nacos"
      NACOS_SERVER_PASSWORD: "nacos"
```



#### 2、范例详述

expose是用来暴露容器端口的，但是一般在镜像中已经配置好了药暴露的端口，所以这个关键字一般不用。