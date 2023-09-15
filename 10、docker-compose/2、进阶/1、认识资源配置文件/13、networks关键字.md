## networks关键字

> <font color="red">在默认的情况下，使用docker-compose创建容器的时候，会创建一个默认的网络。网络的名称是：“你的资源配置文件所在目录_default"，比如：root_default。</font>这个默认网络会对当前创建的所有services下面的服务默认生效，所以services下面的各个服务之间才能够通过service名称互相访问。
>
> 
>
> 如果我要让特定的服务不能跟另外的服务进行通讯，只要使用网络进行隔离就行。就需要使用networks关键字了。



#### 1、使用范例

- 情况1：

  在具体的service中没有指定network，那么service肯定默认会走default的网络，那么我们就可以对default网络进行修改

  ```yaml
  version: "3"
  services:
  
    web:
      build: .
      ports:
        - "8000:8000"
    db:
      image: postgres
  
  networks:
    default:
      driver: bridge
  ```

  这儿只修改了网络的驱动方式

- 情况2：

  在具体的service中没有指定network，那么service肯定默认会走default的网络，那么我们可以使用docker network create创建的网络对default网络进行修改，

  ```yaml
  version: "3"
  services:
  
    web:
      build: .
      ports:
        - "8000:8000"
    db:
      image: postgres
  
  
  networks:
    default:
      external:
        name: my-pre-existing-network  #使用docker network create创建的网络
  ```

  这儿使用了我们使用docker network create创建的网络。

  

- 情况3：

  在具体service中指定network，并且设置我们自己定义的network

  ```yaml
  version: "3"
  services:
    web:
      build: .
      ports:
        - "8000:8000"
      networks:
        -  ceshi  #这儿使用自定义的网络
    db:
      image: postgres
  
  
  networks:
    ceshi:
      driver: bridge
  ```



- 情况4

  在具体service中指定network，并且使用docker network create创建的网络来创建自定义的network

  ```yaml
  version: "3"
  services:
    web:
      build: .
      ports:
        - "8000:8000"
      networks:
        -  ceshi  #这儿使用自定义的网络
    db:
      image: postgres
  
  
  networks:
    ceshi:
      external:
        name: my-pre-existing-network  #使用docker network create创建的网络
  ```

  



#### 2、范例详述

与services同级的networks用于定义网络，

sevices下的networks表明该服务所在的容器使用哪个网络。



