## docker安装mysql



#### 1、拉取mysql8.0的镜像

运行如下命令：

```shell
docker pull mysql:8.0
```



#### 2、准备mysql的配置文件

配置文件名称为my.cnf

```txt
[client]
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4

[mysqld]
port=3306

net_read_timeout=600 #mysql的读写超时时间
net_write_timeout=600

innodb_buffer_pool_size=4G  #调整innodb的内存块大小

datadir=/var/lib/mysql
secure_file_priv=/var/lib/mysql

max_connections=2000

max_connect_errors=10

default-storage-engine=INNODB

slow_query_log = ON
long_query_time = 1

read_buffer_size = 2M
read_rnd_buffer_size = 8M
sort_buffer_size = 8M
join_buffer_size = 8M
thread_cache_size = 8

init_connect='SET collation_connection = utf8mb4_unicode_ci'
init_connect='SET NAMES utf8mb4'
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
collation-server=utf8mb4_unicode_ci
skip-character-set-client-handshake
skip-name-resolve

server_id=1
log-bin=mysql-bin.log
binlog_format=row
binlog-do-db=user #指定哪个库开启binlog日志

replicate-ignore-db=mysql
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema
```



#### 3、如需在mysql部署的过程中，初始化一个数据库并导入数据

-  把要导入的数据准备成.sql文件

- 准备一个shell脚本，mysql在初始化过程中会执行该脚本

  ```shell
  mysql -uroot -pxsdfopNdfret << EOF
  
  create database \`fast-parking\`; #创建数据库
  use \`fast-parking\`; #切换到当前数据库
  source /docker-entrypoint-initdb.d/fast_parking.sql; #导入数据
  
  create database \`fast-parking-member-merchant\`;
  use \`fast-parking-member-merchant\`;
  source /docker-entrypoint-initdb.d/fast_parking_member_merchant.sql;
  
  create database \`fast-parking-order\`;
  use \`fast-parking-order\`;
  source /docker-entrypoint-initdb.d/fast_parking_order.sql;
  
  create database \`fast-parking-payment\`;
  use \`fast-parking-payment\`;
  source /docker-entrypoint-initdb.d/fast_parking_payment.sql;
  
  EOF
  ```

  



#### 4、启动mysql

```shell
docker run --name mysql8 -v /data/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=xsdfopNdfret -d -v /conf/mysql/my.cnf:/etc/mysql/my.cnf -p 13306:3306 mysql的镜像ID
```

如果有需要导入的数据，需要添加 `-v xxxx.sql:/docker-entrypoint-initdb.d`和`-v xxxx.sh:/docker-entrypoint-initdb.d/db-init.sh `。
