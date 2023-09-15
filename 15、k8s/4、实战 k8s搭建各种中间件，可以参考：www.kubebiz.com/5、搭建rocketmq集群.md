## k8s搭建RocketMQ集群



#### 搭建过程

> broker集群方式为：2主2从，异步刷盘，异步复制



准备工作：

- 官网下载rocketmq的二进制包，在制作broker和name server的时候需要用2次，所以要下载2个。下载地址为：https://rocketmq.apache.org/dowloading/releases/
- 下载好java8的jdk包，制作broker镜像的时候需要使用，下载地址：https://www.aliyundrive.com/drive/
- 下载好rocketmq-console的源码，这个是一个典型的springboot 项目，下载地址：https://github.com/apache/rocketmq-externals/tags 



开始部署：

- 制作自己的broker镜像

  > 为了方便后续我们需要修改broker的配置参数，所以选择自己制作broker的镜像

  1. 解压下载好的zip包

     ```tex
     unzip rocketmq-all-4.9.1-bin-release.zip
     ```

  2. 修改runbroker.sh脚本文件里面的内存设置参数，<font color="red">具体还要改哪些，看需求</font>。

     ```java
     " JAVA_OPT= "${JAVA_OPT} -server -Xms1g -Xmx1g -Xmn512m" "
     ```

  3. 将上面解压好并完成修改的文件，重新打成tar包，因为后面docker要使用ADD命令把这个包放到镜像中去，所以要打成tar包

     ```shell
     tar -zvcf rocketmq-all-4.9.1-bin-release.tar.gz rocketmq-all-4.9.1-bin-release
     ```

  4. 编写dockerfile，准备构建镜像，注意要去掉其中的注释

     ```dockerfile
     FROM centos:7
     
     RUN mkdir -p /java8
     
     WORKDIR /java8
     
     ADD ./jdk8.tar.gz ./   #将java8的JDK包放到镜像中
     
     ENV JAVA_HOME=/java8/jdk1.8.0_301
     ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
     ENV PATH=$JAVA_HOME/bin:$PATH
     
     ENV LANG en_US.UTF-8
     
     ADD rocketmq-all-4.9.1-bin-release.tar.gz /usr/local/
     RUN mv /usr/local/rocketmq-all-4.9.1-bin-release /usr/local/rocketmq-4.9.1 && mkdir -p /data/rocketmq/store
     
     EXPOSE 11911
     EXPOSE 11909
     EXPOSE 11912
     
     CMD ["sh", "/usr/local/rocketmq-4.9.1/bin/mqbroker", "&"]
     ```

  5. 制作镜像

     ```shell
     docker build -f Dockerfile -t rocketmq:4.9.1 .
     ```

  6. 给制作好的镜像打上tag

     ```shell
     docker tag 镜像ID registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/rocketmq:4.9.1
     ```

  7. 将镜像推送到阿里云：

     ```shell
     docker push registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/rocketmq:4.9.1
     ```

  

- 制作自己的name server镜像

  1. 解压下载好的zip包：

     ```shell
     unzip rocketmq-all-4.8.0-bin-release.zip
     ```

  2. 修改runserver.sh脚本文件里的内存设置

     ```shell
      JAVA_OPT="${JAVA_OPT} -server -Xms4g -Xmx4g -Xmn2g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
     ```

  3. 将上面解压好的文件，重新打成tar包，因为后面docker要使用ADD命令把这个包放到镜像中去，所以要打成tar包

     ```shell
     tar -zvcf rocketmq-all-4.9.1-bin-release.tar.gz rocketmq-all-4.9.1-bin-release
     ```

  4. 编写Dockerfile，准备构建镜像，注意要去掉其中的注释

     ```dockerfile
     FROM centos:7
     
     RUN mkdir -p /java8
     
     WORKDIR /java8
     
     ADD ./jdk8.tar.gz ./   #将java8的JDK包放到镜像中
     
     ENV JAVA_HOME=/java8/jdk1.8.0_301
     ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
     ENV PATH=$JAVA_HOME/bin:$PATH
     
     ENV LANG en_US.UTF-8
     
     ADD rocketmq-all-4.9.1-bin-release.tar.gz /usr/local/
     RUN mv /usr/local/rocketmq-all-4.9.1-bin-release /usr/local/rocketmq-4.9.1
     
     EXPOSE 9876
     
     CMD ["sh", "/usr/local/rocketmq-4.9.1/bin/mqnamesrv", "&"]
     ```

  5. 运行如下命令，制作镜像：

     ```shell
     docker build -f Dockerfile -t nameserver:4.9.1 .
     ```

  6. 给制作好的镜像打上新的tag

     ```shell
     docker tag 镜像ID registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/nameserver:4.9.1
     ```

  7. 将镜像推送到阿里云

     ```shell
     docker push registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/nameserver:4.9.1
     ```

  

