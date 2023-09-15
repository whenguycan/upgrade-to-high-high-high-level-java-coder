## 部署Grafana

> 参考官网文档：https://grafana.com/grafana/download/7.4.1?edition=oss

#### 1、部署Grafana

```shell
#下载
wget https://dl.grafana.com/oss/release/grafana-7.4.1-1.x86_64.rpm

#安装
yum install grafana-7.4.1-1.x86_64.rpm

#启动
systemctl daemon-reload
systemctl enable grafana-server
systemctl restart grafana-server.service
```



#### 2、访问以查看Grafana是否部署正常

http://IP:3000/login

用户名：admin

密码：admin



#### 3、添加prometheus到Grafana中

![avatar](../images/MG384.jpeg)

进去之后选择prometheus，然后找到需要配置的地方，配置配置就行。



然后就要去选择相应的dashboard页面去查看数据了，具体的dashboard怎么用，看下面的文章。