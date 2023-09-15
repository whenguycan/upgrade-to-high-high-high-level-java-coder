## docker的组成部分和底层架构



#### 1、docker client

docker的客户端，负责将用户输入的命令转换成REST API格式的请求，发送给docker daemon



#### 2、docker daemon

docker的一个守护进程，一般也会被称为 docker engine。负责和Docker client交互，提供Docker镜像、容器的API接口。

```shell
ps ax | grep dockerd
15341 ?        Ssl   52:46 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock
23662 pts/0    S+     0:00 grep --color=auto dockerd
```

如上命令可以看出，dockerd已经被启动了！



#### 3、Containerd

containerd本身也只是一个守护进程，向上为Docker Daemon提供了gRPC接口管理镜像（镜像、元信息等）、容器执行（调用最终运行时组件执行），为Docker Daemon屏蔽下面的结构变化确保原有接口向下兼容。向下通过containerd-shim结合runC，使得引擎可以独立升级，避免之前Docker Daemon升级会导致所有容器不可用的问题。

它囊括了一个容器运行时所需要的一切：执行、分发、监控、网络、构建、日志等。主要作用是：

1）管理容器的生命周期（从创建容器到销毁容器）
2）拉取/推送容器镜像
3）存储管理（管理镜像及容器数据的存储）
4）调用runC运行容器（与runC等容器运行时交互）
5）管理容器网络接口及网络

```shell
ps ax | grep containerd
2650 ?        Ssl  117:35 /usr/bin/containerd
```

如上命令可以看出，containerd已经启动了！



#### 4、docker-containerd-shim

是一个真实运行的容器的真实垫片载体，每启动一个容器都会起一个新的containerd-shim进程。它会向下调用runc的api来操作容器，比如创建一个容器（最后拼装的命令如下：runc create ......）

```shell
ps ax | grep containerd
 2650 ?        Ssl  117:35 /usr/bin/containerd
13850 ?        Sl     1:44 /usr/bin/containerd-shim-runc-v2 -namespace moby -id 76c57f408d39432780b924acf91d66aeccfec89dadee19579b5250f98c815446 -address /run/containerd/containerd.sock
13974 ?        Sl     1:41 /usr/bin/containerd-shim-runc-v2 -namespace moby -id 003cf6339e7844b356fb71e6714cde514c12c17be5564063ddded5d73b8dc6b3 -address /run/containerd/containerd.sock
14895 ?        Sl    11:07 /usr/bin/containerd-shim-runc-v2 -namespace moby -id de3a7d95eb26f95d32de205502db1886e8deb128397969a46c4a3b3a1c984039 -address /run/containerd/containerd.sock
```

可以看到一个容器对应一个containerd-shim



#### 5、runC

定义了容器运行时标准，具体细实线容器启停、资源隔离等功能





#### 6、整体的组成和运行：

<img src="../images/112233.png" alt="avatar" style="zoom:150%;" />

学习了这儿是为了后面学k8s的架构做准备！以及后续k8s的架构的变更！


