## springboot使用SLF4J + Logback的依赖佐证



在我们引入`spring-boot-starter-web`后，会自动引入`spring-boot-starter`，又会自动引入`spring-boot-starter-logging`。

在`spring-boot-starter-logging`中，我们发现，它引入了如下的依赖

```xml
<dependency> <!-- 默认使用logback做为日志的实现，在这个依赖中又依赖了logback-core、slf4j-api，所以我们说，springboot默认使用 SLF4J + Logback -->
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
  <version>1.4.8</version>
  <scope>compile</scope>
</dependency>

<dependency> <!-- 如果使用了log4j的日志实现，那么使用这个依赖会把log4j的日志转成slf4j，最终还是用 SLF4J + Logback-->
  <groupId>org.apache.logging.log4j</groupId>
  <artifactId>log4j-to-slf4j</artifactId>
  <version>2.20.0</version>
  <scope>compile</scope>
</dependency>
<dependency> <!-- 如果使用了jul的日志实现，那么使用这个依赖会把log4j的日志转成slf4j，最终还是用 SLF4J + Logback-->
  <groupId>org.slf4j</groupId>
  <artifactId>jul-to-slf4j</artifactId>
  <version>2.0.7</version>
  <scope>compile</scope>
</dependency>
```

