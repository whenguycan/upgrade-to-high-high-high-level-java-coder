## k8s搭建rabbitmq单机



#### k8s知识点

1. 无头service
2. statefulset方式部署应用
3. storageclass的使用



#### 搭建过程

- 直接运行下面的文件，就可以直接创建好

  ```yaml
  apiVersion: v1
  kind: Service
  metadata:
    name: rabbitmq-standalone-server
    namespace: lanzhen-membrance
    labels: #给当前service打个标签
      app: rabbitmq-standalone-server
  spec:
    ports:
      - name: rabbitmq-port
        port: 5672     #service的端口
        targetPort: 5672 #pod也就是容器的端口
      - name: rabbitmq-manage-port
        port: 15672     #service的端口
        targetPort: 15672 #pod也就是容器的端口
    clusterIP: None #无头service的标志
    selector: #用下面的标签来匹配管理的pod
      app: rabbitmq-standalone
  ---
  apiVersion: apps/v1
  kind: StatefulSet
  metadata:
    name: rabbitmq-standalone
    namespace: lanzhen-membrance
  spec:
    serviceName: rabbitmq-standalone-server   #该有状态应用归属于哪个service
    replicas: 1
    template: #下面是pod的配置
      metadata:
        labels: #给pod打上标签
          app: rabbitmq-standalone
      spec: #pod的规格属性
        containers: #下面是容器配置
        - name: rabbitmq-standalone
          image: rabbitmq:3.8.3-management
          env:
            - name: RABBITMQ_DEFAULT_USER
              value: "admin"
            - name: RABBITMQ_DEFAULT_PASS
              value: "Tangwei123456"
          resources:   #资源
            limits:
              cpu: 2
              memory: "2Gi"
            requests:    #请求的资源
              cpu: 100m   #m代表千分之,相当于0.1 个cpu资源
              memory: "300Mi"   #内存300m大小
          ports:
            - name: broker
              containerPort: 5672
              protocol: "TCP"
            - name: manager
              containerPort: 15672
              protocol: "TCP"
          volumeMounts: #挂载卷
            - name: rabbitmq-data
              mountPath: /var/lib/rabbitmq #挂载持久卷的路径
    volumeClaimTemplates: #创建一个storageClass的申请模板
      - metadata:
          name: rabbitmq-data
        spec:
          storageClassName: nfs-client-storageclass #指定storageclass名称，这里需要根据你的K8S集群进行修改
          accessModes: ["ReadWriteMany"]
          resources:
            requests:
              storage: 5Gi
    selector:
      matchLabels: #servic会使用下面两个label来挑选具体的pod，下面两个app是或者的关系
        app: rabbitmq-standalone
  
  ```

- 暴露端口提供web界面查看服务

  ```yaml
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



> 可以把5672端口从k8s中映射出来，进行测试

