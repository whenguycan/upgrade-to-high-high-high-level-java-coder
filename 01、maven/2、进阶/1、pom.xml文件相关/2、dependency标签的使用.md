## dependency标签



#### 1、使用范例

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
  <exclusions>
    <exclusion>
      <artifactId>log4j-api</artifactId>
      <groupId>org.apache.logging.log4j</groupId>
    </exclusion>
    <exclusion>
      <groupId>com.vaadin.external.google</groupId>
      <artifactId>android-json</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```



#### 2、范例详述

- 标签详述
  1. <dependency>标签，表示当前项目依赖<dependency>标签内的项目。<font color="red">父包中的父包中的.....N个父包的<dependency>具体隔N代的传递性！</font>
  2. <scope>标签，表示当前项目依赖<dependency>标签内的项目会参与到当前项目的哪个生命周期中。常见的生命周期参数为：
     - compile，<scope>标签内的默认值，表示被依赖包需要参与当前项目的编译、测试、运行。所以该依赖会被打到jar包。
     - provided，表示被依赖包需要参与当前项目的编译、测试。可以认为在目标容器中已经提供了这个依赖，无需在提供，但是在编写代码或者编译时可能会用到这个依赖。依赖不会被打入到项目jar包中。
     - test，在一般的编译和运行时都不需要，它们只有在测试的编译和测试的运行阶段可用，不会被打包到项目jar包中。同时如果项目A依赖于项目B，项目B中的test作用域下的依赖不会被继承。
     - runtime，与compile比较相似，区别在于runtime 跳过了编译阶段，该依赖会被打到jar包。
     - system，表示使用本地系统路径下的jar包，需要和一个systemPath一起使用。
     - import，该<scope>import</scope>，只允许使用在<dependencyManagement>标签定义的<dependency>标签中。表示将该<dependency>标签中的项目的pom.xml文件中所有的<dependency>标签引入的依赖全部都复制到当前项目中。参考文章：https://blog.csdn.net/oro99/article/details/115161370
  3. <exclusions>标签，表示当前项目依赖<dependency>标签内的项目，但是排除<dependency>标签内的项目中的<exclusions>标签中的项目。

