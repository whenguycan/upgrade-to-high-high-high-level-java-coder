## 暴露elasticsearch的监控指标到prometheus



#### 1、说在前面

关于elasticsearch内存持续增长的问题：

当用户把数据写入到elasticsearch中，最终数据都会被落盘到本地的硬盘上，再根据用户对elasticsearch的`index.store.type`配置项，如果配置成mmapfs，那么elasticsearch会把落在盘上的所有数据映射到内存中。随着用户写入数据量的增多，落盘的数据也增多，那么内存占用就越来越大！映射硬盘数据的内存位于本地系统中，即不属于JVM的堆内存，也不属于MaxDirectMemorySize 控制的堆外内存。



解决方案：

修改`index.store.type`的配置即可，不要使用mmapfs的方式。改用niofs，niofs性能也很好，对检索的性能影响并不大，并且能够有效的控制内存增长。

具体不同版本的修改方式不一样：

这儿可以选择不同版本：https://www.elastic.co/guide/en/elasticsearch/reference/index.html

具体的某个版本的示范：https://www.elastic.co/guide/en/elasticsearch/reference/7.12/index-modules-store.html



配置好了，可以使用chrome的elasticsearch head插件，然后点击某个索引的`信息`按钮，找到`集群节点信息` ,查找index---store---type即可。

![avatar](./images/MG399.jpeg)



#### 2、监控方案

可以使用Prometheus 社区开发了elasticsearch_exporter，来导出elasticsearch  JVM 的监控指标，以便使用 Prometheus 来采集监控数据。



#### 3、elasticsearch集成elasticsearch_exporter步骤

> 采用将elasticsearch_exporter单独部署为docker容器，给elasticsearch_exporter指定好具体的elasticsearch资源地址，让elasticsearch_exporter通过指定地址拉取elasticsearch的JVM指标，并提供指标到prometheus。
>
> <font color="red">但是官方对于docker的支持比较少，所以我们选择自己打包！</font>



- 操作步骤

  - 服务器上新建文件夹es_export，并进入文件夹

    

  - 下载elasticsearch_exporter的压缩包，并放入es_export文件夹

    下载地址：https://github.com/prometheus-community/elasticsearch_exporter/tags

    

  - 在es_export目录中创建Dockerfile文件

    ```dockerfile
    FROM centos:7
    
    RUN mkdir -p /es_export
    
    WORKDIR /es_export
    
    ADD ./elasticsearch_exporter.tar.gz ./
    
    EXPOSE 9114
    
    ENTRYPOINT ["/es_export/elasticsearch_exporter-1.3.0.linux-amd64/elasticsearch_exporter", "--es.timeout=10s", "--web.listen-address=:9114", "--web.telemetry-path=/metrics", "--es.uri=http://elastic:xxxxx@192.168.1.200:9200"]
    ```

    --es.uri 这里如果有验证就写成basic auth的方式，如果没有验证直接写http://IP:9200

  ​		更多启动参数：https://github.com/prometheus-community/elasticsearch_exporter

  

  - 在es_export目录中运行如下命令，创建镜像

    ```shell
    docker build -f Dockerfile . -t es_export:1.3.0
    ```

  - 镜像打好之后，运行起容器

    ```shell
    docker run -id --name=es_export -p 9114:9114 es_export的镜像ID
    ```

  - 容器运行之后，访问http://IP:9114/metrics查看是否拿到指标信息。