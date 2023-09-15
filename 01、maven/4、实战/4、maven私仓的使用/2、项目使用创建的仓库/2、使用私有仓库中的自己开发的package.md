## 使用maven仓库中的jar包

> 以下三种方式选择一种就行。



#### 方式1：setting.xml文件中，使用<profile>标签的方式

> 注意，我们这儿配置的repository的type是group的类型。

- 在settings.xml文件中添加<server>标签

  ```xml
  <servers>
    <server>
          <id>id自定义</id>
          <username>用户名</username>
          <password>密码</password>
     </server>
  </servers>
  ```

- 在settings.xml文件中添加如下profile标签及代码

  ```xml
  <profile>
    <id>nexus-public</id> <!-- 这儿的id自定义 -->
    <repositories>
      <repository>
        <id>dsynexus</id> <!-- 这个id需要跟上面的server的id对应 -->
        <name>local private nexus release</name>
        <url>http://47.114.152.124:8081/repository/consume_group</url>
      </repository>
    </repositories>
    <pluginRepositories>
      <pluginRepository>
        <id>dsynexus</id>
        <name>Team Nexus Repository</name>
        <url>http://47.114.152.124:8081/repository/comsume_group</url>
      </pluginRepository>
     </pluginRepositories>
  </profile>
  ```

  并激活这个profile

- 到项目的pom.xml文件中引入需要的依赖

  ```xml
  <dependency>
    <groupId>com.dianxin</groupId>
    <artifactId>authentionlt</artifactId>
    <version>0.0.2-RELEASE</version>
  </dependency>
  ```

- 使用`mvn clean package` 测试是否有用。



#### 方式2：setting.xml文件中，使用<profile>标签 + <mirror>标签的方式

> 注意，我们这儿配置的repository的type是group的类型。

- 在settings.xml文件中添加<server>标签

  ```xml
  <servers>
    <server>
          <id>id自定义</id>
          <username>用户名</username>
          <password>密码</password>
     </server>
  </servers>
  ```

- 在settings.xml文件中添加如下profile标签及代码

  ```xml
  <profile>
    <id>nexus-public</id> <!-- 这儿的id自定义 -->
    <repositories>
      <repository>
        <id>dsynexus</id> <!-- 这个id需要跟上面的server的id对应 -->
        <name>local private nexus release</name>
        <url>http://47.114.152.124:8081/repository/consume_group</url>
      </repository>
    </repositories>
    <pluginRepositories>
      <pluginRepository>
        <id>dsynexus</id>
        <name>Team Nexus Repository</name>
        <url>http://47.114.152.124:8081/repository/comsume_group</url>
      </pluginRepository>
     </pluginRepositories>
  </profile>
  ```

  并激活这个profile

- 在settings.xml文件中添加如下mirror标签及代码

  ```xml
  <mirror>
    <id>dsynexus</id> <!-- 这儿的id不能随便起，需要跟要用到的server的id保持一致 -->
    <mirrorOf>dsynexus</mirrorOf>
    <name>dsy</name>
    <url>http://47.114.152.124:8081/repository/consume_group/</url>
  </mirror>
  ```

- 到项目的pom.xml文件中，引入我们自己开发的已经存放到仓库中的jar包

  ```xml
  <dependency>
    <groupId>com.dianxin</groupId>
    <artifactId>authentionlt</artifactId>
    <version>0.0.2-RELEASE</version>
  </dependency>
  ```

- 项目reimport下看看

#### 方式3：在项目pom.xml文件中，使用<repositories>标签的方式

- 在settings.xml文件中添加<server>标签

  ```xml
  <servers>
    <server>
          <id>id自定义</id>
          <username>用户名</username>
          <password>密码</password>
     </server>
  </servers>
  ```

  

- 在项目的pom.xml文件中，使用<repositories>标签配置仓库地址

  ```xml
  <repositories>
    <repository>
      <id>dsynexus</id>
      <name>legend-package</name>
      <url>http://47.114.152.124:8081/repository/comsume_group/</url>
    </repository>
  </repositories>
  ```

- 使用`mvn clean package` 测试是否有用。





