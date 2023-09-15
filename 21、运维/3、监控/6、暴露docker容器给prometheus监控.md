## 暴露docker容器给prometheus监控

> 注意：这样监控的是所有的docker容器，而非容器内的服务



#### 1、拉取镜像并启动暴露服务的容器

```shell
docker run --publish=8081:8080 -v /:/rootfs:ro -v /var/run:/var/run:ro -v /sys:/sys:ro -v /var/lib/docker/:/var/lib/docker:ro -v /dev/disk/:/dev/disk:ro --detach=true --name cadvisor google/cadvisor 
```

因为是监控所有的docker容器，所以上面命令中的 -v 是不能少的



#### 2、打开上面暴露的端口，查看容器是否运行正常

```shell
http://IP:8081/metrics
```



#### 3、修改prometheus的配置文件，将docker纳入监控

```shell
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    metrics_path: /metrics
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']
  - job_name: 'node'
    static_configs:
      - targets: ['192.168.1.21:9100']
    params:
      collect[]:
        - cpu
        - meminfo
        - diskstats
        - netdev
        - systemd

  - job_name: 'docker'           #添加
    static_configs: 
      - targets: ['192.168.1.20:8081']
```



#### 4、重载配置

```shell
curl -X POST http://IP:9090/-/reload
```

