## 部署gitlab

#### 方式一、yum安装

1. 安装

   使用清华源的地址去找到gitlab-ce，地址：https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7/ 找到对应的版本，下载rpm包，通过命令rpm -ivh xxxx.rpm就可以安装好

2. 修改配置

   修改/etc/gitlab/gitlab.rb中的external_url地址，这个是提供外部访问的地址，可以改为”http://192.168.2.235:9999”，

   修改/etc/gitlab/gitlab.rb中的nginx的监听端口，nginx["listen_port"] = 与上面配的9999一样即可

   然后一定要运行 gitlab-ctl reconfigure 命令，需要给gitlab初始化一些数据，注意只要运行一次即可，等待运行完成。

3. 、启动服务

   启动：gitlab-ctl start

   关闭：gitlab-ctl stop

4. 登录，如果是502，需要等待一会，因为有些服务器还在启动中

   如果长时间没有好 可以通过gitlab-ctl status查看是否有down的项目，还可以通过gitlab-ctl tail查看具体的启动日志，是否有端口占用的情况。

   

   默认用户名：root

   默认密码：第一次登录会让你修改





#### 方式二、docker安装

参考：https://docs.gitlab.com/ee/install/docker.html

官方文档写的很清楚，非常好用。



安装好了，之后，第一次登录，文档上也写了怎么获取root用户的密码！

