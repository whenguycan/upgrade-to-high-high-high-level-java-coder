## 安装docker-compose

> 在正常安装了docker的基础上进行
>
> 官方文档：https://docs.docker.com/compose/install/other/



#### 1、下载docker-compose的脚本文件

> 地址：https://github.com/docker/compose/tags

```shell
curl -SL https://github.com/docker/compose/releases/download/v2.15.1/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
```

上面的命令是，下载docker-compose的脚本之后改名为docker-compose放到/usr/local/bin中。



#### 2、赋予docker-compose的脚本可执行权限



#### 3、做一个docker-compose脚本的软链接文件到/usr/bin/中

```shell
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```



至此，docker-compose安装完毕。