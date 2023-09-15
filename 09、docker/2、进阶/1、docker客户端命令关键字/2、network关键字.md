## network关键字

> docker中关于网络创建、删除、查看的命令关键字
>
> 
>
> 如果使用network关键字去创建的网络，使用的ip段为是在/etc/docker/daemon.json中配置的`default-address-pool`中。
>
> 
>
> 网络创建好之后，是在`docker run --network=网络名称 imageid` 



- 创建一个网络

  ```shell
  docker network create litong
  ```




- 创建一个网络，并设置网卡类型、IP段

  ```shell
  docker network create --driver=bridge --subnet=10.10.0.0/16 sellphonecard
  ```

  

- 查看网络详情

  ```shell
  docker network inspect litong
  ```

  查看一个网络的IP段，就只要找到Subnet就可以看到了！



- 查看所有创建好的网络

  ```shell
  docker network ls
  ```

  

- 删除一个网络

  ```json
  docker network rm litong
  ```

  



