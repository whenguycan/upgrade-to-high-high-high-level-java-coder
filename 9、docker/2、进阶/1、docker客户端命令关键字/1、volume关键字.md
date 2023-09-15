## volume关键字

> docker中关于数据卷的创建、删除、查看详细信息、查看列表的命令关键字。
>
> 数据卷是用来被容器运行所挂载使用， 即 `docekr run -v volume名称:/xxx/xxx iamgeid`





- 创建数据卷

  ```shell
  docker volume create redis-volume
  ```

  创建了一个名叫`redis-volume`的数据卷

  

- 查看数据卷的详情

  ```shell
  docker volume inspect redis-volume
  ```

  查看`redis-volume`数据卷的详情，其中`Mountpoint`给出当前数据卷的真实挂载目录。这个挂载目录是由docker自动生成的，我们无法修改！

  

- 查看所有创建好的逻辑卷

  ```shell
  docker volume ls
  ```

  

- 删除逻辑卷

  ```shell
  docker volume rm redis-volume
  ```

  

