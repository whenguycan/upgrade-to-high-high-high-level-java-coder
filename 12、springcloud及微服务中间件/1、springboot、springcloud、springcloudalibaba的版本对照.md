## 版本对照关系

参考：https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E



#### 依赖管理：

Spring Cloud BOM 包含了它所使用的所有依赖版本。

在dependencyManagement中添加如下内容，只需修改对应的version

```xml
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-dependencies</artifactId>
   <version>Hoxton.SR8</version>
   <type>pom</type>
   <scope>import</scope>
</dependency>
```



Spring Cloud Alibaba BOM 包含了它所使用的所有依赖的版本。

在 dependencyManagement 中添加如下内容，只需修改对应的version

```xml
<dependency>
   <groupId>com.alibaba.cloud</groupId>
   <artifactId>spring-cloud-alibaba-dependencies</artifactId>
   <version>2.2.3.RELEASE</version>
   <type>pom</type>
   <scope>import</scope>
</dependency>
```

