## k8s搭建nacos单机



#### k8s知识点

1. 无状态应用部署
2. configmap的挂载使用





#### 搭建过程

准备工作：

- 下载nacos-server包，下载地址：https://github.com/alibaba/nacos/tags
- 数据需要持久化到mysql，所以需要准备一台mysql，并新建数据库nacos_config，把nacos-server包下的conf下的nacos-mysql.sql导入到库中。

> 注意：<font color="red">nacos-server的版本必须与下面使用的镜像版本一致！这儿以1.4.1为例</font>
>

- k8s集群中已经建立好了可以使用的storageclass，查看命令：kubectl get sc 



开始部署：

- 新建一个configmap，供后面的nacos的pod调用

  ```yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: nacos-standalone-cm
    namespace: lanzhen-membrance
  data:
    MYSQL_SERVICE_DB_NAME: "nacos_standalone"
    MYSQL_SERVICE_HOST: "192.168.2.235"
    MYSQL_SERVICE_PASSWORD: "tangwei123456"
    MYSQL_SERVICE_PORT: '30991'
    MYSQL_SERVICE_USER: "root"
  
  ```

- 使用Deployment方式部署nacos单机

  ```yaml
  apiVersion: apps/v1 #接口版本，所有的接口版本可用kubectl api-versions查看
  kind: Deployment
  metadata:
    name: nacos-standalone-with-mysql
    namespace: lanzhen-membrance
    labels: #给当前的deployment打上标签，key-value键值对的形式，key、value都可以自定义
      app: nacos-standalone-with-mysql
  spec:
    replicas: 1 #pod的数量
    selector:
      matchLabels: #需要管理的pod，通过pod的标签去匹配，只要符合下面的标签就会被纳入管理
        app: nacos-standalone-with-mysql
    template: #下面是定义pod的信息
      metadata:
        labels: #pod的标签，key-value的形式，通过标签归属他归哪个deployment管理
          app: nacos-standalone-with-mysql
      spec: #pod的描述信息
        containers: #pod容器信息，可以认为是docker的信息
          - name: nacos-standalone-with-mysql #pod的名称
            imagePullPolicy: Always  #镜像拉取策略
            image: nacos/nacos-server:1.4.1 #镜像地址
            resources: #pod的资源限制
              requests:
                memory: "2048Mi" #内存大小
                cpu: "1000m" #CPU的大小
            ports: #端口
              - containerPort: 8848  #容器端口
                protocol: TCP
            env: #环境变量
              - name: NACOS_REPLICAS
                value: "1"
              - name: nacos.naming.data.warmup  #是否在Server启动时进行数据预热
                value: "false"
              - name: MYSQL_SERVICE_HOST
                valueFrom:
                  configMapKeyRef:
                    name: nacos-standalone-cm #使用上面创建的configmap
                    key: MYSQL_SERVICE_HOST
              - name: MYSQL_SERVICE_DB_NAME
                valueFrom:
                  configMapKeyRef:
                    name: nacos-standalone-cm
                    key: MYSQL_SERVICE_DB_NAME
              - name: MYSQL_SERVICE_PORT
                valueFrom:
                  configMapKeyRef:
                    name: nacos-standalone-cm
                    key: MYSQL_SERVICE_PORT
              - name: MYSQL_SERVICE_USER
                valueFrom:
                  configMapKeyRef:
                    name: nacos-standalone-cm
                    key: MYSQL_SERVICE_USER
              - name: MYSQL_SERVICE_PASSWORD
                valueFrom:
                  configMapKeyRef:
                    name: nacos-standalone-cm
                    key: MYSQL_SERVICE_PASSWORD
  
  
  ```

- 暴露端口给外网使用，<font color="red">这一步要慎重！如果公开在外网 需要修改nacos的登录密码</font>

  ```yaml
  kind: Service
  apiVersion: v1
  metadata:
    name: nacos-nodeport  #跟当的微服务的项目名称一致
    namespace: legend #跟kubesphere的项目名称一致
    labels:
      app: nacos-cluster  #跟当前的微服务的项目名称一致
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

  