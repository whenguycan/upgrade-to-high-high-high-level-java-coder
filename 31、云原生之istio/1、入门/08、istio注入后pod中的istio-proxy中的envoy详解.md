## istio注入后pod中的istio-proxy中的envoy详解

我们上面说过，当k8s中的pod等被istio注入之后，在其中就会运行起istio proxy的容器，而istio proxy容器内部就有一个envoy进程。通过查看进程启动命令，我们可以找到envoy的具体配置文件，然后根据配置文件，我们可以临时映射一个envoy的端口到宿主机上，然后通过网页打开，查看该envoy的配置！



#### 具体操作

- 查看envoy的进程和配置文件

  ```shell
  [root@control-plane ~]# kubectl exec -ti httpd-v1-6b77f8bd8f-bfvxq -n tangwei -c istio-proxy  -- sh
  $ ps -ef
  UID         PID   PPID  C STIME TTY          TIME CMD
  istio-p+      1      0  0 09:50 ?        00:00:04 /usr/local/bin/pilot-agent proxy sidecar --domain ...
  istio-p+     17      1  0 09:50 ?        00:00:58 /usr/local/bin/envoy -c etc/istio/proxy/envoy-rev.json ...
  istio-p+     32      0  0 14:00 pts/0    00:00:00 sh
  istio-p+     38     32  0 14:00 pts/0    00:00:00 ps -ef
  ```

  可以看到envoy的启动配置文件为`etc/istio/proxy/envoy-rev.json`

  

- 查看配置文件，并找到admin栏的相关配置，特别是其中暴露的端口：

  ```json
  "admin": {
      "access_log": [
        {
          "name": "envoy.access_loggers.file",
          "typed_config": {
            "@type": "type.googleapis.com/envoy.extensions.access_loggers.file.v3.FileAccessLog",
            "path": "/dev/null"
          }
        }
      ],
      "profile_path": "/var/lib/istio/data/envoy.prof",
      "address": {
        "socket_address": {
          "address": "127.0.0.1",
          "port_value": 15000
        }
      }
    },
  ```

  发现envoy的管理端占用15000端口！



- 可以临时暴露pod中的envoy的第二步中查到的admin管理端口大啊啊哦宿主机，然后用网页来查看envoy的具体配置

  通过如下命令暴露pod中的envoy的15000端口到15009端口，

  ```shell
  [root@control-plane ~]# kubectl port-forward --address 0.0.0.0 -n istio-system istio-ingressgateway-7759d56fbd-59tgd 15009:15000
  Forwarding from 0.0.0.0:15009 -> 15000
  
  Handling connection for 15009
  Handling connection for 15009
  Handling connection for 15009
  ```

  然后用网页打开 http://IP:15009 找到`config dump`，然后点击进去卡岸envoy的具体配置！

