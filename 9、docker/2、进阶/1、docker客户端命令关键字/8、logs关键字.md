## logs关键字

> 所有的日志驱动参考：https://docs.docker.com/config/containers/logging/configure/#supported-logging-drivers
>
> 
>
> 当容器启动之后，需要查看容器的日志，就需要使用这个关键字命令。
>
> 
>
> 既然是查看容器的日志，那么容器的日志是记录在哪里的？以什么样的形式存储呢？
>
> 1. 以什么方式存储，取决我们选择的容器日志驱动，默认为json-file的形式存储。可以在使用 `docker run --log-driver json-file .....` 或者在 /etc/docker/daemon.json文件中定义如下的
>
>    ```json
>    {
>      "log-driver": "json-file",
>      "log-opts": {
>        "labels": "production_status",
>        "env": "os,customer"
>      }
>    }
>    ```
>
> 
>
> 2. 存储在哪里，存储在/var/lib/docker/containers/ID/ID-json.log。



- 查看容器日志

  ```shell
  docker logs 容器id #查看一个容器的所有日志
  
  
  docker logs -n 100 容器id #查看一个容器最后100行日志
  
  docker logs -f 容器id #查看一个容器的实时日志
  ```
  
  

