#### server标签

用于指定私有仓库信息，一般配合项目的pom.xml文件中的<distributionManagement>标签和<repositories>标签一起使用！



#### 1、使用范例

```xml
<servers>
  <server>
    <id>dsynexus</id>
    <username>admin</username>
    <password>Tangwei123456</password>
  </server>
......
</servers>

```



#### 2、范例详解

- 标签详解
  1. <server>标签，表示下面配置的都是私有仓库的信息
  2. <id>标签，是给当前私有仓库建个身份表示，不能跟别的server的id冲突
  3. <username>标签，表示私有仓库的用户名
  4. <password>标签，表示私有仓库的密码