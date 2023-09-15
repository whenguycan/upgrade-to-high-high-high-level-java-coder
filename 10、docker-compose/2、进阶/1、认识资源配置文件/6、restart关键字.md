## restart关键字



#### 1、使用范例

```yaml
services:
  log:
    image: goharbor/harbor-log:v2.5.0
    container_name: harbor-log
    restart: always

```





#### 2、范例详述

restart指定容器的重启策略，有如下几个值

1. no，默认策略，在容器退出时不重启容器
2. on-failure，在容器非正常退出时（退出状态非0），才会重启容器
3. on-failure:3，在容器非正常退出时重启容器，最多重启3次
4. always，在容器退出时总是重启容器
5. unless-stopped，在容器退出时总是重启容器，但是不考虑在Docker守护进程启动时就已经停止了的容器