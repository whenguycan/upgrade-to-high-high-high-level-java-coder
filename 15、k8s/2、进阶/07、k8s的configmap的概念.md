## configmap概念



#### configmap用途

把用户的明文数据，不加密存储到etcd中，供后续pod调用。



#### 使用场景

比如，在部署redis的时候，会把redis的配置文件，存储起来，给后续的redis的pod调用。



#### 使用

- 创建一个configmap

  - 以键值对的形式

  ```yaml
  apiVersion: v1
  kind: ConfigMap  #新建一个configmap供nacos的使用
  metadata:
    name: nacos-configmap
    namespace: lanzhen-membrance
  data:
    MYSQL_SERVICE_HOST: "192.168.2.235" #nacos需要的数据库地址，一般为我们k8s集群中的内部地址
    MYSQL_SERVICE_DB_NAME: "nacos_config"  #nacos需要的数据库
    MYSQL_SERVICE_PORT: "30991" #数据库端口
    MYSQL_SERVICE_USER: "root"  #数据库用户名
    MYSQL_SERVICE_PASSWORD: "tangwei123456"  #数据库密码
  ```

  - 以文件的形式

  ```yaml
  kind: ConfigMap
  apiVersion: v1
  metadata:
    name: rabbitmq-cluster-config
    namespace: legend-pro
    labels:
      app: rabbitmq-cluster-configmap
  data:
    rabbitmq.conf: |
      default_user = admin
      default_pass = 15194196469TWtw
      cluster_formation.peer_discovery_backend = rabbit_peer_discovery_k8s
      cluster_formation.k8s.host = kubernetes.default.svc.cluster.local
      cluster_formation.k8s.address_type = hostname #从k8s返回的Pod容器列表中计算对等节点列表，这里只能使用主机名，官方示例中是ip，但是默认情况下在k8s中pod的ip都是不固定的，因此可能导致节点的配置和数据丢失，后面的yaml中会通过引用元数据的方式固定pod的主机名
      cluster_formation.node_cleanup.interval = 30
      cluster_formation.node_cleanup.only_log_warning = true
      cluster_partition_handling = autoheal
      queue_master_locator=min-masters
      loopback_users.guest = false
      cluster_formation.randomized_startup_delay_range.min = 0
      cluster_formation.randomized_startup_delay_range.max = 2
      cluster_formation.k8s.hostname_suffix = .rabbitmq-cluster.legend-pro.svc.cluster.local #注意这儿需要根据自己的namespace修改，原来是这样的 ".rabbitmq-cluster.default.svc.cluster.local"，把default改成自己的命名空间。
      vm_memory_high_watermark.absolute = 3GB
      disk_free_limit.absolute = 2GB
  ```

- pod中以Volume数据卷的方式使用configmap，注意：<font color="red">这种方式只能配合，文件形式创建的configmap</font>

  ```yaml
  kind: StatefulSet
  apiVersion: apps/v1
  metadata:
    labels:
      app: rabbitmq-cluster
    name: rabbitmq-cluster
    namespace: legend-pro
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: rabbitmq-cluster
    template:
      metadata:
        labels:
          app: rabbitmq-cluster
      spec:
        containers:
          ......
          ......
            volumeMounts:  #这儿挂载下面的volume
              - mountPath: /etc/rabbitmq   #挂载到这儿，这个目录下就会有rabbitmq.conf的配置文件。
                name: config-volume  
                readOnly: false
        volumes:
          - name: config-volume  #创建一个volume卷
            configMap:
              items:
                - key: rabbitmq.conf   #这儿填写configmap文件名
                  path: rabbitmq.conf   #这儿填写映射到pod中的名称
              name: rabbitmq-cluster-config  #这儿填写configmap的名称
  ```

  

- pod中以环境变量的方式使用configmap，注意：<font color="red">这种方式只能配合，键值对形式创建的configmap</font>

  ```yaml
  apiVersion: apps/v1
  kind: StatefulSet 
  metadata:
    name: nacos-cluster
    namespace: lanzhen-membrance
    labels: 
      app: nacos-cluster
  spec: 
    replicas: 3 
    selector:
      matchLabels: 
        app: nacos-cluster
    template: 
      metadata:
        labels:
          app: nacos-cluster
      spec: 
        containers: 
          ......
          ......
          env:
              - name: MYSQL_SERVICE_HOST
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap  #从上面定义的configmap中读取配置
                    key: MYSQL_SERVICE_HOST
              - name: MYSQL_SERVICE_DB_NAME
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap
                    key: MYSQL_SERVICE_DB_NAME
              - name: MYSQL_SERVICE_PORT
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap
                    key: MYSQL_SERVICE_PORT
              - name: MYSQL_SERVICE_USER
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap
                    key: MYSQL_SERVICE_USER
              - name: MYSQL_SERVICE_PASSWORD
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap
                    key: MYSQL_SERVICE_PASSWORD
              - name: NACOS_SERVER_PORT
                value: "8848"
              - name: NACOS_APPLICATION_PORT
                value: "8848"
              - name: PREFER_HOST_MODE
                value: "hostname"
  
  
  ```

  