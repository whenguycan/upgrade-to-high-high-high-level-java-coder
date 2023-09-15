## secret概念



#### secret用途

将明文数据加密存储到etcd中，后续供pod调用。



#### 使用场景

比如，在部署mysql的时候，会把root用户名、密码做成一个secret，存储起来，给后续的pod调用。



#### 使用

- 创建一个secret

  ```yaml
  apiVersion: v1
  kind: Secret
  metadata:
    name: mysecret
    namespace: czmall
  type: Opaque          #指明，使用base64编码格式来加密Secret
  data:
    username: YWRtaW4=      #值都是用base64加密之后的内容
    password: MWYyZDFlMmU2N2Rm
  ```

  > 注意：type: Opaque 是在加密sercret中最长使用的，指使用base64对数据进行加密，还有另外两种type类型，分别为： kubernetes.io/service-account-token 这个是在使用service account的时候使用的、kubernetes.io/dockerconfigjson 这个用来存储私有docker registry的认证信息，在yaml文件中可以使用imagePullSecrets来使用。

  

  创建kubernetes.io/dockerconfigjson 这种类型的secret，一般使用命令行

  ```shell
  kubectl create secret docker-registry 秘钥名称 --docker-username=仓库用户名 --docker-password=仓库密码 --docker-server=仓库地址，阿里云为registry.cn-hangzhou.aliyuncs.com --docker-email=邮箱地址 --namespace=xxxx
  ```

  

- pod中以环境变量的方式使用secret

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
    namespace: public-pro
  spec:
    containers:
    - name: nginx
      image: nginx
      env:  #pod启动需要的环境变量
        - name: SECRET_USERNAME #变量的名称
          valueFrom: #设置值来源
            secretKeyRef:
              name: mysecret  #上面的yaml文件中的metadata的name值
              key: username   #上面的yaml文件中data中的key
        - name: SECRET_PASSWORD #变量的名称
          valueFrom: #设置值来源
            secretKeyRef:
              name: mysecret #上面的yaml文件中的metadata的name值
              key: password  #上面的yaml文件中data中的key
  
  ```

  创建完毕，使用kubectl exec -ti podname -n namespace bash  进入pod之后，运行 echo $SECRET_USERNAME查看是否成功

- pod中以Volume数据卷的方式使用secret

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
    namespace: public-pro
  spec:
    containers:
    - name: nginx
      image: nginx
      volumeMounts: #挂载信息
      - name: foo
        mountPath: "/etc/foo" #挂载点是哪
        readOnly: true
    volumes:  #创建一个volume
    - name: foo 
      secret: #提示volume是去找secret的
        secretName: mysecret  #具体的secret的名称，去secret.yaml中的metadata.name值一致
  
  ```

  创建完毕，使用kubectl exec -ti podname -n namespace bash  进入pod之后，找到/etc/foo文件夹中的文件，cat看下内容。

  

- pod中使用kubernetes.io/dockerconfigjson 这种类型的secret

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
    namespace: public-pro
  spec:
    containers:
    - name: nginx
      image: nginx
      volumeMounts: #挂载信息
      - name: foo
        mountPath: "/etc/foo" #挂载点是哪
        readOnly: true
    volumes:  #创建一个volume
    - name: foo 
      secret: #提示volume是去找secret的
        secretName: mysecret  #具体的secret的名称，去secret.yaml中的metadata.name值一致
    imagePullSecrets:
      - name: parking-secret #这儿指定secret
  
  ```

  
