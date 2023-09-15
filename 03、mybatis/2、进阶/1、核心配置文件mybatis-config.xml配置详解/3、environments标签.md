## environments配置

MyBatis 可以配置成适应多种环境，这种机制有助于将 SQL 映射应用于多种数据库之中， 现实情况下有多种理由需要这么做。例如，开发、测试和生产环境需要有不同的配置；



```xml
<configuration>
    <environments default="development"> <!-- 这儿的default是负责切换数据源的，default中的值是每个enviroment中的id -->
      
      
      <environment id="development"> <!-- 指定开发库的配置 -->
        <transactionManager type="JDBC"/> <!-- 事务的管理我们一般不动 -->
        <dataSource type="POOLED"> <!-- 数据库的连接池信息，这儿我们一般也不动 -->
          <!-- 下面的配置是连接mysql的信息 -->
          <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
          <property name="url" value="jdbc:mysql://localhost:3306/tw?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai"/>
          <property name="username" value="root"/>
          <property name="password" value="tangwei123456"/>

        </dataSource>
      </environment>
      
      
      <environment id="test">   <!-- 指定测试库的配置 -->
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
          <!-- 下面的配置是连接mysql的信息 -->
          <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
          <property name="url" value="jdbc:mysql://localhost:3306/tw?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai"/>
          <property name="username" value="root"/>
          <property name="password" value="tangwei123456"/>
        </dataSource>
      </environment>
      
      
      ......
      
      
      
  </environments>
</configuration>
```

