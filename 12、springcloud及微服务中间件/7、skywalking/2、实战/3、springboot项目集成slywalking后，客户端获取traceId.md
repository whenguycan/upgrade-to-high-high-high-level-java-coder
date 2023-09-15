## 客户端获取traceId

> 目的是方便去skywalking的dashboard中查找链路



- 引入依赖

  ```xml
  <!-- https://mvnrepository.com/artifact/org.apache.skywalking/apm-toolkit-trace -->
  <dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>对应你自己的skywalking的版本</version>
  </dependency>
  ```

- 在需要的地方，写一行代码就行

  ```java
  TraceContext.traceId(); //就会获取到链路ID
  ```

  拿着这个ID就可以去dashboard中查询了！