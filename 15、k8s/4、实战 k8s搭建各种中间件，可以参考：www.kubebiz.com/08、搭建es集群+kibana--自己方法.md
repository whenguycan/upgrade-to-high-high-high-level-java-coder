## 使用自己的方式搭建ES集群

> 注意，此方法只适用于es 7.x + kibana 7.x的版本。



#### ES集群的概念

- 只要有相同的cluster.name就会被分在一个集群中
- 主节点：master节点主要用于集群的管理、索引的处理，比如：新增节点、分片分配、索引的新增删除
- 数据节点：data节点保存数据分片，负责索引和搜索操作
- 客户端节点：client节点仅作为请求客户端存在，不存数据，只是将请求负载均衡的转发到其它节点。



#### 准备工作

1. k8s内需要有可用的storageclass



#### 具体配置

```yaml
cluster.name: 集群名称，只要是相同的cluster.name就会被分在一个集群中
node.name: xxxxx 当前节点的名称
network.host: 0.0.0.0
http.port: 启动端口
transport.tcp.port: 内部通讯使用的端口，查找其他集群节点就用的这个
node.master: true/false    是否允许成为主节点
node.data: true/false        是否允许作为数据节点存储数据
node.ingest: true/fase      是否允许成为client节点
discovery.zen.ping.unicast.hosts: ["0.0.0.0:9300", "0.0.0.0:9301"]    集群中寻找其他集群的地址
discovery.zen.mininum_master_nodes: 1    #集群中最少的master节点数量
bootstrap.memory_lock: false
node.max_local_storage_nodes: 2

path.data:    数据存储位置
path.logs:    日志存储位置

http.cors.enabled: true
http.cors.all-origin: /.*/        
```



#### 搭建流程

运行如下yaml文件，并暴露9200端口即可

> 创建了3个es节点，3个节点都可以成为master、data、client节点，目前仅可以使用在7.12.1的镜像版本上。

```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: es-cluster
  namespace: public-pro
  labels:
    app: es-cluster
  annotations:
    service.alpha.kubernetes.io/tolerate-unready-endpoints: "true"
spec:
  ports:
    - name: server-port
      port: 9200
      targetPort: 9200
    - name: server-cluster-port
      port: 9300
      targetPort: 9300
  clusterIP: None
  type: ClusterIP
  selector:
    app: es-cluster
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  namespace: public-pro
  name: es-cluster
  labels:
    app: es-cluster
spec:
  serviceName: es-cluster
  replicas: 3
  selector:
    matchLabels:
      app: es-cluster
  template:
    metadata:
      labels:
        app: es-cluster
    spec:
      containers:
        - name: es-cluster
          image: docker.elastic.co/elasticsearch/elasticsearch:7.12.1
          resources:
            requests:
              memory: 1Gi
            limits:
              memory: 1Gi
          command: ["bash", "-c", "ulimit -l unlimited && sysctl -w vm.max_map_count=262144 && chown -R elasticsearch:elasticsearch /usr/share/elasticsearch/data && exec su elasticsearch docker-entrypoint.sh"]
          ports:
            - containerPort: 9200
              name: http
            - containerPort: 9300
              name: transport
          env:
            - name: discovery.seed_hosts    #es会通过这个配置去查找所有的es集群的节点，这儿需要修改namespace的信息
              value: "es-cluster.public-pro.svc.cluster.local"
            - name: discovery.zen.minimum_master_nodes
              value: "1"
            - name: cluster.initial_master_nodes #初始化master节点的所有node节点的名称
              value: "es-cluster-0,es-cluster-1,es-cluster-2"
            - name: ES_JAVA_OPTS
              value: -Xms512m -Xmx512m
            - name: node.master
              value: "true"
            - name: node.ingest
              value: "true"
            - name: node.data
              value: "true"
            - name: cluster.name
              value: "elasticsearch-cluster-v7"
            - name: node.name    #配置当前es的node.name   具体的值 是读取的设置的pod的 metadata.name 的值，需要与上面的 “cluster.initial_master_nodes” 配置的节点一致！！否则无法建立集群。
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: network.host
              value: "0.0.0.0"
          volumeMounts:
            - mountPath: /usr/share/elasticsearch/data
              name: es-data
          securityContext:
            privileged: true
  volumeClaimTemplates:
    - metadata:
        name: es-data
      spec:
        storageClassName: nfs-client-storageclass
        accessModes: [ "ReadWriteMany" ]
        resources:
          requests:
            storage: 10Gi

```





#### kibana安装

- 运行如下的yaml

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  namespace: lanzhen-membrance
  name: kibana-config
  labels:
    app: kibana
data:
  kibana.yml: |-
    server.name: kibana
    server.host: "0"
    i18n.locale: zh-CN                      #设置默认语言为中文
    elasticsearch:
      hosts: http://es-cluster.lanzhen-membrance:9200        #es集群连接地址，由于我这都都是k8s部署且在一个ns下，可以直接使用service name连接
---
apiVersion: v1
kind: Service
metadata:
  name: kibana
  namespace: lanzhen-membrance
  labels:
    app: kibana
spec:
  type: NodePort
  ports:
  - port: 5601
    nodePort: 30432
  selector:
    app: kibana
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kibana
  namespace: lanzhen-membrance
  labels:
    app: kibana
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kibana
  template:
    metadata:
      labels:
        app: kibana
    spec:
      containers:
      - name: kibana
        image: docker.elastic.co/kibana/kibana:7.12.1         #kibana版本需要与es版本一致
        imagePullPolicy: IfNotPresent
        resources:
          limits:
            cpu: 1000m
          requests:
            cpu: 1000m
        env:
          - name: ELASTICSEARCH_URL
            value: http://es-cluster.lanzhen-membrance:9200                 #设置为handless service dns地址即可
        ports:
        - containerPort: 5601
        volumeMounts:
        - name: config
          mountPath: /usr/share/kibana/config/kibana.yml     #kibana配置文件挂载地址
          readOnly: true
          subPath: kibana.yml
      volumes:
      - name: config
        configMap:
          name: kibana-config                                #对应configmap名称
```

- 网页打开kibana，左侧找到dev-tools(开发工具)，输入 "GET _cat/health?v” 就能看到集群信息。