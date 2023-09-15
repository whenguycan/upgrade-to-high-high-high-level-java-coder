## pluginManagement标签



#### 1、使用范例

```xml
<build>
  <pluginManagement>
    <plugins>

      <plugin>
        具体到plugin标签中详细说
      </plugin>

    </plugins>
  </pluginManagement>    
</build>
```



#### 2、范例详述

- 标签详述
  1. <pluginManagement>标签，一般定义在父pom中，<pluginManagement>标签中定义的<plugin>标签中的插件，不会实际被项目引入，而是在这儿规定了插件的版本、配置细节等信息，后续的项目中单独引入这个插件可以不指定版本、配置细节，而默认使用这儿规定的版本和配置细节。<font color="red">父包中的父包中的.....N个父包的<pluginManagement>具有隔N代的传递性！</font>

