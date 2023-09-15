## plugin标签



#### 1、使用范例

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
  </plugins>
</build>
```





#### 2、范例详解

- 标签详解
  1. <plugin>标签，设置项目中使用哪些插件。<font color="red">父包中的父包中的.....N个父包的<plugin>具有隔N代的传递性！</font>