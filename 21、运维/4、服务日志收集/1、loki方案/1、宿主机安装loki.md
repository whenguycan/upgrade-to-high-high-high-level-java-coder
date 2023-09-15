## loki安装

> 此安装方案把loki的所有组件安装在一起

#### 1、下载loki安装包

下载地址：https://github.com/grafana/loki/tags?after=helm-loki-4.5.1



#### 2、解压安装包

```shell
unzip loki-linux-amd64.zip
```

解压之后就直接得到一个可执行的文件



#### 3、授予可执行文件可执行权限

```shell
chmod a+x loki-linux-amd64
```



#### 4、修改可执行文件的文件名

```shell
mv loki-linux-amd64 loki
```



#### 5、移动文件到/usr/local/bin中

```shell
mv loki /usr/local/bin/
```



#### 6、创建一个loki的目录

在目录中创建config、data目录



#### 6、在config目录中创建一个loki的配置文件,内容如下

```yaml
auth_enabled: false  #是否开启认证，在多租户下才开启，单租户不用开启。

server:
  http_listen_port: 3100 #loki的监听端口
  grpc_server_max_concurrent_streams: 0

ingester: #这个模块使用默认配置即可
  lifecycler:
    address: 127.0.0.1
    ring:
      kvstore:
        store: inmemory
      replication_factor: 1
    final_sleep: 0s
  chunk_idle_period: 1h       # Any chunk not receiving new logs in this time will be flushed
  max_chunk_age: 1h           # All chunks will be flushed when they hit this age, default is 1h
  chunk_target_size: 10485760  # Loki will attempt to build chunks up to 1.5MB, flushing first if chunk_idle_period or max_chunk_age is reached first
  chunk_retain_period: 30s    # Must be greater than index read cache TTL if using an index cache (Default index read cache TTL is 5m)
  max_transfer_retries: 0     # Chunk transfers disabled

schema_config:
  configs:
    - from: 2020-10-24
      store: boltdb-shipper
      object_store: filesystem
      schema: v11
      index:
        prefix: index_
        period: 24h
# 存储配置
storage_config:
  boltdb_shipper:
    active_index_directory: /data/loki/boltdb-shipper-active
    cache_location: /data/loki/boltdb-shipper-cache
    cache_ttl: 24h         # Can be increased for faster performance over longer query periods, uses more disk space
    shared_store: filesystem
  filesystem:
    directory: /data/loki/chunks


compactor:
  working_directory: /data/loki/boltdb-shipper-compactor #数据压缩之后的存放地
  shared_store: filesystem


limits_config:
  enforce_metric_name: false
  reject_old_samples: true
  reject_old_samples_max_age: 168h
  ingestion_rate_mb: 200
  # ingestion_burst_size_mb: 400
  # max_streams_per_user: 0
  # max_chunks_per_query: 20000000
  # max_query_parallelism: 140
  # max_query_series: 5000
  # cardinality_limit: 1000000
  # max_streams_matchers_per_query: 10000

chunk_store_config:
  max_look_back_period: 0s

# 数据保留时间
table_manager:
  retention_deletes_enabled: true
  retention_period: 24h #数据保留24小时

ruler:
  storage:
    type: local
    local:
      directory: /data/loki/rules
  rule_path: /data/loki/rules-temp
  alertmanager_url: http://localhost:9093
  ring:
    kvstore:
      store: inmemory
  enable_api: true
```

配置文件中使用到的目录都会被自动创建



#### 7、启动loki

```shell
nohup loki -log.level=info -target all -config.file=loki-local-config.yaml > loki.log &
```

其中 `-log.level=info`是设置日志的级别，`-target all`是启动所有的loki组件 `-config.file=loki-local-config.yaml`是指定loki的配置文件。



到这儿loki安装完成，去grafana中添加loki的数据源的时候，会提示没有label数据，是正常现象。