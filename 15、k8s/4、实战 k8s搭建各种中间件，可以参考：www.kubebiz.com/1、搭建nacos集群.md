## k8s搭建nacos集群



#### k8s知识点

1. 无头service的使用
2. 有状态应用部署
3. configmap以全局变量的方式使用
4. storageClass挂载使用



#### 搭建过程

准备工作：

- 下载nacos-server包，<font color="red">不要下载源码包</font>，下载地址：https://github.com/alibaba/nacos/tags
- 数据需要持久化到mysql，所以需要准备一台mysql，并新建数据库nacos_config，把nacos-server包下的conf下的nacos-mysql.sql导入到库中。

> 注意：<font color="red">nacos-server的版本必须与下面使用的镜像版本一致！这儿以1.4.1为例</font>
>
> 官方参考：https://nacos.io/zh-cn/docs/use-nacos-with-kubernetes.html 我们在部署的时候，不用他的rbac机制！

- k8s集群中已经建立好了可以使用的storageclass，查看命令：kubectl get sc 



开始部署：

- 新建一个configmap，供后面的nacos的pod调用

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

- 创建一个无头Service(这个可以去掉)、并使用StatefulSet方式部署nacos集群

  ```yaml
  ---
  apiVersion: v1
  kind: Service #
  metadata:
    name: nacos-cluster
    namespace: lanzhen-membrance
    labels: #给service打上一个标签
      app: nacos-cluster
    annotations: #自定义注释列表
      service.alpha.kubernetes.io/tolerate-unready-endpoints: "true"
  spec: #service的详细定义
    ports:
      - name: server
        port: 8848     #service的端口
        targetPort: 8848 #pod也就是容器的端口
      - name: client-rpc
        port: 9848
        targetPort: 9848
      - name: raft-rpc
        port: 9849
        targetPort: 9849
      - name: old-raft-rpc     #兼容1.4.x版本的选举端口
        port: 7848
        targetPort: 7848
    clusterIP: None #Node为无头service
    type: ClusterIP #默认的service类型，可以改为NodePort来暴露service的端口
    selector: #通过下面的标签去匹配可管理的对应的pod
      app: nacos-cluster
  ---
  apiVersion: apps/v1
  kind: StatefulSet #有状态的应用部署
  metadata:
    name: nacos-cluster
    namespace: lanzhen-membrance
    labels: #给当前的应用打上标签
      app: nacos-cluster
  spec: #应用的规格
    replicas: 3 #pod的数量
    serviceName: nacos-cluster #告知被哪个无头service关联。如果关联的service是无头的，那么这儿是必填的。
    selector:
      matchLabels: #去匹配如下标签的pod，把pod纳入到管理中
        app: nacos-cluster
    template: #下面定义pod的信息
      metadata:
        labels: #pod的标签
          app: nacos-cluster
        annotations:
          pod.alpha.kubernetes.io/initialized: "true"
      spec: #pod的规格
        terminationGracePeriodSeconds: 10  #配置优雅停机时间
        initContainers:                    #初始化镜像执行初始化操作
          - name: peer-finder-plugin-install #安装一个nacos的插件
            image: nacos/nacos-peer-finder-plugin:1.1 #Nacos集群进行动态扩容的插件
            volumeMounts:
              - mountPath: "/home/nacos/plugins/peer-finder"
                name: nacos-dir
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
                          - nacos-cluster
        containers: #每个pod容器的属性
          - name: nacos-cluster #pod容器的名称
            image: nacos/nacos-server:1.4.1 #镜像
            resources:
              limits:
                cpu: 2
                memory: "2Gi"
              requests:
                cpu: 500m
                memory: "2Gi"
            ports:
              - name: client-port
                containerPort: 8848
              - name: client-rpc
                containerPort: 9848
              - name: raft-rpc
                containerPort: 9849
              - name: olf-raft-rpc
                containerPort: 7848
                
            env:
              - name: NACOS_REPLICAS
                value: "3"
              - name: SERVICE_NAME   #这个的值写成跟上面无头service一样的
                value: "nacos-cluster"
              - name: DOMAIN_NAME     #这个的值一般不变，固定为cluster.local
                value: "cluster.local"
              - name: POD_NAMESPACE
                value: lanzhen-membrance
              - name: MYSQL_SERVICE_HOST
                valueFrom:
                  configMapKeyRef:
                    name: nacos-configmap #从上面定义的configmap中读取配置
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
            volumeMounts: #pod中容器的挂在卷
              - name: nacos-dir
                mountPath: /home/nacos/plugins/peer-finder
              - name: nacos-dir
                mountPath: /home/nacos/data
              - name: nacos-dir
                mountPath: /home/nacos/logs
    volumeClaimTemplates: #定义一个存储卷申请模板
      - metadata:
          name: nacos-dir #模板的名称
        spec:
          storageClassName: nfs-client-storageclass   #存储类名
          accessModes: [ "ReadWriteMany" ]
          resources:
            requests:
              storage: 20Gi
  
  ```

- 对nacos进行动态扩容

  ```tex
  kubectl scale sts nacos-cluster -n 命名空间 --replicas=数量
  ```

  

- 如果部署中有问题，可以进到对象的nacos的pod中查看日志，日志位于/home/nacos/logs/start.out中

- 暴露端口给外网使用，<font color="red">这一步要慎重！如果公开在外网 需要修改nacos的登录密码</font>

  ```yaml
  kind: Service
  apiVersion: v1
  metadata:
    name: nacos-nodeport  
    namespace: legend #跟nacos的项目名称一致
    labels:
      app: nacos-cluster  
  spec:
    ports:
      - name: http
        protocol: TCP
        port: 8848        #service暴露的端口
        targetPort: 8848  #容器内暴露端口
        nodePort: 32756   #NodePort代理的端口，每个service不一样就行，从30000开始才能使用
    selector:
      app: nacos-cluster
    type: NodePort #一旦指定了NodePort，集群就会给他分配一个clusterIP
    sessionAffinity: None
  ```

  

