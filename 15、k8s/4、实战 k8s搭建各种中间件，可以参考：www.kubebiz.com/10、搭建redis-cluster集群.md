## 搭建redis-cluster集群





#### k8s知识点

1. 无头service
2. 有状态应用部署
3. configmap挂载使用
4. storageClass挂载使用
5. pod的DNS
6. 初始化redis-cluster集群



#### 参考资料

https://cloud.tencent.com/developer/article/1392872

> 这样方式搭建的集群，只有应用部署到k8s内，才能使用



#### 搭建过程

- 创建一个configmap，供后面的redis的pod使用<font color="red">（需要去掉其中的注释，不然可能会出问题）</font>

  ```yaml
  kind: ConfigMap
  apiVersion: v1
  metadata:
    name: redis-cluster-conf
    namespace: lanzhen-membrance
    labels:
      app: redis-cluster-conf
    annotations:
      kubesphere.io/alias-name: redis的配置文件
      kubesphere.io/description: redis-conf配置文件
  data:
    redis-conf: |-
      appendonly yes                      #开启Redis的AOF持久化，不太推荐使用aof的方式做数据的持久化
      cluster-enabled yes                 #集群模式打开
      cluster-config-file nodes.conf      #下面说明
      cluster-node-timeout 5000           #节点超时时间
      port 6379                           #开启的端口
  
  ```

- 创建redis的无头service、pod等资源 <font color="red"> pod中需要使用上面创建的configmap</font>

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: redis-headless
    namespace: lanzhen-membrance
    labels: #给redis的service打上标签
      app: redis
  spec: #service的规格信息
    ports:
      - name: redis-port
        port: 6379     #service的端口
        targetPort: 6379 #pod也就是容器的端口
    clusterIP: None #无头service固定格式
    selector: #通过下面定义的标签去匹配具体的pod
      app: redis
      appCluster: redis-cluster
  ---
  apiVersion: apps/v1
  kind: StatefulSet  #有状态的应用部署
  metadata:
    name: redis
    namespace: lanzhen-membrance
  spec:
    serviceName: redis-headless   #该有状态应用归属于哪个service
    replicas: 6 #pod的数量
    template: #下面是pod的信息
      metadata:
        labels: #给pod加上标签
          app: redis
          appCluster: redis-cluster
      spec:
        terminationGracePeriodSeconds: 10  #配置优雅停机时间
        affinity:    #配置Pod反亲和性，放置Pod都起在同一节点上（如果都在一个节点，节点宕机将会使全部实例不可用）       
          podAntiAffinity:
            preferredDuringSchedulingIgnoredDuringExecution:  #软策略，尽可能pod不启在同一个节点上
              - weight: 100 
                podAffinityTerm:
                  topologyKey: "kubernetes.io/hostname"
                  labelSelector:
                    matchExpressions:
                      - key: app
                        operator: In
                        values:
                          - redis  
        containers: #容器的属性
        - name: redis
          image: redis:6.0.11
          command: 
            - "redis-server"       #redis的启动命令
          args:
            - "/etc/redis/redis.conf" #redis-server后面跟的参数,换行代表空格
            - "--protected-mode" #允许外网访问
            - "no"                #组成命令： redis-server /etc/redis/redis.conf --protected-mode no
          resources:   #资源
            limits:
              cpu: 2
              memory: "2Gi"
            requests:    #请求的资源
              cpu: 100m   #m代表千分之,相当于0.1 个cpu资源
              memory: "300Mi"   #内存300m大小
          ports: #容器开放端口
            - name: redis
              containerPort: 6379
              protocol: "TCP"
            - name: cluster
              containerPort: 16379
              protocol: "TCP"
          volumeMounts: #挂载创建好的卷
            - name: redis-conf-map #挂载configmap生成的文件
              mountPath: /etc/redis #挂载到哪个路径下
            - name: redis-data #挂载持久卷的路径
              mountPath: /var/lib/redis
        volumes: #创建卷
          - name: redis-conf-map #创建卷的名称
            configMap: #创建卷使用configMap
              name: redis-cluster-conf  #configMap的名称
              items:
                - key: redis-conf #创建configMap里设置的名称
                  path: redis.conf #把configMap里面设置的值，隐射到redis.conf文件中
    volumeClaimTemplates: ##定义一个存储卷申请模板
      - metadata:
          name: redis-data
        spec:
          storageClassName: nfs-client-storageclass #指定storageclass名称，这里需要根据你的K8S集群进行修改
          accessModes: ["ReadWriteMany"]
          resources:
            requests:
              storage: 5Gi
    selector:
      matchLabels: #应用会使用下面两个label来挑选具体的pod，下面两个app是或者的关系
        app: redis
        appCluster: redis-cluster
  
  ```

- 由于Redis集群必须在所有节点启动后才能进行初始化，而如果将初始化逻辑写入Statefulset中，则是一件非常复杂而且低效的行为。我们可以在K8S上创建一个额外的centos的容器，可以在该容器中安装Redis-tribe，进而初始化Redis集群。按顺序执行如下命令：

  - kubectl run -i --tty centos --image=centos --restart=Never /bin/bash

  - cat >> /etc/yum.repos.d/epel.repo<<'EOF'

    [epel]

    name=Extra Packages for Enterprise Linux 7 - $basearch

    baseurl=https://mirrors.tuna.tsinghua.edu.cn/epel/7/$basearch

    \#mirrorlist=https://mirrors.fedoraproject.org/metalink?repo=epel-7&arch=$basearch

    failovermethod=priority

    enabled=1

    gpgcheck=0

    gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-EPEL-7

    EOF

  - yum -y install redis-trib bind-utils

  - 每一个redis的pod都有一个内部的dns，组成元素为：pod名称.service名称.namespace名称.svc.cluster.local（<font color="red">这儿的前提是redis的配置文件中不能设置redis的密码，不然这儿会连接不上！！！</font>）

    redis-trib create --replicas 1 \

    \`dig +short redis-app-0.redis-service.default.svc.cluster.local\`:6379 \

    \`dig +short redis-app-1.redis-service.default.svc.cluster.local`:6379 \

    \`dig +short redis-app-2.redis-service.default.svc.cluster.local`:6379 \

    \`dig +short redis-app-3.redis-service.default.svc.cluster.local`:6379 \

    \`dig +short redis-app-4.redis-service.default.svc.cluster.local`:6379 \

    \`dig +short redis-app-5.redis-service.default.svc.cluster.local`:6379    #用于将Pod的域名转化为IP，这是因为redis-trib不支持域名来创建集群。

  - 集群搭建完毕

    - 进入到一个redis中查看集群情况：kubectl exec -it redis-2 --namespace lanzhen-membrance /bin/bash
    - /usr/local/bin/redis-cli -c 连接上redis之后（-c 千万不能少）， 输入：cluster info 或者cluster nodes