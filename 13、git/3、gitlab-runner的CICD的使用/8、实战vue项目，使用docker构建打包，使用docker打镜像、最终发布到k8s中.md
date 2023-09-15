## vue项目实战

使用docker构建打包，使用docker打镜像、最终发布到k8s中



#### 1、打开projects目录中vue目录中的park_pc项目

逐行分析.gitlab-ci.yml文件



#### 2、逐行分析.gitlab-ci.yml

```yaml
variables:
  INSTALL_SHELL: "npm install --legacy-peer-deps --registry=https://registry.npmmirror.com"
  BUILD_SHELL: "npm run build:prod"
  CACHE_DIR: 'dist/'
  PROJECT_NAME: 'park_pc'
  VERSION: '${CI_COMMIT_SHA}'
  IMAGE_NAME: "$PROJECT_NAME:$VERSION"
  DOCKER_REGISTRY: '10.10.210.23:5000/cicd/'
  DOCKER_FILE_PATH: "./Dockerfile"
  PUSH_IMAGE_TAG: '$DOCKER_REGISTRY$IMAGE_NAME'
stages:
  - build
  - buildimage
  - deploy

#会自动拉取项目代码到执行器本地
构建项目:
  image: node:16.6.0
  stage: build
  variables:
    HTTP_PROXY: http://10.10.210.19:8888
    HTTPS_PROXY: http://10.10.210.19:8888
  tags:
    - build
  cache: # 工作完成后，会把dist目录缓存起来
    paths:
      - ${CACHE_DIR}
  script:
    - ls
    - cp -r /home/cache/npm/node_modules ./   #将gitlab-runner中缓存的node_modules目录拷贝到本地
    - ls
    - npm install html-webpack-plugin -D --legacy-peer-deps #执行安装命令
    - $INSTALL_SHELL
    - $BUILD_SHELL
    - ls
    - rm -rf /home/cache/npm/node_modules #删除gitlab-runner中缓存的node_modules目录 
    - cp -r ./node_modules /home/cache/npm/ #将最新的node_modules目录放到gitlab-runner的缓存中


制作镜像并推送到镜像仓库:
  cache: #获取到dist目录
    paths:
      - ${CACHE_DIR}
  image: docker:latest 
  stage: buildimage
  tags:
    - buildimage
  script:
    - ls ${CACHE_DIR}
    - ls
    - pwd
    - docker build -t $IMAGE_NAME -f $DOCKER_FILE_PATH .
    - docker tag $IMAGE_NAME $PUSH_IMAGE_TAG
    - echo $DOCKER_REGISTRY_PASSWD | docker login $DOCKER_REGISTRY -u $DOCKER_REGISTRY_USER --password-stdin
    - docker push $PUSH_IMAGE_TAG
    - docker rmi $PUSH_IMAGE_TAG
    - docker rmi $IMAGE_NAME

发布:
  image: lucj/kubectl:1.20.4
  stage: deploy
  tags:
    - deploy
  script:
#    - docker run -d -p 80:80 --name=$PROJECT_NAME$VERSION $IMAGE_NAME
    - sed -i "s#__image__#reg.czsdc.com:5000/cicd/${IMAGE_NAME}#g" deployment.yaml
    - cat deployment.yaml
    - kubectl --kubeconfig=./dev-config delete -f deployment.yaml
    - kubectl --kubeconfig=./dev-config apply -f deployment.yaml

```

具体的内容说明可以参考java部分的，因为大都是雷同的！

