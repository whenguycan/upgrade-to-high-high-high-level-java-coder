## 使用ES官方ECK的方式搭建集群



#### 官方参考

https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-quickstart.html

> 注意查看 官方参考的**Supported versions**，找到适合你的es的版本支持k8s版本



#### 准备工作

1. k8s内需要有可用的storageclass



#### 搭建流程

- 部署ECK到你K8S集群，<font color="red">需要先确认需要安装的es版本，这儿以安装1.6.0为例</font>

  ```shell
  kubectl apply -f https://download.elastic.co/downloads/eck/1.6.0/all-in-one.yaml
  ```

- 查看ECK安装进度，可能需要等很久，因为镜像在国外。

  ```shell
  kubectl -n elastic-system logs -f statefulset.apps/elastic-operator
  ```

  kubectl get pods -n elastic-system 查看elastic-operator-0是否为running状态

- 部署es的集群服务，<font color="red">部署完成，es的https是无法关闭的</font>

  - 运行如下的yaml文件，部署es集群

    > 部分参数 可能版本变更之后的设置是不相同的，具体可以参考 https://www.elastic.co/guide/en/cloud-on-k8s/1.6/index.html 的Orchestrating Elastic Stack applications菜单下的 Run Elasticsearch on ECK菜单中所有的子菜单，查看配置项。

  ```yaml
  apiVersion: elasticsearch.k8s.elastic.co/v1
  kind: Elasticsearch
  metadata:
    name: quickstart
    namespace: lanzhen-membrance
  spec:
    version: 8.1.2      #指定es的版本
    nodeSets:
    - name: master-nodes
      count: 1
      config:
        node.roles: ["master"]   #表示当前节点只能为master节点
        xpack.ml.enabled: true
      podTemplate:
        spec:
          initContainers:
          - name: sysctl
            securityContext:
              privileged: true
            command: ['sh', '-c', 'sysctl -w vm.max_map_count=262144']
          containers:
          - name: elasticsearch
            env:
            - name: ES_JAVA_OPTS
              value: -Xms512m -Xmx512m
            resources:
              requests:
                memory: 1Gi
              limits:
                memory: 1Gi
      volumeClaimTemplates:
      - metadata:
          name: elasticsearch-data
        spec:
          accessModes:
          - ReadWriteOnce
          resources:
            requests:
              storage: 5Gi
          storageClassName: nfs-client-storageclass  #指定k8s内有的storageClass
    - name: data-nodes
      count: 2
      config:
        node.roles: ["data", "ingest"]   #表示当前节点只能是date节点和ingest节点
      podTemplate:
        spec:
          initContainers:
          - name: sysctl
            securityContext:
              privileged: true
            command: ['sh', '-c', 'sysctl -w vm.max_map_count=262144']
          containers:
          - name: elasticsearch
            env:
            - name: ES_JAVA_OPTS
              value: -Xms512m -Xmx512m
            resources:
              requests:
                memory: 1Gi
              limits:
                memory: 1Gi
      volumeClaimTemplates:
      - metadata:
          name: elasticsearch-data
        spec:
          accessModes:
          - ReadWriteOnce
          resources:
            requests:
              storage: 10Gi
          storageClassName: nfs-client-storageclass
  
  
  ```

  - 运行完毕，查看es的pod是否启动完毕，安装过程比较漫长，因为镜像在国外。

  ```shell
  kubectl get all -n yaml文件中的namespace
  ```

  - 运行完毕，会启动4个服务，分为如下：
    1. quickstart-es-data-nodes 从节点的服务
    2. quickstart-es-master-nodes 主节点的服务
    3. quickstart-es-transport
    4. quickstart-es-http 这个服务是有内部虚拟IP的，通过虚拟IP，可以拿到es集群的密码，暴露这个服务的端口，可以提供本地访问，访问形式为：https://ip+port，用户名为elastic，密码可以通过如下方式获取。

  ```shell
  PASSWORD=$(kubectl get secret -n lanzhen-membrance quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')  
  ```

  - 查看es的启动状态，看到结果就证明部署成功

  ```shell
  curl -u "elastic:$PASSWORD" -k "https://quickstart-es-http的虚拟IP地址:9200"
  ```

  - 修改node节点的个数

    只要修改部署的yaml中data-nodes的count数量，再次运行es.yaml文件就行了

  

- 部署Kibana <font color="red" size="20">此处未验证</font>

  - 运行如下命令创建一个kibana用来跟es通信的名为 kibana-elasticsearch-credentials 的secret

  ```shell
  kubectl create secret generic kibana-elasticsearch-credentials --from-literal=elasticsearch.password=$PASSWORD -n lanzhen-membrance
  ```

  - 运行如下yaml文件，<font color="red">参考的网址内都有</font>

  ```yaml
  apiVersion: kibana.k8s.elastic.co/v1
  kind: Kibana
  metadata:
    name: kibana-sample
    namespace: lanzhen-membrance
  spec:
    version: 8.1.2
    count: 1
    config:
      elasticsearch.hosts:    
        - https://quickstart-es-http.lanzhen-membrance:9200   #指定es内部service的DNS和端口
      elasticsearch.username: elastic
    secureSettings:
      - secretName: kibana-elasticsearch-credentials   #使用上面的secret
  ```

  - 通过xxxx-kb-http的service，给kibana对外暴露一个端口

  

  

  

