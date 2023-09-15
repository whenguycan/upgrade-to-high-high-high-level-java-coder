## parent标签



#### 1、使用范例

```xml
.....

<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.6.6</version>
  <relativePath>../</relativePath> <!-- lookup parent from repository -->
</parent>

.....
```

一个pom文件中，只能有一个<parent>标签。因为maven是单继承的。



#### 2、范例详述

- 标签详述

  1. <relativePath>标签，声明父项目的pom.xml文件的相对路径。查找父pom.xml文件的默认顺序：relativePath > 本地仓库 > 远程仓库。没有relativePath标签等同…/pom.xml, 即默认从当前pom文件的上一级目录找。上级目录如果没有，直接从本地仓库找,找不到再从远程仓库找。<font color="red">可以这样理解，就是指明父项目的路径。</font>千万不要写成<relativePath/>这样！会报错的！
  2. 其余标签参考，`基本标签`文档。

  

- parent标签是用来指定当前项目的父项目，在父项目中我们可以做什么？
  1. 使用<dependency>来引入需要的依赖，如果在父项目中引入了依赖，子项目中不用重复引入就已经引入了该依赖。<font color="red">父包中的父包中的.....N个父包的<dependency>具体隔N代的传递性！</font>
  2. 使用<dependencyManagement>来规定子项目中需要引入依赖的版本，注意，在<dependencyManagement>标签中的<dependency>标签中的依赖不会被引入，只是规定版本。<font color="red">父包中的父包中的.....N个父包的<dependencyManagement>具有隔N代的传递性！</font>
  3. 使用<pluginManagement>来规定子项目中需要引入maven插件的版本，注意，在<pluginManagement>标签中的<plugin>标签中的插件不会被引入，只是规定插件的版本。<font color="red">父包中的父包中的.....N个父包的<pluginManagement>具有隔N代的传递性！</font>
  3. 使用<plugin>标签来引入插件，如果在父项目中引入了插件，子项目中不用重复引入就已经引入了该插件。<font color="red">父包中的父包中的.....N个父包的<plugin>具有隔N代的传递性！</font>
  3. 使用<distributionManagement>来指定项目deploy的仓库信息。<font color="red">父包中的父包中的.....N个父包的<plugin>具有隔N代的传递性！</font>



- 注意：
  1. 在<parent>标签内被引入的项目，打包方式一定要是pom，即：<packaging>pom</packaging>。



