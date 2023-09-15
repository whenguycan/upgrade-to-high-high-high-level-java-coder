## profile标签



它包含了id、activation、repositories、pluginRepositories和 properties元素。



```xml
<profiles>
  
  <profile>
    <!-- profile的唯一标识 在运行maven命令的时候，指定profile的id，可以激活并使用profile，mvn clean install -Pid1, id2-->
    <id>test</id>

    <!-- 自动触发profile的条件逻辑 -->
    <activation>
      <!--profile默认是否激活的标识 -->
      <activeByDefault>false</activeByDefault>

      <!--当匹配的jdk被检测到，profile被激活。例如，1.4激活JDK1.4，1.4.0_2，而!1.4激活所有版本不是以1.4开头的JDK。 -->
      <jdk>1.5</jdk>



      <!--在运行maven命令的时候，如果Maven检测到某一个属性，其值等于配置文件中配置的值，Profile就会被激活。如果值字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段，例子：mvn clean install -D mavenVersion=2.0.3 -->
      <property>
        <!--激活profile的属性的名称 -->
        <name>mavenVersion</name>
        <!--激活profile的属性的值 -->
        <value>2.0.3</value>
      </property>


    </activation>

    <!-- 扩展属性列表，当profile被激活了，在pom.xml文件中就能使用其中定的变量${xxxx.xxxx} -->
    <properties>

    </properties>

    <!-- 远程仓库列表 -->
    <repositories>
      <repository>
        <id></id>
        <name></name>
        <url></url>
      </repository>
    </repositories>

    <!-- 插件仓库列表 -->
    <pluginRepositories>
      <pluginRepository>
        <id></id>
        <name></name>
        <url></url>
      </pluginRepository>
    </pluginRepositories>

  </profile>
  
</profiles>
```





profile只有被激活了，才会起作用，可以使用activeProfile标签来激活某个profile，<font color="red">最好只激活需要使用的那个profile</font>

```xml
<activeProfiles>
  <activeProfile>profile的id</activeProfile>
</activeProfiles>
```

