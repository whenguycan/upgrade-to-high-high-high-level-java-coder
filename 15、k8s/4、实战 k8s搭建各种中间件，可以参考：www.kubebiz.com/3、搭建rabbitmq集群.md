## k8s搭建rabbitmq集群



官方参考：https://github.com/rabbitmq/diy-kubernetes-examples/tree/master/minikube，它里面都写好了各种yaml文件。

>以下的部署，需要的rabbitMQ的版本至少为3.7.0



#### k8s知识点

1. configmap的挂载使用
2. rabc的serviceaccount的使用
3. 无头service和普通service的使用
4. statefulset方式部署应用
5. storageclass的使用



#### 概述

集群的创建过程，首先挨个启动rabbitMQ所有的pod，每个rabbitMQ 所在的pod需要通过 rabbitmq_peer_discovery_k8s plugin 这个插件，去k8s中的 api server中获取另外rabbitMQ的pod的url地址，拿到URL之后就可以建立集群。



#### 搭建过程

- 创建一个configmap给后续使用，<font color="red">复制下面的yaml代码时去掉注释。</font>

  ```yaml
  kind: ConfigMap
  apiVersion: v1
  metadata:
    name: rabbitmq-cluster-config
    namespace: legend-pro
    labels:
      app: rabbitmq-cluster-configmap
  data:
    enabled_plugins: |
      [rabbitmq_management,rabbitmq_peer_discovery_k8s]. #使用rabbitmq_management、rabbitmq_peer_discovery_k8s这俩插件
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

- 创建一个service

  ```yaml
  kind: Service
  apiVersion: v1
  metadata:
    labels:
      app: rabbitmq-cluster
    name: rabbitmq-cluster
    namespace: legend-pro
  spec:
    clusterIP: None
    ports:
      - name: rmqport
        port: 5672
        targetPort: 5672
    selector:
      app: rabbitmq-cluster
  
  ---
  kind: Service
  apiVersion: v1
  metadata:
    labels:
      app: rabbitmq-cluster
    name: rabbitmq-cluster-manage
    namespace: legend-pro
  spec:
    ports:
      - name: http
        port: 15672
        protocol: TCP
        targetPort: 15672
        nodePort: 32734
    selector:
      app: rabbitmq-cluster
    type: NodePort
  ```

- 创建rbac授权，因为rabbitmq所在pod需要去k8s的api server中获取其它rabbitmq的pod的URL，所以需要授权：

  ```yaml
  apiVersion: v1
  kind: ServiceAccount
  metadata:
    name: rabbitmq-cluster
    namespace: legend-pro
  ---
  kind: Role
  apiVersion: rbac.authorization.k8s.io/v1beta1
  metadata:
    name: rabbitmq-cluster
    namespace: legend-pro
  rules:
    - apiGroups: [""]
      resources: ["endpoints"]
      verbs: ["get"]
  ---
  kind: RoleBinding
  apiVersion: rbac.authorization.k8s.io/v1beta1
  metadata:
    name: rabbitmq-cluster
    namespace: legend-pro
  roleRef:
    apiGroup: rbac.authorization.k8s.io
    kind: Role
    name: rabbitmq-cluster
  subjects:
    - kind: ServiceAccount
      name: rabbitmq-cluster
      namespace: legend-pro
  ```

- 使用statefulset方式创建rabbitmq

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
    serviceName: rabbitmq-cluster
    template:
      metadata:
        labels:
          app: rabbitmq-cluster
      spec:
        containers:
          - args:
              - -c
              - cp -v /etc/rabbitmq/rabbitmq.conf ${RABBITMQ_CONFIG_FILE}; exec docker-entrypoint.sh
                rabbitmq-server
            command:
              - sh
            env:
              - name: TZ
                value: 'Asia/Shanghai'
              - name: RABBITMQ_ERLANG_COOKIE
                value: 'SWvCP0Hrqv43NG7GybHC95ntCJKoW8UyNFWnBEWG8TY='
              - name: K8S_SERVICE_NAME
                value: rabbitmq-cluster
              - name: POD_IP
                valueFrom:
                  fieldRef:
                    fieldPath: status.podIP
              - name: POD_NAME
                valueFrom:
                  fieldRef:
                    fieldPath: metadata.name
              - name: POD_NAMESPACE
                valueFrom:
                  fieldRef:
                    fieldPath: metadata.namespace
              - name: RABBITMQ_USE_LONGNAME
                value: "true"
              - name: RABBITMQ_NODENAME
                value: rabbit@$(POD_NAME).$(K8S_SERVICE_NAME).$(POD_NAMESPACE).svc.cluster.local
              - name: RABBITMQ_CONFIG_FILE
                value: /var/lib/rabbitmq/rabbitmq.conf
            image: rabbitmq:3.8.3-management
            imagePullPolicy: IfNotPresent
            livenessProbe:
              exec:
                command:
                  - rabbitmq-diagnostics
                  - status
              initialDelaySeconds: 60 # See https://www.rabbitmq.com/monitoring.html for monitoring frequency recommendations.
              periodSeconds: 60
              timeoutSeconds: 15
            name: rabbitmq
            ports:
              - containerPort: 15672
                name: http
                protocol: TCP
              - containerPort: 5672
                name: amqp
                protocol: TCP
            readinessProbe:
              exec:
                command:
                  - rabbitmq-diagnostics
                  - status
              initialDelaySeconds: 20
              periodSeconds: 60
              timeoutSeconds: 10
            volumeMounts:
              - mountPath: /etc/rabbitmq
                name: config-volume
                readOnly: false
              - mountPath: /var/lib/rabbitmq
                name: rabbitmq-storage
                readOnly: false
              - name: timezone
                mountPath: /etc/localtime
                readOnly: true
        serviceAccountName: rabbitmq-cluster
        terminationGracePeriodSeconds: 30
        volumes:
          - name: config-volume
            configMap:
              items:
                - key: rabbitmq.conf
                  path: rabbitmq.conf
                - key: enabled_plugins
                  path: enabled_plugins
              name: rabbitmq-cluster-config
          - name: timezone
            hostPath:
              path: /usr/share/zoneinfo/Asia/Shanghai
    volumeClaimTemplates:
      - metadata:
          name: rabbitmq-storage
        spec:
          accessModes:
            - ReadWriteMany
          storageClassName: "nfs-client-storageclass"
          resources:
            requests:
              storage: 2Gi
  ```

  创建statefulset的过程可能有点久，需要耐心等待，中途出现“容器没有准备就绪”不用管。



