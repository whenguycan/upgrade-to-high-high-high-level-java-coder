## links关键字



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
    links:
      - "db:database"
    environment:
      JAVA_OPTS: "-Djava.security.egd=file:/dev/./urandom"
      WAIT_HOSTS: "redis:6379,postgre:5432,nacos:8848"
      NACOS_SERVER: "nacos:8848"
      NACOS_SERVER_USERNAME: "nacos"
      NACOS_SERVER_PASSWORD: "nacos"
```

links后面配置的是 一个服务的名字，`:`之后的配置是给服务名字取个别名。在容器中，使用服务名字和别名都可以通讯。



#### 2、范例详述

使用links关键字，将2个或多个容器绑定在一起，容器中的环境变量互相共享。可以使用容器名称通讯取代使用IP通讯，应对IP经常变动的问题。