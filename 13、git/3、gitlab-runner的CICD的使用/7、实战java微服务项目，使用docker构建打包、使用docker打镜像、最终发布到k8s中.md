## java微服务实战

使用docker构建打包、使用docker打镜像、最终运行在k8s中



#### 1、打开projects目录中java目录中的park_server项目

逐行分析.gitlab-ci.yml文件



#### 2、逐行分析.gitlab-ci.yml

```yaml
variables:
  BUILD_SHELL: 'mvn clean package -Dmaven.test.skip=true -gs ./mvn-setting.xml -Dmaven.repo.local=/home/cache/maven'
  CACHE_DIR: 'artifact/'
  DOCKER_FILE_PATH: "./Dockerfile"
  PROJECT_NAME: 'order'
  VERSION: '${CI_COMMIT_SHA}'
  IMAGE_NAME:  "$PROJECT_NAME:$VERSION"
  DOCKER_REGISTRY: 'reg.czsdc.com:5000/cicd/'
  PUSH_IMG_TAG: '$DOCKER_REGISTRY$IMAGE_NAME'


stages: #定义3个阶段
  - build
  - buildimage
  - deploy

deploy:
  only:
    - master
  variables:
    GIT_CHECKOUT: "false"
  image: lucj/kubectl:1.20.4
  cache:
    paths:
      - ${CACHE_DIR}
  stage: deploy
  tags:
    - deploy
  script:
    - sh ${CACHE_DIR}deploy.sh $VERSION
    - kubectl --kubeconfig=./artifact/dev-config get pods -A



build_docker_image:
  only:
    - master
  variables:
    GIT_CHECKOUT: "false"
  before_script:
    - docker info
  image: docker:19.03.12
  cache:
    paths:
      - ${CACHE_DIR}
  stage: buildimage
  tags:
    - buildimage
  script:
    - ls
    - sh ${CACHE_DIR}buildimage.sh $DOCKER_REGISTRY_USER $DOCKER_REGISRTY_PASSWD $VERSION

#会自动拉取git仓库中的代码
build:
  only: #只工作在master分支上
    - master
  image: maven:3.6.3-openjdk-17 #运行当前任务，docker的镜像使用哪个
  variables: #设置docker被的代理，因为有些服务器特殊，这儿不考虑这个
    HTTP_PROXY: http://10.10.210.19:8888
    HTTPS_PROXY: http://10.10.210.19:8888
  cache:
    paths:
      - ${CACHE_DIR}   #当前job工作完毕，将artifact/目录缓存到gitlab-runner目录中去
  stage: build #跟stages中的build一致
  tags:
    - build #跟gitlab-runner中的tag一致
  script: #需要运行的脚本
    - curl https://maven.aliyun.com #目的是查看网络通不通
    - mvn clean install -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven #在项目根目录执行打包命令
    - bash build.sh #运行build.sh的shell脚本

```

