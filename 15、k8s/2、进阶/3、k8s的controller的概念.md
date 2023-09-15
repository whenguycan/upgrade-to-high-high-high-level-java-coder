## pod的管理器controller



#### 什么是controller

创建和管理pod的工具



#### 分类

- Deployment
- Statefulset
- DaemonSet
- Job/CronJob



#### 描述及用法

- Deployment

  - 概念：

    无状态应用部署选用的controller

    > 什么是无状态应用？就是一个应用会启动多个pod，但是pod与pod之间不存在主次、主从等其它关系，大家都是平辈的，就叫无状态应用。

  - 可用于部署的yaml文件：

    ```yaml
    kind: Deployment
    apiVersion: apps/v1
    metadata:
      name: nginx-bjy8ec
      namespace: czmall
      labels:
        app: nginx
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: nginx
      template:
        metadata:
          labels:
            app: nginx
    ..........
    ..........
    ```

  - 常用命令：

    1. kubectl get deployments -n xxxx
    2. kubectl delete deployment xxxx -n yyyy         //deployment下的pod也会被清理掉
    3. kubectl edit deployment xxxx -n yyyy     //编辑一个deployment的资源描述文件，会自动保存到一个yaml文件中。
    4. kubectl describe deployment xxxx -n yyyy //可以查看一个deployment的信息

  

- StatefulSet

  - 概念：

    有状态应用部署选用的controller，<font color="red">会生成以 xxx-1/2/3 这样有序数字的名称的pod</font>

    > 什么叫有状态应用？就是一个应用会启动多个pod，但是pod与pod之间存在主次、主从等其它关系，大家是有备份差异的，就叫有状态应用。

  - 可用于部署的yaml文件：

    ```yaml
    apiVersion: apps/v1
    kind: StatefulSet
    metadata:
      name: nginx-statefulset
      namespace: default
    spec:
      serviceName: nginx
      replicas: 3
      selector:
        matchLabels:
          app: nginx
      template:
        metadata:
          labels:
            app: nginx
    ..........
    ..........
    ```

  - 常用命令：

    1. kubectl get statefulsets -n xxxx
    2. kubectl delete statefulsets xxxx -n yyyy      //statefulset下的pod也会被清理掉    
    3. kubectl edit statefulsets xxxx -n yyyy    //一经编辑，直接生效，不像deployment那样生成.yaml的资源配置文件
    4. kubectl describe statefulsets xxxx -n yyyy   //可以查看一个statefulset的信息

  

- DaemonSet

  - 概念：

    当每个k8s的node节点需要运行一个固定的pod，就可以选用这个controller，后续加入到集群中的node节点，只要成功加入集群就会自动启动该pod。

    > 在实际中很少使用，kube-proxy就是DaemonSet方式部署的。我们一般用这种方法启动一个pod，收集node节点的服务器信息。

  - 可用于部署的yaml文件：

    ```yaml
    apiVersion: apps/v1
    kind: DaemonSet
    metadata:
      name: ds-test
      labels:
        app: filebeat
    spec:
      selector:
        matchLabels:
          app: filebeat
      template:
        metadata:
          labels:
            app: filebeat
        spec:
          containers:
          - name: logs
            image: nginx
            ports:
            - containerPort: 80
            volumeMounts:
            - name: varlog
              mountPath: /tmp/log
          volumes:
          - name: varlog
            hostPath:
              path: /var/log
    ```

  - 常用命令

    1. kubectl get daemonsets -n xxxx
    2. kubectl edit daemonsets xxxx -n yyyy     //编辑一个daemonset的资源描述文件，会自动保存到一个yaml文件中
    3. kubectl delete daemonsets xxxx -n yyyy //删除一个daemonset
    4. kubectl describe daemonsets xxxx -n yyyy

  

- Job/CronJob

  - 概念：

    创建一次性任务或定时任务的的应用，需要选择这个controller

    > 使用Job创建的应用，当任务结束后，pod的状态会变成Completed。
    >
    > 使用cronjob创建的应用，当一次任务结束后，pod的状态会变成Completed，下一次开始任务会新启一个pod运行任务。

  - 可用于部署的yaml文件：

    部署job

    ```yaml
    apiVersion: batch/v1
    kind: Job
    metadata:
      name: pi
    spec:
      template:
        spec:
          containers:
          - name: pi
            image: perl
            command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
          restartPolicy: Never
      backoffLimit: 4
    ```

    部署cronjob

    ```yaml
    apiVersion: batch/v1beta1
    kind: CronJob #注意这儿一定要携程CronJob
    metadata:
      name: hello
    spec:
      schedule: "*/1 * * * *" #每分钟执行一次
      jobTemplate:
        spec:
          template:
            spec:
              containers:
              - name: hello
                image: busybox #镜像名称
                args:
                - /bin/sh #运行的命令
                - -c
                - date; echo Hello from the Kubernetes cluster
              restartPolicy: OnFailure #重启策略
    ```

  - 常用命令

    1. kubectl get jobs/cronjobs -n xxxx
    2. kubectl delete jobs/cronjobs xxxx -n yyyy
    3. kubectl edit jobs/cronjobs xxxx -n yyyy //编辑一个job/cronjob的资源描述文件，会自动保存到一个yaml文件中
    4. kubectl describe jobs/cronjobs  xxxx -n yyyy

