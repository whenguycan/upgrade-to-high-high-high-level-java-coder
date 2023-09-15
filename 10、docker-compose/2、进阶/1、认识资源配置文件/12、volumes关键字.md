## volumes关键字



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





#### 2、范例说明

volumes是配置将容器内的文件或目录映射到宿主机。设置规则为： “宿主机目录:容器目录”