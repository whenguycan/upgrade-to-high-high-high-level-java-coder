## 搭建seate server集群服务

> 注意：
>
> 	1. seata server集群，必须依赖nacos的注册中心、配置中心和mysql数据库来实现。
> 	1. 只要符合第1点要求，多启动几个seata server就已经是高可用了。

#### 1、服务器上新建文件夹seate，并进入文件夹



#### 2、下载jdk包，放到seate文件夹中

> 下载地址：https://www.oracle.com/cn/java/technologies/downloads/archive/

如果不下载，可直接使用这儿的： [jdk-8u301-linux-x64.tar.gz](../files/jdk-8u301-linux-x64.tar.gz)



#### 3、下载seate server的包，放到seate文件夹中

> 注意seate的版本，下载地址：https://github.com/seata/seata/tags

如果不下载，可直接使用这儿的：[seata-server-1.3.0.tar.gz](../files/seata-server-1.3.0.tar.gz)



#### 4、准备好一台可以使用的nacos

> seate需要注册到nacos中



#### 5、准备好一台可用的mysql

> 因为server端的全局事务、分支事务、全局锁都需要存到mysql中

在mysql中新建个database，随后导入如下sql

```mysql
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



#### 6、seata server的启动，所有的配置都放在了nacos的配置中心，所以需要把配置先全部导入到nacos的配置中心中

- 导入脚本nacos-config.sh，内容如下

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
      host=192.168.2.235
  fi
  if [[ -z ${port} ]]; then
      port=30873
  fi
  if [[ -z ${group} ]]; then
      group="SEATA_GROUP"
  fi
  if [[ -z ${tenant} ]]; then
      tenant=""
  fi
  if [[ -z ${username} ]]; then
      username=""
  fi
  if [[ -z ${password} ]]; then
      password=""
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

- 将配置项放到config.txt文件中，内容如下

  ```tex
  service.vgroupMapping.my_test_tx_group=default   //这儿default同第二步中配置文件的registry中的cluster的值
  store.mode=db
  store.db.datasource=druid
  store.db.dbType=mysql
  store.db.driverClassName=com.mysql.cj.jdbc.Driver
  store.db.url=jdbc:mysql://192.168.2.235:30991/seata_db?useUnicode=true&rewriteBatchedStatements=true
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

  

- 运行命令， 即可将配置同步到nacos中，不需要指定config.txt，因为nacos-config.sh会到../ 的目录中去找config.txt

  ```shell
  sh nacos-config.sh -h 192.168.10.194 -p 8848 -g SEATA_GROUP -t 22ccafce-22ca-4d59-949e-e0eeee783fb4
  ```

  

- 附，Nacos中所有能配置的项参考如下文件(我们一般只要配置好store.db相关的就可以了l)：

  [nacos中全量配置参数](../files/config.txt)



#### 7、在seata目录下新建registry.conf

```conf
registry {
    type = "nacos" #seata注册到哪个nacos中去
    nacos {
        application = "seata-server-3"
        serverAddr = "192.168.2.235:32532"
        group = "SEATA_GROUP"
        namespace = "4cdca07a-6863-44f9-8827-74559205bb7b"
        cluster = "default"
        username = "nacos"
        password = "nacos"
    }
}
config { #配置文件从哪个nacos读取，配置项不是存在一个文件中，而是配置名就是Data Id，值时具体的内容
    type = "nacos"
    nacos {
        serverAddr = "192.168.2.235:32532"
        group = "SEATA_GROUP"
        namespace = "4cdca07a-6863-44f9-8827-74559205bb7b"
        username = "nacos"
        password = "nacos"
        }
}
```



#### 8、在seata目录下创建Dockerfile文件

```dockerfile
FROM centos:7

RUN mkdir -p /java8

WORKDIR /java8

ADD ./jdk8.tar.gz ./

ENV JAVA_HOME=/java8/jdk1.8.0_301
ENV CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH=$JAVA_HOME/bin:$PATH

RUN mkdir -p /seata

WORKDIR /seata

EXPOSE 8091

ADD ./seata-server-1.3.0.tar.gz ./
ADD ./registry.conf ./seata/conf/

RUN touch ./test.txt

ENTRYPOINT ["sh", "/seata/seata/bin/seata-server.sh"]
```



#### 9、在seata文件夹下运行如下命令，创建seate的docker镜像

```shell
docker build -f Dockerfile . -t seata-server:1.3.0
```