- 部署name server集群

  ```yaml
  ---
  apiVersion: v1
  kind: Service
  metadata:
    name: rocketmq-nameserver-cluster-service
    namespace: public-pro
    labels:
      app: rocketmq-nameserver-cluster-service
  spec:
    ports:
      - name: server-port
        port: 9876
        targetPort: 9876
    clusterIP: None
    type: ClusterIP
    selector:
      app: rocketmq-nameserver-cluster
  ---
  apiVersion: apps/v1
  kind: StatefulSet
  metadata:
    namespace: public-pro
    name: rocketmq-nameserver-cluster
    labels:
      app: rocketmq-nameserver-cluster
  spec:
    serviceName: rocketmq-nameserver-cluster-service
    replicas: 2
    selector:
      matchLabels:
        app: rocketmq-nameserver-cluster
    template:
      metadata:
        labels:
          app: rocketmq-nameserver-cluster
      spec:
        containers:
          - name: rocketmq-nameserver-cluster
            image: registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/nameserver:4.9.1      #使用自己的镜像
            resources:
              requests:
                memory: 1Gi
              limits:
                memory: 1Gi
            ports:
              - containerPort: 9876
                name: http
  
  
  ```

  

- 部署Broker的集群服务

  1. 运行yaml文件，生成broker-a的Master broker的configmap。

     ```yaml
     apiVersion: v1
     kind: ConfigMap
     metadata:
       name: broker-a-properties-configmap
       namespace: public-pro
     data:
       broker-a.properties: |
         brokerClusterName=rocketmq-cluster
         brokerName=broker-a
         brokerId=0
         namesrvAddr=rocketmq-nameserver-cluster-0.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876;rocketmq-nameserver-cluster-1.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876
         defaultTopicQueueNums=4
         autoCreateTopicEnable=true
         autoCreateSubscriptionGroup=true
         listenPort=11911     #rocket的broker监听的端口
         deleteWhen=04
         fileReservedTime=120
         mapedFileSizeCommitLog=1073741824
         mapedFileSizeConsumeQueue=300000
         diskMaxUsedSpaceRatio=88
         storePathRootDir=/data/rocketmq/store
         brokerRole=ASYNC_MASTER     #注意这个参数
         flushDiskType=ASYNC_FLUSH
     
     ```

  2. 运行文件，生成broker-a的Slave broker的configmap。

     ```yaml
     apiVersion: v1
     kind: ConfigMap
     metadata:
       name: broker-a-s-properties-configmap
       namespace: public-pro
     data:
       broker-a-s.properties: |
         brokerClusterName=rocketmq-cluster
         brokerName=broker-a
         brokerId=1
         namesrvAddr=rocketmq-nameserver-cluster-0.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876;rocketmq-nameserver-cluster-1.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876
         defaultTopicQueueNums=4
         autoCreateTopicEnable=true
         autoCreateSubscriptionGroup=true
         listenPort=11911     #rocket的broker监听的端口
         deleteWhen=04
         fileReservedTime=120
         mapedFileSizeCommitLog=1073741824
         mapedFileSizeConsumeQueue=300000
         diskMaxUsedSpaceRatio=88
         storePathRootDir=/data/rocketmq/store
         brokerRole=SLAVE       #注意这个参数
         flushDiskType=ASYNC_FLUSH
     
     ```

  3. 运行文件，生成broker-b的Master broker的configmap。

     ```yaml
     apiVersion: v1
     kind: ConfigMap
     metadata:
       name: broker-b-properties-configmap
       namespace: public-pro
     data:
       broker-b.properties: |
         brokerClusterName=rocketmq-cluster
         brokerName=broker-b
         brokerId=0
         namesrvAddr=rocketmq-nameserver-cluster-0.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876;rocketmq-nameserver-cluster-1.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876
         defaultTopicQueueNums=4
         autoCreateTopicEnable=true
         autoCreateSubscriptionGroup=true
         listenPort=11911      #rocket的broker监听的端口
         deleteWhen=04
         fileReservedTime=120
         mapedFileSizeCommitLog=1073741824
         mapedFileSizeConsumeQueue=300000
         diskMaxUsedSpaceRatio=88
         storePathRootDir=/data/rocketmq/store
         brokerRole=ASYNC_MASTER   #注意这个参数
         flushDiskType=ASYNC_FLUSH
     
     ```

  4. 运行文件，生成broker-b的Slave broker的configmap。

     ```yaml
     apiVersion: v1
     kind: ConfigMap
     metadata:
       name: broker-b-s-properties-configmap
       namespace: public-pro
     data:
       broker-b-s.properties: |
         brokerClusterName=rocketmq-cluster
         brokerName=broker-b
         brokerId=1
         namesrvAddr=rocketmq-nameserver-cluster-0.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876;rocketmq-nameserver-cluster-1.rocketmq-nameserver-cluster-service.public-pro.svc.cluster.local:9876
         defaultTopicQueueNums=4
         autoCreateTopicEnable=true
         autoCreateSubscriptionGroup=true
         listenPort=11911    #rocket的broker监听的端口
         deleteWhen=04
         fileReservedTime=120
         mapedFileSizeCommitLog=1073741824
         mapedFileSizeConsumeQueue=300000
         diskMaxUsedSpaceRatio=88
         storePathRootDir=/data/rocketmq/store
         brokerRole=SLAVE          #注意这个参数
         flushDiskType=ASYNC_FLUSH
     
     ```

  5. 执行文件，生成broker-a的Master的服务

     ```yaml
     apiVersion: v1
     kind: Service
     metadata:
       labels:
         app: broker-a
       name: broker-a
       namespace: public-pro
     spec:
       type: NodePort
       ports:
         - port: 11911
           targetPort: 11911
           name: broker-port
           nodePort: 30911
       selector:
         app: broker-a
     ---
     apiVersion: apps/v1
     kind: StatefulSet
     metadata:
       name: broker-a
       namespace: public-pro
     spec:
       serviceName: broker-a
       replicas: 1
       selector:
         matchLabels:
           app: broker-a
       template:
         metadata:
           labels:
             app: broker-a
         spec:
           containers:
             - name: broker-a
               image: registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/rocketmq:4.9.1
               imagePullPolicy: IfNotPresent
               command: ["sh","-c","/usr/local/rocketmq-4.9.1/bin/mqbroker  -c /usr/local/rocketmq-4.9.1/conf/broker-a.properties"]
               ports:
                 - containerPort: 11911
                   name: http
                   protocol: TCP
                 - containerPort: 11909
                   name: vip
                   protocol: TCP
                 - containerPort: 11912
                   name: slave
                   protocol: TCP
               volumeMounts:
                 - mountPath: /root/logs
                   name: rocketmq-data
                   subPath: mq-brokeroptlogs
                 - mountPath: /data/rocketmq
                   name: rocketmq-data
                   subPath: mq-brokeroptstore
                 - name: broker-config
                   mountPath: /usr/local/rocketmq-4.9.1/conf/broker-a.properties
                   subPath: broker-a.properties
           volumes:
             - name: broker-config
               configMap:
                 name: broker-a-properties-configmap
                 items:
                   - key: broker-a.properties
                     path: broker-a.properties
       volumeClaimTemplates:
         - metadata:
             name: rocketmq-data
           spec:
             storageClassName: nfs-client-storageclass   #存储类名
             accessModes:
               - ReadWriteMany
             resources:
               requests:
                 storage: 10Gi
     
     ```

  6. 执行文件，生成broker-a的Slave的服务

     ```yaml
     apiVersion: v1
     kind: Service
     metadata:
       labels:
         app: broker-a-s
       name: broker-a-s
       namespace: public-pro
     spec:
       type: NodePort
       ports:
         - port: 11911
           targetPort: 11911
           name: broker-port
           nodePort: 30912
       selector:
         app: broker-a-s
     ---
     apiVersion: apps/v1
     kind: StatefulSet
     metadata:
       name: broker-a-s
       namespace: public-pro
     spec:
       serviceName: broker-a-s
       replicas: 1
       selector:
         matchLabels:
           app: broker-a-s
       template:
         metadata:
           labels:
             app: broker-a-s
         spec:
           containers:
             - name: broker-a-s
               image: registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/rocketmq:4.9.1
               imagePullPolicy: IfNotPresent
               command: ["sh","-c","/usr/local/rocketmq-4.9.1/bin/mqbroker  -c /usr/local/rocketmq-4.9.1/conf/broker-a-s.properties"]
               ports:
                 - containerPort: 11911
                   name: http
                   protocol: TCP
                 - containerPort: 11909
                   name: vip
                   protocol: TCP
                 - containerPort: 11912
                   name: slave
                   protocol: TCP
               volumeMounts:
                 - mountPath: /root/logs
                   name: rocketmq-data
                   subPath: mq-brokeroptlogs
                 - mountPath: /data/rocketmq
                   name: rocketmq-data
                   subPath: mq-brokeroptstore
                 - name: broker-config
                   mountPath: /usr/local/rocketmq-4.9.1/conf/broker-a-s.properties
                   subPath: broker-a-s.properties
           volumes:
             - name: broker-config
               configMap:
                 name: broker-a-s-properties-configmap
                 items:
                   - key: broker-a-s.properties
                     path: broker-a-s.properties
       volumeClaimTemplates:
         - metadata:
             name: rocketmq-data
           spec:
             storageClassName: nfs-client-storageclass   #存储类名
             accessModes:
               - ReadWriteMany
             resources:
               requests:
                 storage: 10Gi
     
     ```

  7. broker-b的Master的服务，同broker-a的master一样部署

  8. broker-b的Slave的服务，同broker-a的slave一样部署

