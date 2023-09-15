## 使用maven-jar-plugin配合spring-boot-maven-plugin的方式

> 普通的maven项目也可以使用！springboot项目的首选。



为什么springboot项目，去掉spring-boot-maven-plugin插件，就可以打成一个可依赖的jar包了？因为去掉了spring-boot-maven-plugin插件，项目就会使用maven-jar-plugin打包，maven-jar-plugin打出来的包就是不带依赖的！



<font color="red">spring-boot-maven-plugin可以配合maven-jar-plugin同时使用</font>



springboot项目中已经默认使用了maven-jar-plugin！

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
              	<!-- 一定要写成repackage，看下面的说明 -->
                <goal>repackage</goal>
            </goals>
            <configuration>
              	<!-- 配置了classifier，那么打出来的包一个是可执行的包，一个是可依赖的包 -->
                <classifier>spring-boot</classifier>
              	<!-- 项目的入口类 -->
                <mainClass>
                  org.baeldung.executable.ExecutableMavenJar
                </mainClass>
            </configuration>
        </execution>
    </executions>
</plugin>
```



说明：

为什么一定要写成repackage？

没有加repackage（goal）打出的包是这样的：

![avatar](../../../images/6eb3.png)

加了repackage（goal）是这样的:

![avatar](../../../images/19374.png)

BOOT-INF目录下有两个子目录：classes和lib目录。lib目录存放的是应用依赖的jar包，具体参照Maven的pom.xml中的依赖内容。classes目录下存放的是项目CLASS_PATH下的内容，包括应用代码和配置文件（比如application.yml等），可以理解为repackage将原始Maven打包的jar文件中的除META-INF以外的内容放置到该目录下打包。META-INF目录下存放的是应用相关的元信息，比如JAR包的必要配置文件MANIFEST.MF和Maven的配置文件等。

<font color="red">注意jdk中的依赖没有被加入到jar包中哦！因为，jdk中的依赖是安装在项目运行的环境中的！所以不需要加入到jar包中！</font>



那么使用repackage的作用就在于：

1. 在原始Maven打包形成的jar包基础上，进行重新打包，新形成的jar包不但包含应用类文件和配置文件，而且还会包含应用所依赖的jar包以及Springboot启动相关类（loader等），以此来满足Springboot独立应用的特性；
2. 将原始Maven打包的jar重命名为XXX.jar.original作为原始文件







优点
自由选择打成jar包还是war包，且所有的东西都打到一个包中，很方便。

缺点
添加了一些不必要的Spring和Spring Boot依赖