## 搭建Logstash集群

> 此方式适用 使用自己方法搭建es集群 ,且logstash的版本必须与es的版本一致



#### 搭建过程

- 运行如下yaml文件，创建一个configmap，配置好logstash接收数据的端口、日志如何处理、发送到哪个es

```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: logstash-cluster-conf
  namespace: public-pro
  labels:
    app: logstash-cluster-conf
  annotations:
    kubesphere.io/alias-name: logstash的配置文件
    kubesphere.io/description: logstash-conf配置文件
data:
  logstash-conf: |-
    input {
      tcp {
        port => 5044   #如果是有多个项目 最好一个项目对应一个端口
        type => "erp"
      }
    }

    filter {
      grok {
        match=>{
          "message"=>"(?<application-name>[a-zA-Z0-9.-_]+)\s+(?<level>\[[a-z-A-Z0-9.-_]+\])\s+"
        }
      }
    }

    output {
    	if [type] == "erp"{
      	elasticsearch {
          hosts => ["192.168.2.235:31468"]  #这儿填写es-http的9200的端口地址
          index => "%{[application-name 这儿改成你需要的项目名称]}-%{[@version]}-%{+YYYY.MM.dd}"
          ssl => false  #是否打开https
          user => "elasticsearch的用户名"
          password => "elasticsearch的密码"
          ssl_certificate_verification => false
          cacert => "证书的位置ca.crt"
        }
      }
    }

```

- 运行如下yaml文件，创建一个configmap，设置好worker的进程数、logstash配置文件的路径

```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: logstash-cluster-pipline-conf
  namespace: public-pro
  labels:
    app: logstash-cluster-pipline-conf
  annotations:
    kubesphere.io/alias-name: logstash-pipline的配置文件
    kubesphere.io/description: logstash-pipline-conf配置文件
data:
  logstash-pipline-yaml: |-
    - pipeline.id: lanzhen-logstash
      pipeline.workers: 5
      path.config: "/usr/share/logstash/config/logstash.conf"

```

- 运行如下yaml文件，创建一个configmap，设置好logstash的绑定IP

```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: logstash-cluster-logstash-conf
  namespace: public-pro
  labels:
    app: logstash-cluster-logstash-conf
  annotations:
    kubesphere.io/alias-name: logstash-logstash的配置文件
    kubesphere.io/description: logstash-logstash-conf配置文件
data:
  logstash-log-conf: |-
    http.host: "0.0.0.0"

```

- 运行如下文件，创建一个deployment，需要挂载上面3个configmap使用

```yaml
apiVersion: v1
kind: Service
metadata:
  name: logstash-headless
  namespace: public-pro
  labels: #给logstash的service打上标签
    app: logstash
spec: #service的规格信息
  ports:
    - name: logstash-port
      port: 9600     #service的端口
      targetPort: 9600 #pod也就是容器的端口
    - name: log-pod-transport
      port: 5044
      targetPort: 5044
  clusterIP: None #无头service固定格式
  selector: #通过下面定义的标签去匹配具体的pod
    app: logstash
    appCluster: logstash-cluster
---
apiVersion: apps/v1
kind: StatefulSet  #有状态的应用部署
metadata:
  name: logstash
  namespace: public-pro
spec:
  serviceName: logstash-headless   #该有状态应用归属于哪个service
  replicas: 3 #pod的数量
  template: #下面是pod的信息
    metadata:
      labels: #给pod加上标签
        app: logstash
        appCluster: logstash-cluster
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
                        - logstash
      containers: #容器的属性
      - name: logstash
        image: logstash:7.12.1  #如果是ECK部署的es，用自己制作的镜像吧
        resources:   #资源
          limits:
            cpu: 1
            memory: "800Mi"
          requests:    #请求的资源
            cpu: 100m   #m代表千分之,相当于0.1 个cpu资源
            memory: "300Mi"   #内存300m大小
        ports: #容器开放端口
          - name: logstash-port
            containerPort: 9600
            protocol: "TCP"
          - name: log-transport
            containerPort: 5044
            protocol: "TCP"
        volumeMounts: #挂载创建好的卷
          - name: logstash-conf-map #挂载configmap生成的文件
            mountPath: /usr/share/logstash/config/logstash.conf #挂载到哪个路径下，如果是具体的文件，就需要具体到文件名，与下面的subPath一致
            subPath: logstash.conf #如果挂载的mountPath不是个目录，而是一个文件，就需要用这个，指定挂载的文件名
          - name: logstash-cluster-pipline-conf #挂载configmap生成的文件
            mountPath: /usr/share/logstash/config/pipelines.yml #挂载到哪个路径下，如果是具体的文件，就需要具体到文件名，与下面的subPath一致
            subPath: pipelines.yml #如果挂载的mountPath不是个目录，而是一个文件，就需要用这个，指定挂载的文件名
          - name: logstash-cluster-logstash-conf #挂载configmap生成的文件
            mountPath: /usr/share/logstash/config/logstash.yml #挂载到哪个路径下，如果是具体的文件，就需要具体到文件名，与下面的subPath一致
            subPath: logstash.yml #如果挂载的mountPath不是个目录，而是一个文件，就需要用这个，指定挂载的文件名
      volumes: #创建卷
        - name: logstash-conf-map #创建卷的名称
          configMap: #创建卷使用configMap
            name: logstash-cluster-conf  #configMap的名称
            items:
              - key: logstash-conf #创建configMap里设置的名称
                path: logstash.conf #把configMap里面设置的值，隐射到logstash.conf文件中
        - name: logstash-cluster-pipline-conf #创建卷的名称
          configMap: #创建卷使用configMap
            name: logstash-cluster-pipline-conf  #configMap的名称
            items:
              - key: logstash-pipline-yaml #创建configMap里设置的名称
                path: pipelines.yml #把configMap里面设置的值，隐射到logstash.conf文件中
        - name: logstash-cluster-logstash-conf #创建卷的名称
          configMap: #创建卷使用configMap
            name: logstash-cluster-logstash-conf  #configMap的名称
            items:
              - key: logstash-log-conf #创建configMap里设置的名称
                path: logstash.yml #把configMap里面设置的值，隐射到logstash.conf文件中
  selector:
    matchLabels: #应用会使用下面两个label来挑选具体的pod，下面两个app是或者的关系
      app: logstash
      appCluster: logstash-cluster

```

