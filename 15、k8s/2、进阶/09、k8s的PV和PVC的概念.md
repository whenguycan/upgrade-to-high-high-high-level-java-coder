## PV/PVC概念



#### 概念

1. PV：对持久化存储资源进程抽象的工具，比如 将nfs的网络共享存储进行抽象，达到为k8s内pod可使用的目的。

2. PVC：pod想要使用PV的一个中间介质。

   

#### 实现流程图示

![avatar](../images/33.jpg)

#### 使用

以NFS的网络共享存储做为示例

> 前提条件 各个服务器节点都需要安装好nfs（如何安装nfs，自行baidu解决），nfs server端需要先共享出资源。

- 创建pv

  > pv没有namespace的概念

  ```yaml
  apiVersion: v1
  kind: PersistentVolume #声明为pv
  metadata:
    name: my-pv #pv的名称
    labels: #pvc绑定到对应pv通过labels标签方式实现
    
      pv: my-pv
  spec:
    capacity:
      storage: 5Gi
    accessModes:
      - ReadWriteMany #允许多节点读写
    storageClassName: nfs #至关重要，需要与下面的pvc.yaml文件中的storageClassName名称一致
    nfs:
      path: /data/share #NFS服务端共享的路径(千万不要放共享的根路径，且需要给子目录可写的权限) 写入/etc/exports中的
      server: 192.168.110.16 #NFS服务端的IP
  ```

  创建pv完毕之后，可以通过kubectl get pv来查看pv的状态，STATUS 为 Available，表示 pv就绪，可以被 PVC 申请

- 创建pvc

  > pvc有namespace的概念

  ```yaml
  apiVersion: v1
  kind: PersistentVolumeClaim #声明为PVC
  metadata:
    name: my-pvc #pvc名称
    namespace: czmall #pvc属于哪个命名空间
  spec:
    storageClassName: nfs #至关重要，需要跟上面的pv.yaml文件中的storageClassName一致
    accessModes:
      - ReadWriteMany #允许多节点读写
    resources:
      requests:
        storage: 5Gi
    selector:
      matchLabels: #pvc绑定到对应pv通过labels标签方式实现
        pv: my-pv
  ```

  创建pv完毕之后，可以查看pvc的状态 kubectl get pvc -n xxxx

- 创建pod使用上面的pvc

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: nginx-dep1
    namespace: czmall   #需要跟上面的pvc创建在同一个namespace下
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: nginx
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
        - name: nginx
          image: nginx
          volumeMounts:
          - name: wwwroot
            mountPath: /usr/share/nginx/html
          ports:
          - containerPort: 80
        volumes:
        - name: wwwroot
          persistentVolumeClaim:
            claimName: my-pvc
  
  ```

  当pod创建完毕，可以kubectl exec -ti podname -n namespace bash 进入到pod内，然后切换到/usr/share/nginx/html中随便创建一个文件，然后去挂载的目录看下有没有公共存储的文件。