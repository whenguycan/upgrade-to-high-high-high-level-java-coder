## 使用Canal需要确定mysql开启了binlog日志并配置授权账户



#### 1、修改mysql的配置文件my.cnf，添加如下内容

```tex
server_id=1  #当前mysql的实例id，不能重复
log-bin=mysql-bin.log #生成binlog日志
binlog_format=row 
binlog-do-db=user #指定哪个库开启binlog日志，如果不配置，默认全部数据库都会进行日志收集
```



#### 2、查看binlog日志是否开启

````mysql
show VARIABLES like 'log_bin';
````

如果出现 “ON”就证明，mysql已经打开了binlog日志



#### 3、查看binlog是不是ROW模式

```shell
show VARIABLES like 'binlog_format';
```



#### 4、授权当前mysql账户，便于后面canal能拉取binlog

创建账号

```shell
CREATE USER canal IDENTIFIED BY 'canal';
```

授予权限

```shell
GRANT all ON *.* TO 'canal'@'%';
```

刷新并应用

```shell
FLUSH PRIVILEGES
```

测试下使用canal能不能通过远程连上mysql！

