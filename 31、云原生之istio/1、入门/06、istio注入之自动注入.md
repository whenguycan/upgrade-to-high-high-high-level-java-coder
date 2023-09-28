## istio注入之自动注入



#### 1、自动注入的概念

给namespace打一个人label标签，之后往这个namespace中发布的内容都会被istio自动注入！



#### 2、自动注入测试

- 查看test命名空间下有哪些label

  ```shell
  kubectl get ns test --show-labels
  ```

  

- 给test命名空间打上istio认可的label

  ```shell
  kubectl label ns test istio-injection=enabled
  ```

  

- 正常部署一个我们写好的资源配置文件(.yaml)

  使用如下命令，查看istio-proxy是否被安装，被安装了就证明注入成功了！

  ```shell
  kubectl exec -ti nginx-7cbb8cd5d8-gnspm -n test -c istio-proxy -- /bin/bash
  ```

  

  



#### 3、手动注入成功后，验证上面的pod中istio-proxy和nginx这两个容器是使用同一个网络

运行如下两个命令，对比istio-proxy容器和nginx容器的ip是不是一致的！

运行如下命令，查看istio-proxy的ip

```shell
[root@control-plane ~]# kubectl exec -ti nginx-9d8c7948c-4gf6l -n test -c istio-proxy -- ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.7  netmask 255.255.0.0  broadcast 172.17.255.255
        ether 02:42:ac:11:00:07  txqueuelen 0  (Ethernet)
        RX packets 2409  bytes 429928 (429.9 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 2375  bytes 262526 (262.5 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 1201  bytes 175755 (175.7 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 1201  bytes 175755 (175.7 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```

运行如下命令，查看nginx的ip

```shell
[root@control-plane ~]# kubectl exec -ti nginx-9d8c7948c-4gf6l -n test -c nginx -- ifconfig
eth0      Link encap:Ethernet  HWaddr 02:42:AC:11:00:07
          inet addr:172.17.0.7  Bcast:172.17.255.255  Mask:255.255.0.0
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:2906 errors:0 dropped:0 overruns:0 frame:0
          TX packets:2866 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0
          RX bytes:476220 (465.0 KiB)  TX bytes:308989 (301.7 KiB)

lo        Link encap:Local Loopback
          inet addr:127.0.0.1  Mask:255.0.0.0
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:1452 errors:0 dropped:0 overruns:0 frame:0
          TX packets:1452 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000
          RX bytes:212711 (207.7 KiB)  TX bytes:212711 (207.7 KiB)
```

两个容器的eht0网卡的ip确实是一致的，证明确实共用一个网络！





#### 4、手动注入成功后，验证上面的pod会在nginx容器开放端口的基础上，多开放几个端口

运行如下命令，查看

```shell
[root@control-plane ~]# kubectl exec -ti nginx-9d8c7948c-4gf6l -n test -c istio-proxy -- netstat -ntlp
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 0.0.0.0:15021           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:15021           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:80              0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15090           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:15090           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 127.0.0.1:15000         0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:15001           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:15001           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 127.0.0.1:15004         0.0.0.0:*               LISTEN      1/pilot-agent
tcp        0      0 0.0.0.0:15006           0.0.0.0:*               LISTEN      17/envoy
tcp        0      0 0.0.0.0:15006           0.0.0.0:*               LISTEN      17/envoy
tcp6       0      0 :::15020                :::*                    LISTEN      1/pilot-agent
[root@control-plane ~]# kubectl exec -ti nginx-9d8c7948c-4gf6l -n test -c nginx -- netstat -ntlp
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 0.0.0.0:15021           0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15021           0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:80              0.0.0.0:*               LISTEN      1/nginx: master pro #nginx的端口
tcp        0      0 0.0.0.0:15090           0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15090           0.0.0.0:*               LISTEN      -
tcp        0      0 127.0.0.1:15000         0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15001           0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15001           0.0.0.0:*               LISTEN      -
tcp        0      0 127.0.0.1:15004         0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15006           0.0.0.0:*               LISTEN      -
tcp        0      0 0.0.0.0:15006           0.0.0.0:*               LISTEN      -
tcp        0      0 :::15020                :::*                    LISTEN      -
```

上面，nginx只暴露了80端口，但是istio-proxy容器中的envoy暴露了很多另外的端口















