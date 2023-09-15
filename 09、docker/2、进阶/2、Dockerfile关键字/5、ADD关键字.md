## ADD关键字

> 将一个本地或网络文件，添加到基础镜像的指定目录中。注意，如果这儿添加的是一个压缩包，如tar包，会自动解压到指定目录中。



```dockerfile
FROM nginx:latest
......
WORKDIR /opt/data
ADD ./app.jar ./new-app.jar
```

注意：ADD 后跟着的第一个参数是当前dockerfile文件所在的路径，后面一个参数是上面WORKDIR切换到的路径！