- 部署rocketmq的dashboard的服务

  1. 打开已经下载好的rocketmq-console的源码，修改配置文件中`rocketmq.config.namesrvAddr`的值

  2. 制作dashboard的镜像

     ```dockerfile
     FROM adoptopenjdk/openjdk8-openj9:alpine-slim
     
     #验证码字体包
     RUN sed -i "s/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g" /etc/apk/repositories
     RUN set -xe && apk --no-cache add ttf-dejavu fontconfig
     
     RUN mkdir -p /dashboard
     
     WORKDIR /dashboard
     
     EXPOSE 8081
     
     ADD ./rocketmq-console-ng-1.0.0.jar ./app.jar
     
     ENTRYPOINT ["java", "-jar", "app.jar"]
     
     ```

  3. 推送镜像到阿里云仓库

  4. 创建dashboard服务

     ```yaml
     apiVersion: v1
     kind: Service
     metadata:
       labels:
         app: mq-dashboard
       name: mq-dashboard-service
       namespace: public-pro
     spec:
       type: NodePort
       ports:
         - port: 8081
           targetPort: 8081
           name: console-port
           nodePort: 30916
       selector:
         app: mq-dashboard
     ---
     apiVersion: apps/v1
     kind: Deployment
     metadata:
       name: mq-dashboard
       namespace: public-pro
     spec:
       replicas: 1
       selector:
         matchLabels:
           app: mq-dashboard
       template:
         metadata:
           labels:
             app: mq-dashboard
         spec:
           containers:
             - name: mq-dashboard
               image: registry.cn-hangzhou.aliyuncs.com/lanzhen-tw/rocketmq-dashboard:4.9.1
               imagePullPolicy: IfNotPresent
               ports:
                 - containerPort: 8081
                   name: http
                   protocol: TCP
     
     ```

  5. 打开网页输入dashboard的IP:PORT就可以查看集群情况了！