- build阶段

  1. build阶段的代码

     ```yaml
     #会自动拉取git仓库中的代码
     build:
       only: #只工作在master分支上
         - master
       image: maven:3.6.3-openjdk-17 #运行当前任务，docker的镜像使用哪个
       variables: #设置docker被的代理，因为有些服务器特殊，这儿不考虑这个
         HTTP_PROXY: http://10.10.210.19:8888
         HTTPS_PROXY: http://10.10.210.19:8888
       cache:
         paths:
           - ${CACHE_DIR}   #当前job工作完毕，将artifact/目录缓存到gitlab-runner目录中去
       stage: build #跟stages中的build一致
       tags:
         - build #跟gitlab-runner中的tag一致
       script: #需要运行的脚本
         - curl https://maven.aliyun.com #目的是查看网络通不通
         - mvn clean install -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven #在项目根目录执行打包命令
         - bash build.sh #运行build.sh的shell脚本
     ```

     

  2. mvn clean install -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven    其中的/home/cache是从哪里来的？

     在部署gitlab-runner的时候，都会默认生成一个gitlab-runner的配置文件`config.toml`，宿主机部署gitlab-runner那么config.toml在/etc/gitlab-runner目录中，docker方式部署gitlab-runner那么是会被映射到宿主机上的，具体在哪里需要去看你的docker run命令。有了config.toml文件，打开你会发现里面各个gitlab-runner的执行器都有对应的配置，大致的内容如下

     ```toml
     concurrent = 1
     check_interval = 0
     
     [session_server]
       session_timeout = 1800
     
     # 一个runner执行器的配置信息
     [[runners]]
       name = "runnerforcentos"  #执行器的名称
       url = "http://172.16.4.145/"
       token = "Pwr6WRMyqRzjUoznay8b"
       executor = "docker"
       [runners.custom_build_dir]
       [runners.cache]
         [runners.cache.s3]
         [runners.cache.gcs]
         [runners.cache.azure]
       [runners.docker]
         tls_verify = false
         image = "alpine:latest"
         privileged = false
         disable_entrypoint_overwrite = false
         oom_kill_disable = false
         disable_cache = false
         volumes = ["/cache"] #执行器需要挂载的目录
         shm_size = 0
     
     # 一个runner执行器的配置信息
     [[runners]]
       ......
     ```

     我们可以修改需要的执行的配置，上面我们的命令是：

     mvn clean install -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven 指定了本地maven仓库的地址，确保不要每次都拉取maven的依赖。那么就需要把本地maven仓库的目录缓存起来，所以我们去改配置文件<font color="red">添加的部分标注了感叹号</font>

     ```toml
     concurrent = 1
     check_interval = 0
     
     [session_server]
       session_timeout = 1800
     
     
     # 一个runner执行器的配置信息
     [[runners]]
       cache_dir = "/home/cache" #设置需要缓存的目录！！！！！！！！！！！！
       name = "sdc-shared-runner-for-build"  #执行器的名称
       url = "http://10.10.210.24:9999/"
       token = "6Ef7xxuxLerMEYzCdasR"
       executor = "docker"
       [runners.custom_build_dir]
       [runners.cache]
         [runners.cache.s3]
         [runners.cache.gcs]
         [runners.cache.azure]
       [runners.docker]
         dns = ["114.114.114.114"]
         tls_verify = false
         image = "alpine:latest"
         privileged = false
         disable_entrypoint_overwrite = false
         oom_kill_disable = false
         disable_cache = false
         
         #执行器需要挂载的目录
         volumes = ["/cache",
         "/opt/gitlab-runner/cache:/home/cache:rw"]#将cache映射到宿主机，这样每次起一个新的执行器，之前的maven依赖都会被保留在宿主机然后被新的执行器加载，就不用再次拉取依赖了！
         shm_size = 0
         pull_policy = "if-not-present"
     
     # 一个runner执行器的配置信息
     [[runners]]
       ......
     ```

  3. build.sh干了什么事？

     build.sh的内容如下：

     ```shell
     #! /bin/bash
     
     all_modules_arr=(parking-charge parking-device parking-lot parking-notification parking-order parking-payment parking-member-merchant) #设定一个数组，里面存放要进入到各个目录去打包的目录名
     
     ls -la ./
     
     #如果artifact文件夹不存在，创建文件夹，注意这个artifact目录是job完成后被缓存在gitlab-runner的！！
     if [ ! -d "./artifact" ]; then
       mkdir ./myfolder
     else
       rm -rf ./artifact
       mkdir ./artifact
     fi
     
     for every_module in ${all_modules_arr[*]}; do #遍历上面的数组
       pwd
       cd  ${every_module} #进去到每一个目录
       pwd
       ls
       mkdir ../artifact/${every_module} #在artifact创建对应的目录
       mvn clean package -Dmaven.test.skip=true -gs ./mvn-setting.xml  -Dmaven.repo.local=/home/cache/maven
       cp ./target/*.jar ../artifact/${every_module}/${every_module}.jar #将打好的jar包移动到artifact下当前项目目录中
       cp ./Dockerfile ../artifact/${every_module}/ #将Dockerfile移动到artifact下当前项目目录中
       cp ./deployment.yaml ../artifact/${every_module}/ #将deployment.yaml移动到artifact下当前项目目录中
       ls
       cd ../
       cp ./buildimage.sh ./artifact #将buildimage.sh移动到artifact下
       cp ./deploy.sh ./artifact #将deploy.sh 移动到artifact下
       cp ./dev-config ./artifact #将dev-config移动到artifact下
     done
     
     ```

     

