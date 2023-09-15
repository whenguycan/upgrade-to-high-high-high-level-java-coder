## export关键字

> 当一个基础的image不能满足我们的要求，我们需要在镜像启动的容器中安装一些工具，然后再把容器制作成镜像怎么办？就需要使用docker export命令
>
> 比如：我们使用centos:7镜像，但是基础的镜像中没有安装vim工具，但是我们需要直接就要在镜像中带vim，就可以使用centos:7运行一个容器，然后进入容器安装好vim，再用docker export把容器制作成tar包！



- 将容器制作成tar包

  ```shell
  docker export -o xxxxx.tar 容器id
  ```

  