## pull关键字

> 通过images关键字，查看镜像之后发现我们没有需要的镜像，就需要使用`pull`关键字来拉取镜像。
>
> 镜像是从我们安装docker的时候配置的/etc/docker/daemon.json文件中的`registry-mirrors`指定的镜像仓库地址拉取的！
>
> 查看镜像的版本可以到hub.docker.com去搜索你需要的镜像版本。



- 拉取镜像

  ```shell
  docker pull redis:latest
  ```

  这儿拉取的是redis的最新版本的镜像

  