## inspect关键字

> 在之前的network、volume也提到过这个关键字
>
> 
>
> inspect可以查看镜像(image)、容器(contrainer)的详细信息



- 查看镜像详情

  ```shell
  docker inspect imageid
  ```

  可以查看哪些信息？

  `ExposedPorts`镜像内指定的可以暴露的端口

  `Cmd`镜像内指定的容器启动命令

  `Entrypoint`镜像内指定的容器启动命令

  `Env`镜像内指定环境变量

  `WorkingDir`镜像内指定工作目录



- 查看容器详情

  ```shell
  docker inspect 容器id
  ```

  可以查看哪些信息？

  `NetworkSettings` 容器的网络设置

  