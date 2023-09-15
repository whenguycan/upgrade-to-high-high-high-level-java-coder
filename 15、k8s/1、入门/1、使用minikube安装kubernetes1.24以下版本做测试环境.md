## 安装minikube做测试环境



#### 1、安装好docker环境

参考我们之前的docker讲解



#### 2、环境准备

- 关闭防火墙

  ```shell
  systemctl stop firewalld
  systemctl disable firewalld
  ```

- 关闭selinux

  ```shell
  sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久关闭
  setenforce 0  # 临时关闭
  ```

- 关闭swap分区

  ```shell
  swapoff -a  # 临时
  sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久
  ```



#### 4、安装kubectl工具

- 指定阿里云镜像，加快安装速度

  ```shell
  cat > /etc/yum.repos.d/kubernetes.repo << EOF
  [kubernetes]
  name=Kubernetes
  baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
  enabled=1
  gpgcheck=0
  repo_gpgcheck=0
  gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpghttps://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
  EOF
  ```

- 安装

  ```shell
  yum install kubectl-1.23.5 -y
  ```

  注意这儿安装的kubectl的版本就决定了，后面我们安装的kubernetes的版本

#### 5、安装minikube

到https://github.com/kubernetes/minikube/tags下载对应版本的.rpm文件

```shell
rpm -ivh xxxxxxxx.rpm
```

即可安装成功

安装成功后，可以查看当前minikube能安装的最新版本的kubernetes

```shell
minikube start -h | grep 'kubernetes-version' -A 2
```





#### 6、启动minikube

```shell
minikube start --force --kubernetes-version v1.23.5 --driver=none --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers --image-mirror-country=cn
```

注意：v1.23.5与上面安装kubectl的版本必须一致



#### 7、出现错误

- 出现`Sorry, Kubernetes 1.23.5 requires conntrack to be installed in root's path`错误，把conntrack安装下。

  ```shell
  yum install conntrack -y
  ```

  

#### 8、测试使用

```shell
kubectl get pods -A -o wide
```



#### 9、删除minikube安装的所有东西

```shell
minikube delete --all
```

