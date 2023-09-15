## command关键字



#### 1、使用范例

```yaml
services:
  log:
    image: goharbor/harbor-log:v2.5.0
    container_name: harbor-log
    restart: always
    command: ["/bin/sh","-c","while true;do echo hello;sleep 1;done"]
```



#### 2、范例详述

command指定容器启动执行的命令，会覆盖掉镜像中原有启动命令。