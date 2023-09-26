## 部署package到私有maven仓库



#### 1、在项目的pom.xml文件中添加如下代码

> 如果是多module多父子层级的项目，只要写在根项目的pom.xml文件中。

```xml
<distributionManagement>
  <repository>
    <id>xxxx</id><!-- 此处id和settings.xml的id保持一致 -->
    <name>xxxx</name>
    <url>xxxx</url><!-- 如果是上传到release仓库，项目的version一定要带RELASE，比如：0.0.5.RELEASE -->
  </repository>
  
  <snapshotRepository><!--指定构件的快照部署到哪里。如果没有配置该元素，默认部署到repository元素配置的仓库，参见distributionManagement/repository元素 -->
        <id>xxx</id><!-- 此处id和settings.xml的id保持一致 -->
        <name>xxx</name>
        <url>xxx</url>
    </snapshotRepository>
  
</distributionManagement>
```

如果我们deployment的仓库被group仓库关联了，那么部署的包在group中同样有一份。

#### 2、在maven的settings.xml文件中添加

```xml
<servers>
  <server>
        <id>id自定义</id>
        <username>用户名</username>
        <password>密码</password>
   </server>
</servers>
```



#### 3、去掉项目中的spring-boot-maven-plugin插件



#### 4、在项目中运行

```shell
mvn clean deploy
```



