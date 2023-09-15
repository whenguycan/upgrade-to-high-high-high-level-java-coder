## docker部署seata单机



#### 1、下载seata的安装包

> 目的只是为了使用包中的.sql文件

下载地址：https://github.com/seata/seata/tags



#### 2、准备好一台mysql导入seata需要的数据表

seata需要的数据表信息在上面下载的包中的/script/server/db中的.sql文件，将这个.sql文件导入即可



#### 3、准备好一台可用的nacos

- 在nacos的配置中心，准备好一个dataId，名称为`seataServer.properties`。

- 准备一个config.txt文件，这里面就是写的需要导入到nacos的`seataServer.properties`这个dataId中的数据

  > config.txt在上面下载的seata包的script/config-center中

  但是我们不需要把config.txt文件中所有的配置都导入进去，我们只需要挑重点，比如一下的内容

  ```txt
  service.vgroupMapping.default_tx_group=default //这儿default同第4步中配置文件的registry中的cluster的值,my_test_tx_group同客户端集成seate.tx-service-group的值
  store.mode=db
  store.db.datasource=druid
  store.db.dbType=mysql
  store.db.driverClassName=com.mysql.cj.jdbc.Driver
  store.db.url=jdbc:mysql://10.10.210.19:3306/seata?useUnicode=true&rewriteBatchedStatements=true
  store.db.user=root
  store.db.password=root
  store.db.minConn=5
  store.db.maxConn=30
  store.db.globalTable=global_table
  store.db.branchTable=branch_table
  store.db.distributedLockTable=distributed_lock
  store.db.queryLimit=100
  store.db.lockTable=lock_table
  store.db.maxWait=5000
  ```

  将上述内容复制到nacos配置中心的`seataServer.properties`的dataId中，根据自己的要求修改。



#### 4、创建resources目录，在其中准备seata的server端的配置文件

> 配置文件必须叫application.yml

```yaml
server:
  port: 7091

spring:
  application:
    name: seata-server

logging:
  config: classpath:logback-spring.xml
  file:
    path: ${user.home}/logs/seata
  # extend:
  #  logstash-appender:
  #    destination: 127.0.0.1:4560
  #  kafka-appender:
  #    bootstrap-servers: 127.0.0.1:9092
  #    topic: logback_to_logstash

console: #浏览器打开seata的dashboard的用户名和密码
  user:
    username: seata
    password: seata

seata:
  config: #seata的配置文件在哪里
    # support: nacos, consul, apollo, zk, etcd3
    type: nacos #配置文件在nacos中
    nacos:
      server-addr: 172.16.4.144:8808 #这儿可以指向到nacos的地址，也可以指向到nacos的上层nginx的地址，端口是8808端口是nginx反向代理nacos8848端口端口。
      group: SEATA_GROUP
      username: nacos
      password: nacos
      data-id: seataServer.properties

  registry: #将seata注册到哪里
    # support: nacos, eureka, redis, zk, consul, etcd3, sofa
    type: nacos #注册到nacos
    nacos:
      application: seata-server #注册的名称
      server-addr: 172.16.4.144:8808 #这儿可以指向到nacos的地址，也可以指向到nacos的上层nginx的地址，端口是8808端口是nginx反向代理nacos8848端口端口。
      group: SEATA_GROUP
      # namespace: seata-server
      # tc集群名称
      cluster: default
      username: nacos
      password: nacos
#  server:
#    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
  security:
    secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
    tokenValidityInMilliseconds: 1800000
    ignore:
      urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/api/v1/auth/login
```



#### 5、启动seata服务

```shell
docker run -id -v /root/seata/resources/application.yml:/seata-server/resources/application.yml --name=seata-server  -p 8091:8091 -p 7091:7091 -e SEATA_IP=xx.xx.xx.xx --network=network-name seataio/seata-server:1.6.1
```

常量 SEATA_IP 的作用是：指定seata注册到nacos中的时候，使用的IP。

注意--network指定的网络，需要视情况而定，也可以不指定



#### 6、成功测试

1. 浏览器打开：http://IP:7091/ 用户名和密码都是seata
2. 修改seata需要的配置文件，查看日志是否能得到配置文件修改的日志
3. 查看seata是否正常注册到nacos

