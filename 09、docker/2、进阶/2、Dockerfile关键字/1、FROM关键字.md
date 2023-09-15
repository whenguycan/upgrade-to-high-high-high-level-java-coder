## FROM关键字

> 指定当前要制作的镜像使用的基础镜像，在每个Dockerfile文件中，这个是必须要有的。
>
> 我的理解还有一层，把基础镜像中的dockerfile里面的命令全部include到咱们的dockerfile中。
>
> 我们要制作的镜像，只要对基础镜像做些修改就可以为我们所用了！



<img src="../../images/1677113713040.jpg" alt="avatar" style="zoom: 50%;" />

```dockerfile
FROM nginx:latest
......
......
......
```











