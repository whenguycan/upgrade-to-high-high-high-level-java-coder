## 暴露docker内的mysql给prometheus监控



#### 1、连接到数据，并创建监控所需的账号和权限

```shell
CREATE USER 'exporter'@'%' IDENTIFIED BY 'exporter';

GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'%';
flush privileges;

select user,host from mysql.user;
```

用新建的账号去连接下mysql，如果能正常连接上，就证明没有问题了！



#### 2、启动读取docker内mysql指标的docker容器

```shell
docker run -d --name mysql_exporter --restart always -p 9104:9104 -e DATA_SOURCE_NAME="exporter:exporter@(121.229.28.212:13306)/" prom/mysqld-exporter --collect.info_schema.innodb_metrics --collect.info_schema.tables --collect.info_schema.processlist --collect.info_schema.tables.databases=* --collect.info_schema.innodb_tablespaces --collect.info_schema.tablestats
```

容器启动之后，访问：http://IP:9104/metrics 查看指标是否有输出



#### 3、去prometheus中添加监控目标



#### 4、grafana中添加mysql监控的dashboard

dashboard的ID：7362