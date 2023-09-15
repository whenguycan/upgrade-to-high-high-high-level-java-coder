## k8s搭建seata集群

> 注意：
>
> 	1. seata server集群，必须依赖nacos的注册中心、配置中心和mysql数据库来实现。
> 	2. 只要符合第1点要求，多启动几个seata server就已经是高可用了。



#### k8s知识点

1. 无状态应用部署
2. configmap的挂载使用



#### 准备工作

1. 准备好可用的mysql，并且已经导入如下数据表，库名自己取。

   ```sql
   CREATE TABLE `branch_table` (
     `branch_id` bigint(20) NOT NULL,
     `xid` varchar(128) NOT NULL,
     `transaction_id` bigint(20) DEFAULT NULL,
     `resource_group_id` varchar(32) DEFAULT NULL,
     `resource_id` varchar(256) DEFAULT NULL,
     `branch_type` varchar(8) DEFAULT NULL,
     `status` tinyint(4) DEFAULT NULL,
     `client_id` varchar(64) DEFAULT NULL,
     `application_data` varchar(2000) DEFAULT NULL,
     `gmt_create` datetime(6) DEFAULT NULL,
     `gmt_modified` datetime(6) DEFAULT NULL,
     PRIMARY KEY (`branch_id`),
     KEY `idx_xid` (`xid`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   
   CREATE TABLE `global_table` (
     `xid` varchar(128) NOT NULL,
     `transaction_id` bigint(20) DEFAULT NULL,
     `status` tinyint(4) NOT NULL,
     `application_id` varchar(32) DEFAULT NULL,
     `transaction_service_group` varchar(32) DEFAULT NULL,
     `transaction_name` varchar(128) DEFAULT NULL,
     `timeout` int(11) DEFAULT NULL,
     `begin_time` bigint(20) DEFAULT NULL,
     `application_data` varchar(2000) DEFAULT NULL,
     `gmt_create` datetime DEFAULT NULL,
     `gmt_modified` datetime DEFAULT NULL,
     PRIMARY KEY (`xid`),
     KEY `idx_gmt_modified_status` (`gmt_modified`,`status`),
     KEY `idx_transaction_id` (`transaction_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   
   CREATE TABLE `lock_table` (
     `row_key` varchar(128) NOT NULL,
     `xid` varchar(96) DEFAULT NULL,
     `transaction_id` bigint(20) DEFAULT NULL,
     `branch_id` bigint(20) NOT NULL,
     `resource_id` varchar(256) DEFAULT NULL,
     `table_name` varchar(32) DEFAULT NULL,
     `pk` varchar(36) DEFAULT NULL,
     `gmt_create` datetime DEFAULT NULL,
     `gmt_modified` datetime DEFAULT NULL,
     PRIMARY KEY (`row_key`),
     KEY `idx_branch_id` (`branch_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   
   ```

   

2. 准备好可用的nacos，并把seata的相关配置导入到nacos的配置中

   >  运行如下shell脚本，可以直接到../目录中查找到config.txt文件并且读取config.txt文件中的配置保存到指定nacos的配置中。

   ```shell
   #!/usr/bin/env bash
   # Copyright 1999-2019 Seata.io Group.
   #
   # Licensed under the Apache License, Version 2.0 (the "License");
   # you may not use this file except in compliance with the License.
   # You may obtain a copy of the License at、
   #
   #      http://www.apache.org/licenses/LICENSE-2.0
   #
   # Unless required by applicable law or agreed to in writing, software
   # distributed under the License is distributed on an "AS IS" BASIS,
   # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   # See the License for the specific language governing permissions and
   # limitations under the License.
   
   while getopts ":h:p:g:t:u:w:" opt
   do
     case $opt in
     h)
       host=$OPTARG
       ;;
     p)
       port=$OPTARG
       ;;
     g)
       group=$OPTARG
       ;;
     t)
       tenant=$OPTARG
       ;;
     u)
       username=$OPTARG
       ;;
     w)
       password=$OPTARG
       ;;
     ?)
       echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
       exit 1
       ;;
     esac
   done
   
   urlencode() {
     for ((i=0; i < ${#1}; i++))
     do
       char="${1:$i:1}"
       case $char in
       [a-zA-Z0-9.~_-]) printf $char ;;
       *) printf '%%%02X' "'$char" ;;
       esac
     done
   }
   
   if [[ -z ${host} ]]; then
       host=192.168.2.235    #这儿需要修改，会被命令行的 -h取代
   fi
   if [[ -z ${port} ]]; then
       port=30873            #这儿需要修改，会被命令行的 -p取代
   fi
   if [[ -z ${group} ]]; then
       group="SEATA_GROUP"    #这儿需要修改，会被命令行的 -g取代
   fi
   if [[ -z ${tenant} ]]; then  #这儿需要修改，会被命令行的 -t取代
       tenant=""              
   fi
   if [[ -z ${username} ]]; then
       username=""            #这儿需要修改
   fi
   if [[ -z ${password} ]]; then
       password=""           #这儿需要修改
   fi
   
   nacosAddr=$host:$port
   contentType="content-type:application/json;charset=UTF-8"
   
   echo "set nacosAddr=$nacosAddr"
   echo "set group=$group"
   
   failCount=0
   tempLog=$(mktemp -u)
   function addConfig() {
     curl -X POST -H "${contentType}" "http://$nacosAddr/nacos/v1/cs/configs?dataId=$(urlencode $1)&group=$group&content=$(urlencode $2)&tenant=$tenant&username=$username&password=$password" >"${tempLog}" 2>/dev/null
     if [[ -z $(cat "${tempLog}") ]]; then
       echo " Please check the cluster status. "
       exit 1
     fi
     if [[ $(cat "${tempLog}") =~ "true" ]]; then
       echo "Set $1=$2 successfully "
     else
       echo "Set $1=$2 failure "
       (( failCount++ ))
     fi
   }
   
   count=0
   for line in $(cat $(dirname "$PWD")/config.txt | sed s/[[:space:]]//g); do
     (( count++ ))
   	key=${line%%=*}
       value=${line#*=}
   	addConfig "${key}" "${value}"
   done
   
   echo "========================================================================="
   echo " Complete initialization parameters,  total-count:$count ,  failure-count:$failCount "
   echo "========================================================================="
   
   if [[ ${failCount} -eq 0 ]]; then
   	echo " Init nacos config finished, please start seata-server. "
   else
   	echo " init nacos config fail. "
   fi
   
   ```

   config.txt可以使用的所有配置有如下：

   ```tex
   transport.type=TCP
   transport.server=NIO
   transport.heartbeat=true
   transport.enableClientBatchSendRequest=true
   transport.threadFactory.bossThreadPrefix=NettyBoss
   transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
   transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
   transport.threadFactory.shareBossWorker=false
   transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
   transport.threadFactory.clientSelectorThreadSize=1
   transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
   transport.threadFactory.bossThreadSize=1
   transport.threadFactory.workerThreadSize=default
   transport.shutdown.wait=3
   service.vgroupMapping.my_test_tx_group=default
   service.default.grouplist=127.0.0.1:8091
   service.enableDegrade=false
   service.disableGlobalTransaction=false
   client.rm.asyncCommitBufferLimit=10000
   client.rm.lock.retryInterval=10
   client.rm.lock.retryTimes=30
   client.rm.lock.retryPolicyBranchRollbackOnConflict=true
   client.rm.reportRetryCount=5
   client.rm.tableMetaCheckEnable=false
   client.rm.tableMetaCheckerInterval=60000
   client.rm.sqlParserType=druid
   client.rm.reportSuccessEnable=false
   client.rm.sagaBranchRegisterEnable=false
   client.rm.tccActionInterceptorOrder=-2147482648
   client.tm.commitRetryCount=5
   client.tm.rollbackRetryCount=5
   client.tm.defaultGlobalTransactionTimeout=60000
   client.tm.degradeCheck=false
   client.tm.degradeCheckAllowTimes=10
   client.tm.degradeCheckPeriod=2000
   client.tm.interceptorOrder=-2147482648
   store.lock.mode=file
   store.session.mode=file
   store.publicKey=
   store.file.dir=file_store/data
   store.file.maxBranchSessionSize=16384
   store.file.maxGlobalSessionSize=512
   store.file.fileWriteBufferCacheSize=16384
   store.file.flushDiskMode=async
   store.file.sessionReloadReadSize=100
   store.db.datasource=druid
   store.db.dbType=mysql
   store.db.driverClassName=com.mysql.jdbc.Driver
   store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
   store.db.user=username
   store.db.password=password
   store.db.minConn=5
   store.db.maxConn=30
   store.db.globalTable=global_table
   store.db.branchTable=branch_table
   store.db.queryLimit=100
   store.db.lockTable=lock_table
   store.db.maxWait=5000
   store.redis.mode=single
   store.redis.single.host=127.0.0.1
   store.redis.single.port=6379
   store.redis.sentinel.masterName=
   store.redis.sentinel.sentinelHosts=
   store.redis.maxConn=10
   store.redis.minConn=1
   store.redis.maxTotal=100
   store.redis.database=0
   store.redis.password=
   store.redis.queryLimit=100
   server.recovery.committingRetryPeriod=1000
   server.recovery.asynCommittingRetryPeriod=1000
   server.recovery.rollbackingRetryPeriod=1000
   server.recovery.timeoutRetryPeriod=1000
   server.maxCommitRetryTimeout=-1
   server.maxRollbackRetryTimeout=-1
   server.rollbackRetryTimeoutUnlockEnable=false
   server.distributedLockExpireTime=10000
   client.undo.dataValidation=true
   client.undo.logSerialization=jackson
   client.undo.onlyCareUpdateColumns=true
   server.undo.logSaveDays=7
   server.undo.logDeletePeriod=86400000
   client.undo.logTable=undo_log
   client.undo.compress.enable=true
   client.undo.compress.type=zip
   client.undo.compress.threshold=64k
   log.exceptionRate=100
   transport.serialization=seata
   transport.compressor=none
   metrics.enabled=false
   metrics.registryType=compact
   metrics.exporterList=prometheus
   metrics.exporterPrometheusPort=9898
   ```

   config.txt的常用配置有如下,<font color="red">我们一般用这儿的，同时config文件中的注释需要去掉！</font>：

   ```tex
   service.vgroupMapping.my_test_tx_group=default   //这儿default同下面的创建comfigmap中的registry中的cluster的值
   store.mode=db
   store.db.datasource=druid
   store.db.dbType=mysql
   store.db.driverClassName=com.mysql.cj.jdbc.Driver
   store.db.url=jdbc:mysql://192.168.2.235:30991/seata_db?useUnicode=true&rewriteBatchedStatements=true  //这儿的数据库使用上面准备好了的
   store.db.user=root
   store.db.password=tangwei123456
   store.db.minConn=5
   store.db.maxConn=30
   store.db.globalTable=global_table
   store.db.branchTable=branch_table
   store.db.queryLimit=100
   store.db.lockTable=lock_table
   store.db.maxWait=5000
   
   ```

   准备好如上脚本文件和config.txt文件之后，只需要执行脚本文件即可，-t意为 是nacos下的哪个namespace：

   ```shell
   sh xxxx.sh -h 192.168.10.194 -p 8848 -g SEATA_GROUP -t 22ccafce-22ca-4d59-949e-e0eeee783fb4
   ```

   > -h 为nacos server的IP
   >
   > -p 为nacos server的port
   >
   > -g 为nacos server的group
   >
   > -t 为nacos server的namespace



#### 搭建过程

- 创建一个configmap，后续deployment中要使用

  > 目的是告诉seata server 把seata服务注册到哪里去，并且seata需要的配置在哪里读取。

  ```yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: seata-server-file-conf
    namespace: lanzhen-membrance
  data:
    registry.conf: |
      registry {  #seata注册到哪个nacos中去
          type = "nacos"
          nacos {
            application = "seata-server"
            serverAddr = "192.168.2.235:30565"
            group = "SEATA_GROUP" #这个不能少
            namespace: ""
            cluster = "default"
            username = "nacos"
            password = "nacos"
          }
      }
      config {  #seata从哪个nacos读取配置，配置项不是存在一个文件中，而是配置名就是Data Id，值时具体的内容
        type = "nacos"
        nacos {
           serverAddr = "192.168.2.235:30565"
           group = "SEATA_GROUP"
           namespace: ""
           username = "nacos"
           password = "nacos"
        }
      }
  
  ```

- 使用deployment部署seata服务

  ```yaml
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: seata-server-cluster
    namespace: lanzhen-membrance
    labels:
      k8s-app: seata-server-cluster
  spec:
    replicas: 3
    selector:
      matchLabels:
        k8s-app: seata-server-cluster
    template:
      metadata:
        labels:
          k8s-app: seata-server-cluster
      spec:
        containers:
          - name: seata-server-cluster
            image: docker.io/seataio/seata-server:1.4.1
            imagePullPolicy: IfNotPresent
            env:  #注意这个一定要，因为 docker中启动seata，不知道去哪里找配置文件，这儿就指定了从哪里去找配置文件
              - name: SEATA_CONFIG_NAME
                value: file:/root/seata-config/registry   #对应下面的挂载的配置文件路径，一定要file:开头，且结尾不要.conf
            ports:
              - name: http
                containerPort: 8091
                protocol: TCP
            volumeMounts:
              - name: seata-config
                mountPath: /root/seata-config  #挂载的配置文件的实际路径为/root/seata-config/registry.conf
        volumes:
          - name: seata-config
            configMap:
              name: seata-server-file-conf
              items:
                - key: registry.conf
                  path: registry.conf
              defaultMode: 420
  



seata server已经可以使用了，无需暴露端口。