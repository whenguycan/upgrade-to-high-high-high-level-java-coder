## profile标签

> 该标签同样可以写在maven的setting.xml文件中。用法一致。

它包含了id、activation、repositories、pluginRepositories和 properties元素。



```xml
<profiles>
  
  <profile>
    <!-- profile的唯一标识 在运行maven命令的时候，指定profile的id，可以激活并使用profile，mvn clean install/package -P id1, id2-->
    <id>test</id>

    <!-- 自动触发profile的条件逻辑 -->
    <activation>
      <!--profile默认是否激活标识，如果默认激活了，就不需要在运行打包命令mvn clean install/package 的时候加 -P 参数 -->
      <activeByDefault>false</activeByDefault>


      <!--在运行maven命令的时候，如果Maven检测到某一个属性，其值等于配置文件中配置的值，Profile就会被激活。如果值字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段，例子：mvn clean install -D mavenVersion=2.0.3 -->
      <property>
        <!--激活profile的属性的名称 -->
        <name>mavenVersion</name>
        <!--激活profile的属性的值 -->
        <value>2.0.3</value>
      </property>

    </activation>
		
    <!-- 定义的一个常量，后续可以在配置文件中使用，表示如果profile起作用了，那么就会定义变量 -->
    <properties>
      <profiles.active>dev</profiles.active>
    </properties>
   
  </profile>
  
  <profile>
    
    <id>test</id>
    <!-- 定义的一个常量，后续可以在配置文件中使用，表示如果profile起作用了，那么就会定义变量 -->
    <properties>
      <profiles.active>test</profiles.active>
      <!--  众多参数配置 -->
    </properties>
    
  </profile>
  
</profiles>
```

上述profile定义好了后，还不能够区分项目环境，需要在application.yml文件中，添加如下

```yaml
spring:
  profiles:
    active: @profiles.active@  #这儿的@profiles.active@就是使用pom文件中定义的properties参数
```

