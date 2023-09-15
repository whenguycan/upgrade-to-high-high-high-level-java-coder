## docker安装



#### 1、安装必要依赖

```shell
yum install -y yum-utils device-mapper-persistent-data lvm2
```



#### 2、添加docker的阿里云yum源

```shell
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```



#### 3、更新yum源

```shell
yum makecache fast
```



#### 4、安装docker

- 方式1：安装默认版本的docker

  ```shell
   yum -y install docker-ce docker-ce-cli containerd.io
  ```

  

- 方式2：安装指定版本的docker

  - 查找Docker-CE的版本

    ```shell
    yum list docker-ce.x86_64 --showduplicates | sort -r
    ```

  - 安装指定版本的Docker-CE: (VERSION 例如上面的 17.03.0.ce.1-1.el7.centos)

    ```shell
    yum -y install docker-ce-[VERSION] docker-ce-cli-[VERSION] containerd.io
    ```

    注意：在某些版本之后，docker-ce安装出现了其他依赖包，如果安装失败的话请关注错误信息。例如 docker-ce 17.03 之后，需要先安装 docker-ce-selinux。

    \# yum list docker-ce-selinux- --showduplicates | sort -r

    \# sudo yum -y install docker-ce-selinux-[VERSION]



#### 5、配置docker的阿里云的镜像源

创建目录

```shell
mkdir /etc/docker/
```

使用命令编辑/etc/docker/daemon.json文件

```shell
vim /etc/docker/daemon.json
```

写入如下内容：

```json
{ 
  "exec-opts": ["native.cgroupdriver=systemd"], #这块是为以后安装k8s做准备的
  "registry-mirrors": ["https://e951xbyx.mirror.aliyuncs.com"] 
}
```

随后运行，如下命令：

```shell
systemctl daemon-reload
```



#### 6、启动docker

```shell
systemctl start docker
systemctl enable docker
```



docker安装完成之后，会创建一个docker的用户组！









