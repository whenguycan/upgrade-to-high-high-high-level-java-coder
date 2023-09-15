## 收集docker方式部署的docker容器的日志到loki再到grafana



#### 1、安装docker插件

> 主要是让docker认识loki的日志驱动

```shell
docker plugin install registry.docker.com/grafana/loki-docker-driver:main --alias loki --grant-all-permissions
```



#### 2、查看上面的docker插件是否安装成功并启用

```shell
 docker plugin ls
```



#### 3、需要收集日志的容器启动命令如下

```shell
docker run -id --name=容器的名称 --log-driver=loki --log-opt loki-url="http://YOUR_IP:3100/loki/api/v1/push" --log-opt max-size=50m --log-opt max-file=10 镜像ID
```



#### 4、grafana中集成loki

到Add DataSource中新增loki到granafa

![avatar](/Users/tangwei/Desktop/课件/19、服务日志收集/images/55667.png)

![avatar](/Users/tangwei/Desktop/课件/19、服务日志收集/images/8a1b1c157.png)

![avatar](/Users/tangwei/Desktop/课件/19、服务日志收集/images/ba27f32.png)

![avatar](/Users/tangwei/Desktop/课件/19、服务日志收集/images/07c3de8f43c.png)

就可以找到对应的container的名称，然后去查看日志了！