- 等待所有的pod启动完成，进入到某一个pod中，运行 rabbitmqctl cluster_status，如果如下就证明成功了！

  > Basics
  >
  > Cluster name: rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local
  >
  > Disk Nodes
  >
  > rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local
  > rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local
  > rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local
  >
  > Running Nodes
  >
  > rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local
  > rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local
  > rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local
  >
  > Versions
  >
  > rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local: RabbitMQ 3.8.3 on Erlang 22.3.4.1
  > rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local: RabbitMQ 3.8.3 on Erlang 22.3.4.1
  > rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local: RabbitMQ 3.8.3 on Erlang 22.3.4.1
  >
  > 
  >
  > Alarms
  >
  > (none)
  >
  > Network Partitions
  >
  > (none)
  >
  > Listeners
  >
  > Node: rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 25672, protocol: clustering, purpose: inter-node and CLI tool communication
  > Node: rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 5672,protocol: amqp, purpose: AMQP 0-9-1 and AMQP 1.0
  > Node: rabbit@rabbitmq-cluster-0.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 15672, protocol: http, purpose: HTTP API
  > Node: rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 25672, protocol: clustering, purpose: inter-node and CLI tool communication
  > Node: rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 5672,protocol: amqp, purpose: AMQP 0-9-1 and AMQP 1.0
  > Node: rabbit@rabbitmq-cluster-1.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 15672, protocol: http, purpose: HTTP API
  > Node: rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 25672, protocol: clustering, purpose: inter-node and CLI tool communication
  > Node: rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 5672,protocol: amqp, purpose: AMQP 0-9-1 and AMQP 1.0
  > Node: rabbit@rabbitmq-cluster-2.rabbitmq-cluster.legend.svc.cluster.local, interface: [::], port: 15672, protocol: http, purpose: HTTP API
  >
  > Feature flags
  >
  > Flag: drop_unroutable_metric, state: enabled
  > Flag: empty_basic_get_metric, state: enabled
  > Flag: implicit_default_bindings, state: enabled
  > Flag: quorum_queue, state: enabled
  > Flag: virtual_host_metadata, state: enabled

  

- 发现configmap中的用户名和密码，可以正常登录，如何新增管理员呢？

  1. rabbitmqctl add_user 新用户名 密码
  2. rabbitmqctl set_user_tags 新用户名 administrator
  3. rabbitmqctl set_permissions -p / 新用户名 ".*" ".*" ".*"

  
  
  > 可以把5672端口从k8s中映射出来，进行测试
  
  