## ENV关键字

> 在制作的镜像中设置环境变量，在容器启动的时候可以直接使用，在使用`docker run `的时候可以使用 `-e `去修改环境变量的值。



```dockerfile
FROM nginx:latest
...
...
...
ENV name tangwei
```

