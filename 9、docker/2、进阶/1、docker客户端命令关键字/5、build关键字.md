## build关键字

> 镜像仓库中，没有发现我们需要的镜像，那么就需要我们自己去制作镜像了！
>
> build关键字，就是使用我们写好的Dockerfile去打镜像的。



- 打镜像

  ```shell
  docker build -f Dockerfile -t image名称:1.0.1 .
  ```

  `-f` 指定使用的Dockerfile文件

  `-t`镜像的名称和版本

  `.`不能少，是指在打包的时候的环境时在当前dockerfile的目录





Dockerfile示例文件，这儿先看看，后面我们会详细学习Dockerfile

```dockerfile
FROM centos:7
RUN mkdir -p /tt
WORKDIR /tt
RUN touch tt.txt
ENTRYPOINT ["tail","-f", "tt.txt"]
```

