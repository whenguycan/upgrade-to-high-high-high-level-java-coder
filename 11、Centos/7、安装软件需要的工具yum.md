## yum详解



#### 1、安装需要的工具

```shell
yum install vim -y
```



#### 2、根据安装包名称去仓库中搜索相关的软件

```shell
yum search vim 
```



#### 3、结合当前的yum仓库，列出所有可安装的软件包

```shell
yum list
```

可用作搜索

```shell
yum list | grep vim
```



#### 4、对已经安装过的软件重新覆盖安装

```shell
yum reinstall vim -y
```



#### 5、查看当前的所有的可用的yum源

```shell
yum repolist
```

其实这个命令列出来的就是/etc/yum.repo.d下的所有的yum配置文件，不过，有些配置文件中enable=0，所以不会命令不会显示而已。



#### 6、添加一个yum源

- 方法1：添加一个yum源的配置文件的方式

  配置文件大致内容如下：

  ```tex
  #CentOS-fasttrack.repo
  [fasttrack]
  name=CentOS-7 - fasttrack  
  mirrorlist=http://mirrorlist.centos.org/?release=$releasever&arch=$basearch&repo=fasttrack&infra=$infra
  #baseurl=http://mirror.centos.org/centos/$releasever/fasttrack/$basearch/
  gpgcheck=1
  enabled=0
  gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
  ```

  文件中的关键详解：

  1. name： 执行yum源的名称，最终使用`yum repolist`查看到的名称就是这个
  2. baseurl： yum源地址（软件下载的地址），包括HTTP（http://）、本地（file:///）、FTP（ftp://）
  3. enabled：是否启用该yum源配置，只有启用了`yum repolist`才会显示。
  4. gpgcheck：设置此yum源是否校验文件，1为校验，0为不校验
  5. gpgkey：若开启gpg校验，此为公钥key文件地址
  6. failovermethod：有两个选项，roundrobin和priority，意思分别是有多个url可供选择时，yum选择的次序。roundrobin是随机选择，priority则根据url的次序从第一个开始。如果不指明，默认是roundrobin
  7. mirrorlist：用于指定镜像服务器的地址列表

- 方法2：通过`yum-config-manager`命令添加

  ```shell
  yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
  ```

  其实就是去指定的地址将.repo文件下载到/etc/yum.repo.d/而已。



#### 7、构建yum的下载缓存

```shell
yum makecache
```

就是将已经安装好的软件缓存下来，下次再来安装，就不用从外网拉取数据了，而是从缓存拉取数据进行安装。



#### 8、清理yum缓存

```shell
yum clean all
```



#### 9、卸载已经安装好的软件

```shell
yum remove vim -y
```

