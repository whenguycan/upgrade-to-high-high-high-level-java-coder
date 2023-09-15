## EXPOSE关键字

> 指定自己制作的镜像需要对外暴露的端口，注意这儿只是声明镜像暴露端口，在使用镜像启动容器的时候，需要使用 `-p port:port`进行端口映射的！



```dockerfile
FROM nginx:latest
RUN .....
EXPOSE 80
......
```

 