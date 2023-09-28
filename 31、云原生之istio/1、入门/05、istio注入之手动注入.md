## istio注入之手动注入



#### 1、手动注入的概念

在启动pod的时候，手动执行如下命令

```shell
istioctl kube-inject -f nginx.yaml | kubectl apply -f -
```

就会往`nginx.yaml`资源配置文件启动的pod中，注入istio的istio-init、istio-proxy组件。



#### 2、手动注入测试

- 创建一个test的namespace给下面的nginx使用

  ```shell
  kubectl create ns test
  ```

  

- 准备一个`nginx.yaml`的资源配置文件，内容如下

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    labels:
      app: nginx
    name: nginx
    namespace: test
  spec:
    replicas: 1
    selector:
      matchLabels:
        app: nginx
    strategy: {}
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
        - image: nginx:1.14-alpine
          name: nginx
          resources: {}
  status: {}
  
  ```

  

- 使用如下命令运行`nginx.yaml`文件，并注入istio到nginx的pod中

  ````shell
  istioctl kube-inject -f nginx.yaml | kubectl apply -f -
  ````

  上面的命令会对`nginx.yaml`做修改，并重启部署nginx的deployment，再次发布的nginx就已经是被istio注入过的了。

  如果要看对原来的`nginx.yaml`文件做了哪些修改，可以运行`istioctl kube-inject -f nginx.yaml > nginx2.yaml `然后去查看`nginx2.yaml`文件即可。

  





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

