## WORKDIR关键字

> 为RUN、CMD、ENTRYPOINT指令配置工作目录。
>
> 可以使用多个WORKDIR指令，后续参数如果是相对路径，则会基于之前的命令指定的路径。如：WORKDIR  /home　　WORKDIR test 。最终的路径就是/home/test。



```dockerfile
FROM nginx:latest
WORKDIR /opt/data
RUN pwd
```

