## 安装minikube做测试环境



#### 1、安装好docker环境

参考我们之前的docker讲解



#### 2、安装cri-dockerd环境

下载地址：https://github.com/Mirantis/cri-dockerd 下载对应版本的rpm包，到服务器上

```shell
rpm -ivh xxxx.rpm
```

并设置为开机启动

```shell
systemctl start cri-docker.service && systemctl enable cri-docker.service
```



#### 3、环境准备

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
  yum install kubectl-1.25.3 kubeadm-1.25.3 kubelet-1.25.3 -y
  ```

  注意这儿安装的kubectl的版本就决定了，后面我们安装的kubernetes的版本

- 运行命令

  ```shell
  systemctl start kubelet && systemctl enable kubelet
  ```

  

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





#### 6、安装crictl

如果在安装minikube的rpm包的时候安装了crictl，这一步安装可以省略，注意仅安装可以省略，测试还是要跑的。

下载地址：https://github.com/kubernetes-sigs/cri-tools/tags  

crictl的版本尽量与k8s的版本一致

```shell
wget -c https://github.com/kubernetes-sigs/cri-tools/releases/download/v1.25.0/crictl-v1.25.0-linux-amd64.tar.gz

tar zxf crictl-v1.25.0-linux-amd64.tar.gz

mv crictl /bin/
```



测试运行`crictl ps`测试crictl是否成功安装，如果报错了，则需要修复，编辑`vim /etc/crictl.yaml`文件，将文件内容修改如下：

```yaml
runtime-endpoint: unix:///var/run/cri-dockerd.sock
timeout: 10
debug: false
```

再运行`crictl ps`发现没有问题了！



#### 7、提前下载一个镜像，缺少了这个镜像会找到k8s启动不了

```shell
docker pull registry.aliyuncs.com/google_containers/pause:3.6 #下载镜像

docker tag registry.aliyuncs.com/google_containers/pause:3.6 registry.k8s.io/pause:3.6 #镜像打个tag
```



#### 8、启动k8s

```shell
minikube start --kubernetes-version v1.25.3 --driver=none --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers --image-mirror-country=cn --cri-socket=/var/run/cri-dockerd.sock --extra-config=kubelet.cgroup-driver=systemd --cni=bridge
```

注意：v1.25.3与上面安装kubectl的版本必须一致

`--cni=`是指定使用的网络插件，如果指定为calico、flannel就需要后续下载很多墙外的镜像文件（即便是--image-repository指定了阿里云的镜像仓库，但是因为是新版本的k8s，阿里云仓库中没有），太麻烦！所以这儿直接指定bridge就拉到了！



#### 9、出现错误

- 首先使用如下命令删除之前所有安装过的东西

  ```shell
  minikube delete --all
  ```

  

#### 10、测试使用

```shell
kubectl get pods -A -o wide
```





#### 11、可能出现pod的Pending状态  挨个查看日志解决问题。

如果minikube start时指定的网络插件是calico的话`--cni=calico`，大概率docker镜像地址会被墙，自己去hub.docker.com去下载，然后重新tag下。

目前已知需要下载的：

1. calico/node
2. calico/cni
3. calico/pod2daemon-flexvol
4. calico/kube-controllers