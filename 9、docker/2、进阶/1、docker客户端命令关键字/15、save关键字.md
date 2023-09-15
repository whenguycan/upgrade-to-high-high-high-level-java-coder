## save关键字

> 使用场景：一个服务器不能使用外网，那么就没法通过docker pull image的方式去下载镜像。我们要把镜像弄到那台服务器上，怎么办？可以找一台连了外网的服务器，然后把镜像pull到本地，然后使用docker save命令，将一个或多个镜像打成tar包，拷贝到不能用外网的那台服务器上。



- 保存一个或多个镜像到tar包

  ```shell
  docker save -o docker.tar mysql:8.0.32 nginx:1.23.3
  ```

  `-o` 指定生成的tar名称 

