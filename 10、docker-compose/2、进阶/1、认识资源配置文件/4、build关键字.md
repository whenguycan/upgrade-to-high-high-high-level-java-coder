## build关键字



#### 1、使用范例

```yaml
services:
  服务名称: 
    build: 
      context: ....
      dockerfile: ....
    ....
```

#### 2、范例详述

服务除了可以基于指定的镜像，还可以基于一份Dockerfile，在使用up命令启动时，执行构建任务。其中`context` 指定上下文目录即dockerfile所在目录[相对、绝对路径都可以]。`dockerfile` 文件名称[在指定的context的目录下指定那个Dockerfile文件名称]



注意：如果同时指定了image和build两个关键字，那么Compose会构建镜像并且吧镜像命名为image后面的名字。