- buildimage阶段

  1. buildimage阶段代码

     ```yaml
     build_docker_image:
       only: #只工作在master分支上
         - master
       variables:
         GIT_CHECKOUT: "false" #不要拉取git仓库中的项目代码
       before_script:
         - docker info
       image: docker:19.03.12 #当前执行器的镜像
       cache:   #将artifact目录放到当前执行器的本地，里面有各个微服务的目录，各个微服务目录中存放着jar包、Dockerfile等
         paths:
           - ${CACHE_DIR}
       stage: buildimage
       tags:
         - buildimage #根据这个tag找到gitlab-runner
       script:
         - ls
         - sh ${CACHE_DIR}buildimage.sh $DOCKER_REGISTRY_USER $DOCKER_REGISRTY_PASSWD $VERSION
     ```

  2. sh ${CACHE_DIR}buildimage.sh $DOCKER_REGISTRY_USER $DOCKER_REGISRTY_PASSWD $VERSION 的目录是什么？

     是运行buildimage.sh的shell脚本，并传入了docker私有仓库的用户名、密码、和镜像的版本

     

  3. buildimage.sh干了什么？

     ```shell
     #! /bin/bash
     
     DOCKER_REGISRTY_PASSWD=$2   #获取到命令上的传参
     DOCKER_REGISTRY_USER=$1
     VERSION=$3
     
     echo $DOCKER_REGISRTY_PASSWD
     echo $DOCKER_REGISTRY_USER
     echo $VERSION
     
     #docker私仓登录
     echo $DOCKER_REGISRTY_PASSWD | docker login '10.10.210.23:5000/cicd/' -u $DOCKER_REGISTRY_USER --password-stdin
     
     #进入到artifact目录
     cd ./artifact
     
     # 遍历artifact目录
     for every_file in `ls ./`; do
       echo $every_file
       if [ -d $every_file ]; then # 进入artifact目录中的子目录
         pwd
         cd $every_file
         pwd
         ls
         echo "docker build -t ${every_file}:${VERSION} -f ./Dockerfile ." #打镜像，并推送镜像到docker私仓
         docker build -t ${every_file}:${VERSION} -f ./Dockerfile .
         docker tag ${every_file}:${VERSION} '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
         docker push '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
         docker rmi '10.10.210.23:5000/cicd/'${every_file}:${VERSION}
         docker rmi ${every_file}:${VERSION}
         cd ../
       fi
     done
     
     touch finish.txt
     
     ```

     因为是在docker中操作宿主机上的docker进行打包并上传镜像到私仓，所以，对应的执行的配置需要有所改变

     ```toml
     concurrent = 1
     check_interval = 0
     
     [session_server]
       session_timeout = 1800
     
     # 一个runner执行器的配置信息
     [[runners]]
       cache_dir = "/home/cache" #设置需要缓存的目录！！！！！！！！！！！！
       name = "sdc-shared-runner-for-buildimage"
       url = "http://10.10.210.24:9999/"
       token = "Tgq2TxFD1pAS8tkvjBwx"
       executor = "docker"
       [runners.custom_build_dir]
       [runners.cache]
         [runners.cache.s3]
         [runners.cache.gcs]
         [runners.cache.azure]
       [runners.docker]
         tls_verify = false
         image = "alpine:latest"
         privileged = true  #这儿需要改成true
         disable_entrypoint_overwrite = false
         oom_kill_disable = false
         disable_cache = false
         #执行器需要挂载的目录
         volumes = ["/var/run/docker.sock:/var/run/docker.sock",  #在发布执行器容器内操作宿主机的docker需要这个!!!!!!
         "/etc/docker/daemon.json:/etc/docker/daemon.json", 
         "/cache", 
         "/opt/gitlab-runner/cache:/home/cache:rw"] #将cache映射到宿主机，这样每次起一个新的执行器，之前的maven依赖都会被保留在宿主机然后被新的执行器加载，就不用再次拉取依赖了！ 
         shm_size = 0
         pull_policy = "if-not-present"
         
     # 一个runner执行器的配置信息
     [[runners]]
       ......
     ```

     



