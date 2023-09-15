## prometheus过滤掉不要的指标

默认node-exporter会提供数十种类型的指标进行收集，但是并不是所有的指标都需要监控

我们可以通过过滤来筛选我们要收集那种类型的指标数据，来减少各种资源的消耗



```tex
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

    params:         #添加以下配置，配置在这儿的就不会去收集了
      collect[]:
        - cpu
        - meminfo
        - diskstats
        - netdev
        - systemd
```

重载配置

```shell
curl -X POST http://IP:9090/-/reload
```

