## springboot3.x+sharingsphere+mybatis-plus实现水平分表



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



#### 2. 往数据库中导入如下2张表

```sql
CREATE TABLE `order_1` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `num` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;


CREATE TABLE `order_2` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `num` varchar(255) NOT NULL DEFAULT '',
  `content` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
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
      names: ds1 #给数据源起个名称
      ds1: #数据源信息配置
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://10.10.210.19:3306/user?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8 
        username: root # 用户名
        password: root # 密码
    rules: #数据分片规则
      sharding:
        tables: 
          t_order: #逻辑表的表名，在XxxEntity中@TableName中也要填写这个逻辑表的表名
            actual-data-nodes: ds1.order_$->{1..2} #逻辑表有哪些数据节点，数据节点由数据源名称.真实表 得到
            table-strategy: #数据表数据的分片策略
              standard:
                sharding-column: num #参与分片的字段
                sharding-algorithm-name: tetete #使用什么分片算法
        sharding-algorithms: #分片算法
          tetete:
            type: INLINE #使用行表达式分片策略
            props:
              algorithm-expression: order_$->{ num % 2 + 1} #具体的分片策略表达式为 根据num的值对2取余 + 1得到 order_1还是order_2表
              allow-range-query-with-inline-sharding: true #在查询的时候，是否允许使用范围查询
    props:
      sql-show: true
```



#### 5. 注意事项

1. 在entity指定@TableName(“这儿的名字要跟配置文件中逻辑表的名称一致”)

   ```java
   @TableName("t_order") //这儿填写逻辑表的表名
   @Data
   public class OrderEntity {
   
       @TableId(type = IdType.AUTO)
       private Integer id;
   
       private Integer num;
   
       private String content;
   }
   ```



#### 6. 测试使用

```java
@GetMapping("/add-order")
public void addOrder(){
  OrderEntity orderEntity = new OrderEntity();
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



