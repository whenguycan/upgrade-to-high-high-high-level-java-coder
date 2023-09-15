## java的maven项目使用sonar检测代码质量

maven项目，不用sonar-scanner，只要在pom文件中加点配置就行了。

> 官方参考：https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/



#### 1、生成sonar的token

我的账号----->安全---->生成令牌



#### 2、修改本地的.m2文件夹中的settings.xml的配置：

- 找到pluginGroups标签，添加如下配置：

  ```xml
  <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
  ```

- 找到profiles标签，添加如下配置：

  ```xml
  <profile>
    <id>sonar</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
  
    <properties>
      <sonar.host.url>http://10.10.210.24:9000</sonar.host.url>
    </properties>
  </profile>
  ```



#### 3、进入项目或者微服务目录

运行如下命令：

```shell
mvn clean verify sonar:sonar -Dmaven.test.skip=true -Dsonar.login=第一步中生成的token
```



#### 4、如果要修改projectName等信息，

可以通过修改项目下的pom.xml文件来修改，找到properties标签，然后填写

```xml
<sonar.projectName>xxxxxx</sonar.projectName>
```

具体还有哪些属性可以修改，参考：https://docs.sonarqube.org/latest/analysis/analysis-parameters/



#### 5、等待命令运行完毕，去sonar的后台查看自己项目的bug

