## springboot3.x +shardingsphere+mybatis-plus实现水平分库，如果有sql没有携带分库策略字段怎么办？

如果没有携带分库策略的字段，那么就会以第一个配置的数据库为主库，数据只会在主库操作！



#### 测试

往两个数据库中分别带入如下表

```sql
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
```

在已经做好分库的基础上，user表进行测试，看是否只会在主库上操作

