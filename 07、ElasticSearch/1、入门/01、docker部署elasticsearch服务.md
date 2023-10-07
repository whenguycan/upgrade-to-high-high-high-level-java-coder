## docker部署es服务



生产服务器上，需要修改系统的参数！文件打开数和socket连接数等等！



#### 1、拉取elasticsearch的镜像

```shell
docker pull elasticsearch:7.12.0
```



#### 2、创建挂载文件

```shell
mkdir /data

cd /data

mkdir es

cd es

mkdir config data plugins logs

cd ../

chmod -R 0777 es
```



#### 3、新增elasticsearch.yml文件到config中

```shell
vim /data/es/config/elasticsearch.yml

写入：
http.host: 0.0.0.0
```



#### 4、启动容器

> 9200端口用于跟client端与es服务端通讯，9300用于集群间通讯，这儿不用开，是因为这儿是单机部署。

```shell
docker run -i -p 9200:9200 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms84m -Xmx512m" -v /data/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /data/es/data:/usr/share/elasticsearch/data -v /data/es/plugins:/usr/share/elasticsearch/plugins -v /data/es/logs:/usr/share/elasticsearch/logs -d --name=elasticsearch elasticsearch:7.12.0
```



#### 5、测试是否成功

```shell
curl http://localhost:9200
```

