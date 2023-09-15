## 搭建loki集群



#### 1、准备loki-rbac.yaml文件

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: loki
  namespace: parking
---
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: loki
  namespace: parking
rules:
- apiGroups: ["extensions"]
  resources: ["podsecuritypolicies"] # podsecuritypolicies这个资源属于extensions（api groups），所以apiGroups: ["extensions"]这样写
  verbs: ["use"]
  resourceNames: [loki]
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: loki
  namespace: parking
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: loki
subjects:
- kind: ServiceAccount
  name: loki
```



#### 2、准备loki-configmap.yaml

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: loki
  namespace: parking
  labels:
    app: loki
data:
  loki.yaml: |
    auth_enabled: false
    ingester:
      chunk_idle_period: 3m
      chunk_block_size: 262144
      chunk_retain_period: 1m
      max_transfer_retries: 0
      lifecycler:
        ring:
          kvstore:
            store: inmemory
          replication_factor: 1
    limits_config:
      enforce_metric_name: false
      reject_old_samples: true
      reject_old_samples_max_age: 168h
    schema_config:
      configs:
      - from: "2022-05-15"
        store: boltdb-shipper
        object_store: filesystem
        schema: v11
        index:
          prefix: index_
          period: 24h
    server:
      http_listen_port: 3100
    storage_config: # 存储配置
      boltdb_shipper:
        active_index_directory: /data/loki/boltdb-shipper-active
        cache_location: /data/loki/boltdb-shipper-cache
        cache_ttl: 24h         
        shared_store: filesystem
      filesystem:
        directory: /data/loki/chunks
    chunk_store_config:
      max_look_back_period: 0s
    table_manager:
      retention_deletes_enabled: true
      retention_period: 48h
    compactor:
      working_directory: /data/loki/boltdb-shipper-compactor #数据压缩之后的存放地
      shared_store: filesystem
```



#### 3、准备statefulset配置文件loki-statefulset.yaml

```yaml
apiVersion: v1
kind: Service
metadata:
  name: loki
  namespace: parking
  labels:
    app: loki
spec:
  type: NodePort
  ports:
    - port: 3100
      protocol: TCP
      name: http-metrics
      targetPort: http-metrics
      nodePort: 30201
  selector:
    app: loki
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: loki
  namespace: parking
  labels:
    app: loki
spec:
  podManagementPolicy: OrderedReady
  replicas: 1
  selector:
    matchLabels:
      app: loki
  serviceName: loki
  updateStrategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        app: loki
    spec:
      serviceAccountName: loki
      initContainers:
      - name: chmod-data
        image: busybox:1.28.4
        imagePullPolicy: IfNotPresent
        command: ["chmod","-R","777","/loki/data"]
        volumeMounts:
        - name: storage
          mountPath: /loki/data
      containers:
        - name: loki
          image: grafana/loki:2.3.0
          imagePullPolicy: IfNotPresent
          args:
            - -config.file=/etc/loki/loki.yaml
          volumeMounts:
            - name: config
              mountPath: /etc/loki
            - name: storage
              mountPath: /data
          ports:
            - name: http-metrics
              containerPort: 3100
              protocol: TCP
          securityContext:
            readOnlyRootFilesystem: true
      terminationGracePeriodSeconds: 4800
      volumes:
        - name: config #将配置文件以volume挂载进来使用
          configMap:
            name: loki
        - name: storage #将pvc以volume的形式挂载使用
          persistentVolumeClaim:
            claimName: my-pvc-loki  #这儿需要手动去创建一个pvc

```



#### 4、使用命令去查看loki是否部署成功

```shell
curl http://172.16.4.141:30201/ready
```

正常返回 证明loki安装成功



#### 5、可以使用springboot项目的镜像，用docker直接往loki中写入日志进行测试

