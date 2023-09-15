## 修改docker容器的默认网段





#### 1、容器启动不指定--network参数时

- 根据/etc/docker/daemon.json中的如下配置来确定网络

  ```json
  {
      "bip": "192.168.1.1/24"
  }
  ```

  当容器不指定--network时，就会默认走上面配置的网段的IP。

  

- 删除现有的docker网卡

  先`ifconfig`查看下docker网卡

  ```shell
  docker0: flags=4099<UP,BROADCAST,MULTICAST>  mtu 1500
          inet 172.17.0.1  netmask 255.255.0.0  broadcast 172.17.255.255
          ether 02:42:f3:ae:10:fd  txqueuelen 0  (Ethernet)
          RX packets 0  bytes 0 (0.0 B)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 0  bytes 0 (0.0 B)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  ens33: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
          inet 172.16.4.134  netmask 255.255.255.0  broadcast 172.16.4.255
          inet6 fe80::de52:d74d:ef75:f140  prefixlen 64  scopeid 0x20<link>
          ether 00:50:56:37:98:a0  txqueuelen 1000  (Ethernet)
          RX packets 130001  bytes 160929729 (153.4 MiB)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 53340  bytes 4535603 (4.3 MiB)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  
  lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
          inet 127.0.0.1  netmask 255.0.0.0
          inet6 ::1  prefixlen 128  scopeid 0x10<host>
          loop  txqueuelen 1000  (Local Loopback)
          RX packets 4  bytes 200 (200.0 B)
          RX errors 0  dropped 0  overruns 0  frame 0
          TX packets 4  bytes 200 (200.0 B)
          TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
  ```

  

  删除docker0的网卡

  ```shell
  ip link delete docker0
  ```

  重启docker

  ```shell
  systemctl daemon-reload && systemctl restart docker
  ```

  使用`ifconfig`查看docker0的IP段

  

- 运行一个nginx的容器

  ```shell
  docker run -d -i --name=nginx nginx:1.23.3
  ```

  然后使用`docker inspect 容器ID` 来查看容器的`IPAddress`信息，就可以证明bip的配置已经生效了！





#### 2、容器指定--network参数时

<span id="need">需要对创建的网络设定IP</span>

- 根据/etc/docker/daemon.json中的如下配置来确定网络

  ```json
  {
    
    "default-address-pools": [ { "base": "18.0.0.0/16", "size": 24 } ]
    
  }
  ```

- 运行`systemctl daemon-reload`让上面的修改生效！

- 运行`systemctl restart docker` 启动docker

- 运行`docker network create foo` 创建一个叫`foo`的网络

- 运行`docker network inspect foo | grep Subnet` 查看是不是我们配置文件中配置好的IP网段即可。