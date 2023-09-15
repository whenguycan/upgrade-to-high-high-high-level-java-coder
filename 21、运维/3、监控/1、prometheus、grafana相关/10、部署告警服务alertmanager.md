## 部署alertmanager



#### 1、下载安装包

下载地址：https://github.com/prometheus/alertmanager/tags



#### 2、部署

```shell
#创建安装目录
mkdir -p /usr/local/alertmanager

#解压下载的安装包
tar -zxvf alertmanager-0.21.0.linux-amd64.tar.gz -C /usr/local/alertmanager

#修改可执行文件的权限
chmod 0755 /usr/local/alertmanager/alertmanager-0.21.0.linux-amd64/alertmanager
chmod 0755 /usr/local/alertmanager/alertmanager-0.21.0.linux-amd64/amtool
```



#### 3、理解配置文件

> 配置文件为alertmanager.yml

```yaml
global:
  resolve_timeout: 5m

route:
  group_by: ['alertname'] #分组概念：就是将多条告警信息聚合成一条发送，这样就不会收到连续的报警了。将传入的告警按标签分组(标签在prometheus中的rules中定义)。如果不想使用分组，可以这样写group_by: [...]。这儿的意思是，将多条消息以alertname进行分组。
  group_wait: 30s #第一组告警发送通知需要等待的时间，这种方式可以确保有足够的时间为同一分组获取多个告警，然后一起触发这个告警信息。
  group_interval: 5m #发送第一个告警后，等待"group_interval"发送一组新告警。
  repeat_interval: 1h ## 分组内发送相同告警的时间间隔。这里的配置是每1小时发送告警到分组中。举个例子：收到告警后，一个分组被创建，等待5分钟发送组内告警，如果后续组内的告警信息相同,这些告警会在3小时后发送，但是3小时内这些告警不会被发送。
  receiver: 'web.hook' #这里说一下，告警发送是需要指定接收器的，接收器在receivers中配置，接收器可以是email、webhook、pagerduty、wechat等等。一个接收器可以有多种发送方式。
  
receivers:
- name: 'web.hook'
  webhook_configs:
  - url: 'http://127.0.0.1:5001/'
  
# 下面都是选配的  
inhibit_rules:
#下面配置的含义：当有多条告警在告警组里时，并且他们的标签alertname,dev,instance都相等，如果severity: 'critical'的告警产生了，那么就会抑制severity: 'warning'的告警。
  - source_match:
      severity: 'critical' # 标签匹配满足severity=critical的告警作为源告警
    target_match: # 目标告警(被抑制的告警)
      severity: 'warning' # 告警必须满足标签匹配severity=warning才会被抑制
    equal: ['alertname', 'dev', 'instance']
```



#### 4、让alertmanager可以用systemctl

```shell
cat > /usr/lib/systemd/system/alertmanager.service <<EOF
[Unit]
Description=Alertmanager handles alerts sent by client applications such as the Prometheus server.
Documentation=https://prometheus.io/docs/alerting/alertmanager/
After=network.target

[Service]
User=root
ExecStart=/usr/local/alertmanager/alertmanager-0.21.0.linux-amd64/alertmanager \\
  --config.file=/usr/local/alertmanager/alertmanager-0.21.0.linux-amd64/alertmanager.yml \\
  --storage.path=/usr/local/alertmanager/alertmanager-0.21.0.linux-amd64/store \\ #指定数据存储路径  
ExecReload=/bin/kill -HUP
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF
```

>  注意：
>
> --storage.path 是指定数据存储路径
>
> 如果是集群化部署:
>
> 1. 在master的启动命令上要加`--cluster.listen-address=IP:8001 `指明自身的IP和PORT
> 2. 在slave的启动命令上要加`--cluster.listen-address=IP:8001`指明自身的IP和PORT、`--cluster.peer=IP:8001`指明master的IP和PORT。
>
> 





#### 5、启动alertmanager

```shell
systemctl daemon-reload
systemctl start alertmanager
systemctl status alertmanager
```





#### 6、打开地址查看alertmanager是否成功启动

地址：http://IP:9093