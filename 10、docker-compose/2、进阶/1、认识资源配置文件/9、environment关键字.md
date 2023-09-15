## environment关键字



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

往容器中设置环境变量，类似于`docker run -e xxx`的效果