- deploy阶段

  1. deploy阶段代码

     ```yaml
     deploy:
       only:
         - master
       variables:
         GIT_CHECKOUT: "false" #不要拉取git仓库中的项目代码
       image: lucj/kubectl:1.20.4 #使用kubectl的镜像
       cache: #将artifact目录放到当前执行器的本地，里面有各个微服务的目录，各个微服务目录中存放着jar包、Dockerfile等
         paths:
           - ${CACHE_DIR}
       stage: deploy
       tags:
         - deploy
       script:
         - sh ${CACHE_DIR}deploy.sh $VERSION
         - kubectl --kubeconfig=./artifact/dev-config get pods -A
     ```

  2. sh ${CACHE_DIR}deploy.sh $VERSION 命令的干什么的？

     ```shell
     #! /bin/bash
     
     VERSION=$1
     
     echo $VERSION
     
     cd ./artifact
     
     # 遍历artifact目录
     for every_file in `ls ./`; do
       echo '--------------------分隔线------------------'
       echo $every_file
       if [ -d $every_file ]; then # 进入artifact目录中的子目录
         pwd
         cd $every_file
         pwd
         ls
         sed -i "s#__image__#reg.czsdc.com:5000/cicd/${every_file}:${VERSION}#g" deployment.yaml #使用镜像+版本号替换各个项目中的deployment.yaml中的’__images__‘占位符
         cat deployment.yaml
         kubectl --kubeconfig=../dev-config delete -f deployment.yaml 
         kubectl --kubeconfig=../dev-config apply -f deployment.yaml #运行deployment.yaml发布到k8s中去。
         cd ../
       fi
     done
     
     ```

  3. 使用到了deployment.yaml，内容如下

     输入k8s的范畴，这儿不多解释

     ```yaml
     kind: Service
     apiVersion: v1
     metadata:
       name: charge
       namespace: parking
       labels:
         app: charge
     spec:
       clusterIP: None
       ports:
         - name: http
           protocol: TCP
           port: 8095
           targetPort: 8095
         - name: http2
           protocol: TCP
           port: 9095
           targetPort: 9095
       selector:
         app: charge
       type: ClusterIP
       sessionAffinity: None
     ---
     apiVersion: apps/v1
     kind: Deployment
     metadata:
       name: charge
       namespace: parking
       labels:
         app: charge
     spec:
       replicas: 1
       selector:
         matchLabels:
           app: charge
       template:
         metadata:
           labels:
             app: charge
         spec:
           nodeSelector:
             node-network: internet
           containers:
             - name: charge
               image: __image__
               ports:
                 - containerPort: 8095
                   protocol: TCP
                 - containerPort: 9095
                   protocol: TCP
               resources:
                 limits:
                   cpu: 1000m
                   memory: 500Mi
                 requests:
                   cpu: 10m
                   memory: 10Mi
               terminationMessagePath: /dev/termination-log
               terminationMessagePolicy: File
               imagePullPolicy: IfNotPresent
           restartPolicy: Always
           terminationGracePeriodSeconds: 30
           dnsConfig:
             nameservers:
               - 114.114.114.114
               - 8.8.8.8
           imagePullSecrets:
             - name: parking-secret
       strategy:
         type: RollingUpdate
         rollingUpdate:
           maxUnavailable: 25%
           maxSurge: 25%
       revisionHistoryLimit: 10
       progressDeadlineSeconds: 600
     
     ```

     