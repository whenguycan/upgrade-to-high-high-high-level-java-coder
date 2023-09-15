## COPY关键字

> 将一个本地文件，添加到基础镜像的指定目录中。
>
> 与ADD不同在于，不能添加网络文件，复制压缩包如tar包，不会自动解压。



```dockerfile
FROM nginx:latest
......
WORKDIR /opt/data
ADD ./app.jar ./new-app.jar
```

注意：COPY 后跟着的第一个参数是当前dockerfile文件所在的路径，后面一个参数是上面WORKDIR切换到的路径！