## StorageClass概念

> k8s 1.20版本以上一直按照文档安装
>
> k8s 1.20以上的版本，需要修改apiserver的文件打开selfLink功能，才正常部署，具体百度！
>
> k8s 1.25的版本 这样创建没有用，因为selfLink功能被k8s移除了！



#### 概念

storageclass是k8s提供的一种自动创建pv的机制



#### 为什么要使用storageclass

因为在实际的项目中，pod的新增和删除是很频繁的，如果派一个工程师一直在手动创建pv/pvc，那么会非常麻烦，所以就有了storageclass，用storageclass可以为pod自动处理好pv/pvc，无需我们关注。



#### 创建storageclass

- 我们需要一个nfs-client的自动装载程序，我们称之为Provisioner，这个程序会使用我们已经配置好的NFS服务器自动创建持久卷，也就是自动帮我们创建PV。

  > 注意：
  >
  > ​		自动创建的PV会以${namespace}-${pvcName}-${pvName}的目录格式放到NFS服务器上
  >
  > ​		在下面的reclaimPolicy为Delete的时候，PV被回收(手动删除pod以及pvc)，则会以archieved-${namespace}-${pvcName}-${pvName}这样的格式存放到  NFS服务器上

- 运行如下yaml文件为Provisioner (nfs-client)进行rbac的授权

  ```yaml
  kind: ServiceAccount
  apiVersion: v1
  metadata:
    name: nfs-client-provisioner
    namespace: xxxxx   #默认使用default就行了
  ---
  kind: ClusterRole
  apiVersion: rbac.authorization.k8s.io/v1
  metadata:
    name: nfs-client-provisioner-runner
  rules:
    - apiGroups: [""]
      resources: ["persistentvolumes"]
      verbs: ["get", "list", "watch", "create", "delete"]
    - apiGroups: [""]
      resources: ["persistentvolumeclaims"]
      verbs: ["get", "list", "watch", "update"]
    - apiGroups: ["storage.k8s.io"]
      resources: ["storageclasses"]
      verbs: ["get", "list", "watch"]
    - apiGroups: [""]
      resources: ["events"]
      verbs: ["watch", "create", "update", "patch"]
    - apiGroups: [""]
      resources: ["services", "endpoints"]
      verbs: ["get","create","list", "watch","update"]
    - apiGroups: ["extensions"]
      resources: ["podsecuritypolicies"]
      resourceNames: ["nfs-client-provisioner"]
      verbs: ["use"]
  ---
  kind: ClusterRoleBinding
  apiVersion: rbac.authorization.k8s.io/v1
  metadata:
    name: run-nfs-client-provisioner
  subjects:
    - kind: ServiceAccount
      name: nfs-client-provisioner
      namespace: xxxx    #这儿的namespace必不可少，就用默认的dafault就行
  roleRef:
    kind: ClusterRole
    name: nfs-client-provisioner-runner
    apiGroup: rbac.authorization.k8s.io
  ---
  kind: Role
  apiVersion: rbac.authorization.k8s.io/v1
  metadata:
    name: leader-locking-nfs-client-provisioner
    namespace: xxxx     #默认使用default就行了
  rules:
    - apiGroups: [""]
      resources: ["endpoints"]
      verbs: ["get", "list", "watch", "create", "update", "patch"]
  ---
  kind: RoleBinding
  apiVersion: rbac.authorization.k8s.io/v1
  metadata:
    name: leader-locking-nfs-client-provisioner
    namespace: xxxx    #默认使用default就行
  subjects:
    - kind: ServiceAccount
      name: nfs-client-provisioner
      # replace with namespace where provisioner is deployed
      namespace: xxxx       #这儿的namespace必不可少，默认使用default就行
  roleRef:
    kind: Role
    name: leader-locking-nfs-client-provisioner
    apiGroup: rbac.authorization.k8s.io
  
  ```
  
