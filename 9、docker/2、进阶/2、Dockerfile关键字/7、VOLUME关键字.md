## VOLUME关键字

> 指定制作的镜像，需要挂载的目录。在使用镜像启动容器的时候，不需要显示指定 `-v` 进行挂载，VOLUME的目录会在容器运行时自动挂载到/var/lib/docker/xxxxxxxxxxxx路径中，这儿的xxxxxxxxxx是一个随机的名字。



```dockerfile
FROM nginx:latest
......
......
......
VOLUME /opt/data
```

