## docker搭建maven私有仓库



- 下载maven的docker镜像

  下载地址：https://hub.docker.com/r/sonatype/nexus3/

  

- 打开宿主机的ipv4网络转发

  编辑/etc/sysctl.conf文件，添加，net.ipv4.ip_forward = 1到文件最后一行，随后运行sysctl -p 生效。

  

- 运行如下命令启动仓库

  ```shell
  docker run -d -p 8081:8081 --name nexus sonatype/nexus3
  ```



​	网页上打开http://xxxxx/8081 即可访问，登录用户名默认为admin，密码在登录的时候会提示在哪里。之后需要docker exec -ti containerId bash 进入到容器中，查看默认密码。

