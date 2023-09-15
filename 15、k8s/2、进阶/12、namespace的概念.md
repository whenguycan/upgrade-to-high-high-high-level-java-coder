## namespace概念



#### 1、什么是namespace

namespace是kubernetes系统中的一种非常重要的资源，namespace的主要作用是用来实现多套环境的资源隔离，或者说是多租户的资源隔离。默认情况下，k8s集群中所有pod都是可以相互访问的，但在实际环境中，可能不想让两个pod之间相互访问，那此时就可以将两个pod划分到不同的namespace下。k8s通过将集群内部的资源分配到不同的namespace中，可以形成逻辑上的隔离，以方便不同的资源进行隔离使用和管理。不同的命名空间可以存在同名的资源，命名空间为资源提供了一个作用域。





#### 2、命令的使用

> namespace可以简写为ns，k8s创建之初默认会创建好几个namespace

- 查看目前系统中已有的namespace

  ```shell
  kubectl get namespace
  ```

  

- 创建一个namespace

  - 命令行创建

    ```shell
    kubectl create namespace dev
    ```

  - 资源配置文件创建

    ```yaml
    kind: Namespace
    apiVersion: v1
    metadata:
      name: dev
      labels:
        name: dev
    ```

    然后使用`kubectl apply -f xxx.yaml`执行资源配置文件



- 删除namespace

  ```shell
  kubectl delete namespace dev
  ```

  