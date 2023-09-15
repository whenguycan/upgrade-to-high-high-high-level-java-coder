## dependencyManagement标签



#### 1、使用范例

```xml
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.apache.activemq</groupId>
        <artifactId>activemq-amqp</artifactId>
        <version>${activemq.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
  </dependencies>
</dependencyManagement>
```



#### 2、范例详述

- 标签详述

  1. <dependencyManagement>标签，一般定义在父pom中，<dependencyManagement>标签中定义的<dependency>标签中的依赖，不会实际被项目引入，而是在这儿规定一个版本，后续的项目中单独引入这个依赖可以不指定版本，而默认使用这儿规定的版本。<font color="red">父包中的....N个父包的<dependencyManagement>具有隔N代的传递性！</font>

  2. <type>标签，固定值为pom，一般是配合<scope>import</scope>一起使用的。

  3. <scope>标签，在<dependencyManagement>标签内使用<scope>标签其值只能是import，代表被依赖的项目的pom文件中所有的<dependencyManagement>标签内<dependency>标签中的依赖都会被复制到本项目中。

  4. <type> + <scope> 的作用在于：当一个父pom中的dependencyManagement 标签中需要导入另一个pom中的dependencyManagement的时候，必须同时使用<scope>import</scope> 和 <type>pom</type>

     

