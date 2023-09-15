## springboot3.x +shardingsphere+mybatis-plus实现水平分库后操作公共表

当我们使用了shardingsphere jdbc的分库的策略之后，如果配置了公共表！在CRUD数据的时候，是操作公共表，shardingsphere jdbc会把sql广播到所有的库，然后执行语句，保证公共表的数据一致！！如果某个公共表在某个库中不存在的时候，就会抛异常。在新增、删除、更新数据的时候，

配置公共表，只要在分库的基础上添加如下的配置即可

```yaml
spring:
  shardingsphere:
    rules:
      sharding:
        broadcast-tables: 真实表的名称

```

