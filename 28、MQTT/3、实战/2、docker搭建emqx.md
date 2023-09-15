## docker搭建emqx



#### 拉取镜像

````shell
docker pull emqx:5.0.19
````



#### 运行EMQX

```shell
docker run -i -d --name=emqx -p 18083:18083 -p 1883:1883 emqx:5.0.19
```



#### 修改EMQX的日志等级为debug

```shell
docker exec -i emqx的容器名称 /opt/emqx/bin/emqx ctl log set-level debug
```



#### 打开dashboard

地址 http://IP:18083

用户名：admin

密码：public

进入之后会让改密码！