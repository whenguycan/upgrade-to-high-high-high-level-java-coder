## docker搭建nacos

> 本文参考：https://github.com/nacos-group/nacos-docker



#### 1、下载官方naocs-docker的文件

地址：https://github.com/nacos-group/nacos-docker



#### 2、按照文档说明安装

> 安装过程中，需要修改nacos的版本，只需要进入example/.env文件中的nacos版本就行

在nacos的2.2.1及以上版本，

1. NACOS_AUTH_IDENTITY_KEY
2. NACOS_AUTH_IDENTITY_VALUE
3. NACOS_AUTH_TOKEN

这3个常量已经被干掉了，需要自己去配置，所以我们这儿选择的是2.2.0的版本，如果需要配置可以参考：https://nacos.io/zh-cn/docs/v2/guide/user/auth.html



安装单机版的nacos，数据存储在nacos内置的derby数据库中，只需执行

```shell
docker-compose -f example/standalone-derby.yaml up -d
```

注意

​	standalone-derby.yaml文件中关于prometheus、grafana都可以去掉，暂时用不到



#### 3、部署完成

浏览器打开：http://IP:8848/nacos/

用户名和密码都是：nacos



