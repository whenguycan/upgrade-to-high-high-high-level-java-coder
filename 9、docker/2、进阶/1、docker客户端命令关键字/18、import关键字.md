## import关键字

> 上面的export已经把一个容器打包成tar包了，那么我们需要把tar包中的东西提取成镜像，就需要使用docker import命令了



- 将tar包中的文件提取到一个镜像中

  ```shell
  docker import xxxx.tar image名称:image版本
  ```

  之前我们往容器中安装了vim的，这会我们再用这个镜像起一个容器看看是不是有vim工具？

  注意：<font color="red">`docker run` 运行导入的镜像一定要指定command，即：`docker run -i -d --name=xxxx iamgeid command`不带command会报错！</font>具体的command，可以使用`docker ps --no-trunc` 找到导出容器的command就是它了！

