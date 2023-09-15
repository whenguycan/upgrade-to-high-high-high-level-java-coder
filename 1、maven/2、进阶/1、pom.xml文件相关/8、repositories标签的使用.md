## repositories标签



#### 1、使用范例

```xml
<repositories>
  <repository>
    <id>dsynexus</id>
    <name>legend-package</name>
    <url>http://47.114.152.124:8081/repository/maven-releases</url>
  </repository>
</repositories>
```



#### 2、范例详解

- 标签详解
  1. <repositories>标签，是在配置当前项目需要使用私有仓库中的jar包时配置。
  2. <repository>标签，是表示配置仓库信息。
  3. <id>标签，需要对应到settings.xml文件中，配置的私有仓库信息的<server>标签中<id>标签，保持两者值一致即可。
  4. <name>标签，给当前的`distributionManagement`的配置起个名字。
  5. <url>标签，表示当前jar包发布到的私有仓库的地址，实例：<url>http://地址/repository/maven-releases/</url>

