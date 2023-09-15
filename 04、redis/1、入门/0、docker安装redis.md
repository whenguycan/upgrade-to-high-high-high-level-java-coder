## docker安装redis



#### 1、拉取redis镜像

拉取命令：

```shell
docker pull redis:6.2
```





#### 2、准备一个redis的配置文件

文件名为redis.conf，内容如下

```tex
bind 0.0.0.0   #绑定的IP，一般不用绑定IP的方式
protected-mode yes  #此值为no时外部网络可以直接访问，为yes时需配置bind ip或者设置访问密码
port 6379
databases 16  #数据库数量
requirepass ZhjsfiortLKsf  #连接redis的密码,在protected-mode配置为yes时需要使用
appendonly no #是否开启AOF 因为AOF效率低，所以不推荐打开，最好设置为no
```



#### 3、启动redis容器

```shell
docker run -i -d --name redis -v /redis/redis.conf:/usr/local/etc/redis/redis.conf -v /redis/data:/data -p 16379:6379 redis的镜像ID redis-server /usr/local/etc/redis/redis.conf 
```

在指定镜像ID之后，指定redis的启动命令和启动使用的配置文件。如果不指定启动命令，那么redis默认就直接使用redis-server启动，不使用任何配置文件。

