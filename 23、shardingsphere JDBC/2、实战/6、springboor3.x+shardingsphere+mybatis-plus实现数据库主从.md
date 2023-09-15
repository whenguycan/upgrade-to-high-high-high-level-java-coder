## springboor3.x+shardingsphere+mybatis-plus实现数据库主从

#### 1. 确保版本关系

```xml
<!-- springboot版本为3.1.1 -->

<!-- mysql的连接驱动 -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.28</version>
</dependency>

<!-- mybatis-plus的版本 -->
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>3.5.3.1</version>
</dependency>

<!-- shardingsphere-jdbc的版本 -->
<dependency>
  <groupId>org.apache.shardingsphere</groupId>
  <artifactId>shardingsphere-jdbc-core-spring-boot-starter</artifactId>
  <version>5.2.1</version>
</dependency>
```



#### 2. 新建2个数据库，分别往数据库中导入如下1张表

```sql
CREATE TABLE `order_1` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user` int unsigned DEFAULT '0',
  `num` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;


```



#### 3. 确保正常使用mybatis-plus

先去掉shardingsphere-jdbc的依赖，测试看使用mybatis-plus能不能把数据正常写入到一张表中



#### 4. 项目添加如下配置

```yaml
spring:
  shardingsphere:
    mode:
      type: Standalone
      repository:
        type: JDBC
    datasource:
      names: ds1,ds2
      ds1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://10.10.210.19:3306/user_1?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8 # 数据库连接
        username: root # 用户名
        password: root # 密码
      ds2:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://10.10.210.19:3306/user_2?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8 # 数据库连接
        username: root # 用户名
        password: root # 密码
    rules:
      readwrite-splitting: #读写分离配置
        load-balancers: #配置负载均衡策略
          roundRobin: #策略名称
            type: ROUND_ROBIN #负载均衡策略的类型
        data-sources:
          readwrite_ds: # 读写分离逻辑数据源名称 随便起
            staticStrategy:
              writeDataSourceName: ds1 #指定写入的数据源
              readDataSourceNames: #指定读取数据的数据源
                - ds2
            loadBalancerName: roundRobin #使用哪个负载均衡策略
    props:
      sql-show: true
```



#### 5. 测试使用

```java
@GetMapping("/add-order")
public void addOrder(){
  OrderEntity orderEntity = new OrderEntity();
  orderEntity.setUser(3);
  orderEntity.setNum(3);
  orderEntity.setContent("yyyyy");
  orderService.save(orderEntity);
}

@GetMapping("/get-order")
public List<OrderEntity> getOrder(){
  return orderService.getBaseMapper().selectList(new QueryWrapper<OrderEntity>().eq("num", 3));
}

@GetMapping("/delete-order")
public void deleteOrder(){
  orderService.getBaseMapper().delete(new QueryWrapper<OrderEntity>().le("num", 2));
}

@GetMapping("/update-order")
public void updateOrder(){
  var orderEntity = new OrderEntity();
  orderEntity.setContent("mmm");
  orderService.getBaseMapper().update(orderEntity, new QueryWrapper<OrderEntity>().lt("num", 4));
}
```

