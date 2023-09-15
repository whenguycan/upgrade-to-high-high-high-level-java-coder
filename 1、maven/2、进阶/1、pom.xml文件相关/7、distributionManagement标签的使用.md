## distributionManagement标签



#### 1、使用范例

```xml
<distributionManagement>
  <repository>
    <id>xxxx</id>
    <name>xxxx</name>
    <url>xxxx</url> <!-- 如果是上传到release，项目的version一定要带RELASE，比如：0.0.5.RELEASE -->
  </repository>
  
  <snapshotRepository><!--指定构件的快照部署到哪里。如果没有配置该元素，默认部署到repository元素配置的仓库，参见distributionManagement/repository元素 -->
        <id>xxx</id><!-- 此处id和settings.xml的id保持一致 -->
        <name>xxx</name>
        <url>xxx</url>
    </snapshotRepository>
  
</distributionManagement>
```

需要配合settingx.xml文件的<server>标签配置使用。

#### 2、范例详解

- 标签详解

  1. <distributionManagement>标签，是我们自己的jar需要deploy到私有maven仓库的时候，才需要配置的标签。<font color="red">父包中的父包中的.....N个父包的<distributionManagement>具有隔N代的传递性！</font>

  2. <repository>标签，是表示配置的非snapshots版本的jar包仓库信息。

  3. <id>标签，需要对应到settings.xml文件中，配置的私有仓库信息的<server>标签中<id>标签，保持两者值一致即可。

  4. <name>标签，给当前的`distributionManagement`的配置起个名字。

  5. <url>标签，表示当前jar包发布到的私有仓库的地址，实例：<url>http://地址/repository/maven-releases/</url>

  5. <snapshotRepository>指定快照jar包的deploy的仓库信息，注意在<version>中带了snapshots关键字的都自动会deploy到snapshotRepository指定的url中。

     

- 注意：执行mvn clean deploy将项目打包并发布到宿主仓库，发布成功后，如果项目版本为0.0.1-SNAPSHOT，是带SNAPSHOT的快照版本则到Browse中maven-snapshots库查看、如果不带SNAPSHOT则到Browse中maven-releases库中查看。