- 运行如下yaml文件为Provisioner (nfs-client)创建好自动装载程序

  ```yaml
  kind: Deployment
  apiVersion: apps/v1
  metadata:
    name: nfs-client-provisioner
    namespace: public-pro     #使用default就行
  spec:
    replicas: 1
    selector:
      matchLabels:
        app: nfs-client-provisioner
    strategy:
      type: Recreate
    template:
      metadata:
        labels:
          app: nfs-client-provisioner
      spec:
        serviceAccountName: nfs-client-provisioner
  #      securityContext:
  #        privileged: true
        containers:
          - name: nfs-client-provisioner
            image: registry.cn-hangzhou.aliyuncs.com/rookieops/nfs-client-provisioner:v0.1
            securityContext:
              privileged: true
            volumeMounts:
              - name: nfs-client-root
                mountPath: /persistentvolumes  #这个镜像中volume的mountPath默认为/persistentvolumes，不能修改，否则运行时会报错
            env:
              - name: PROVISIONER_NAME
                value: fuseim.pri/ifs
              - name: NFS_SERVER
                value: 192.168.110.16 #改成你NFS的地址，如果在云上有2块网卡，记得用内网地址
              - name: NFS_PATH
                value: /data/share  #改成你自己的NFS都是共享路径
        volumes:
          - name: nfs-client-root
            nfs:
              server: 192.168.110.16 #改成你NFS的地址，如果在云上有2块网卡，记得用内网地址
              path: /data/share   #改成你自己的NFS都是共享路径
  
  ```

  创建完毕之后，通过kubectl get pods -n xxxx 查看对应名称的pod是否启动成功。

- 创建storageClass

  ```yaml
  apiVersion: storage.k8s.io/v1
  kind: StorageClass
  metadata:
    name: nfs-client-storageclass
  provisioner: fuseim.pri/ifs #必须和上面得Deployment的YAML文件中PROVISIONER_NAME的值保持一致
  reclaimPolicy: Delete
      #"reclaim policy"有三种方式：Retain、Recycle、Deleted。
      # Retain:  
      		#删除PVC时，不会同时删除PV，而是将PV的状态改成Released，该PV不能被其它PVC绑定。管理员需要手动骤释NFS服务器上的放存储资源
      # Delete:
          #删除PVC时，会默认同时删除PV，PV对应的文件数据会以archieved-${namespace}-${pvcName}-${pvName}这样的格式存放到  NFS服务器上
      # Recycle: 已废弃
  
  ```

  storageclass创建完毕，通过kubectl get sc 查看下。

- 创建好storageclass之后，，将其设置为默认的存储驱动

  ```shell
  kubectl patch storageclass storageclass的名称 -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
  ```

  

- 创建一个pvc供pod调用测试

  ```yaml
  apiVersion: v1
  kind: PersistentVolumeClaim
  metadata:
    name: test-nfs-pvc2
    annotations:
      volume.beta.kubernetes.io/storage-class: "nfs-client-storageclass"
  spec:
    storageClassName: nfs-client-storageclass   #指定使用上面的storageclass
    accessModes:
      - ReadWriteMany
    resources:
      requests:
        storage: 1Mi
  ```

- 创建一个pod测试

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: test-storageclass-pod
  spec:
    containers:
    - name: busybox
      image: busybox:latest
      imagePullPolicy: IfNotPresent
      command:
      - "/bin/sh"
      - "-c"
      args:
      - "sleep 3600"
      volumeMounts:   #挂载volume使用
      - name: nfs-pvc
        mountPath: /mnt
    restartPolicy: Never
    volumes:   #创建一个volume
    - name: nfs-pvc
      persistentVolumeClaim:
        claimName: test-nfs-pvc2  #指定volume使用pvc的名称
  ```





​	<font color="red">大多数我们有了storageclass都是 使用volumeClaimTemplates: #定义一个存储卷申请模板   的方式去使用storageclass</font>

- 测试storageclass是否可以正常使用

  ```yaml
  ---
  apiVersion: v1
  kind: Service
  metadata:
    labels:
      app: public-pro-nginx-storage
    name: public-pro-nginx-storage
    namespace: public-pro
  spec:
    clusterIP: None  
    ports:
    - name: nginx-port
      port: 8080    #当前service的端口
      protocol: TCP
      targetPort: 80  #pod的端扣 = container的端口 = dockerfile中expose的端口 
    selector:
      app: nginx
    sessionAffinity: None
    type: ClusterIP   #指定类型为ClusterIP
  ---
  apiVersion: apps/v1
  kind: StatefulSet
  metadata:
    name: nginx-dep1
    namespace: public-pro  
  spec:
    serviceName: public-pro-nginx-storage
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
          - name: nacos-dir 
            mountPath: /usr/share/nginx/html
          ports:
          - containerPort: 80
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

  

