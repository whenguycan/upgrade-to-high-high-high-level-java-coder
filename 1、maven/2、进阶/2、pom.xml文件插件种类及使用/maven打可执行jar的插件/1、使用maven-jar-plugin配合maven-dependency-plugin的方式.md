## 使用maven-jar-plugin配合maven-dependency-plugin的方式

> 不推荐使用



首先，maven-jar-plugin的作用是打包时配置manifest文件，修改其中的配置项，比如mainClass和指定classpath等

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-jar-plugin</artifactId>
  <version>3.2.0</version>
  <configuration>
    <archive>
      <!-- 修改manifest的配置 -->
      <manifest>
        <!-- 指定项目启动的启动类 -->
        <mainClass>lsieun.Main</mainClass>
        <!-- 是否在manifest文件中添加classpath。默认为false。如果为true，则会在manifest文件中添加classpath，这样在启动的时候就不用再手动指定classpath了, classpath是指项目所有依赖的jar所在的路径 -->
        <addClasspath>true</addClasspath>
        <!-- classpath的前缀，这儿配置是lib，则项目所有的依赖jar包都在lib这个目录中 -->
        <classpathPrefix>lib/</classpathPrefix>
        <addDefaultImplementationEntries>false</addDefaultImplementationEntries>
        <addDefaultSpecificationEntries>false</addDefaultSpecificationEntries>
      </manifest>
      
      <!-- manifestEntries的作用是给manifest文件添加键值对 -->
      <manifestEntries>
        <!-- 标签为key，内容为value -->
        <Premain-Class>lsieun.agent.LoadTimeAgent</Premain-Class>
        <Agent-Class>lsieun.agent.DynamicAgent</Agent-Class>
        <Launcher-Agent-Class>lsieun.agent.LauncherAgent</Launcher-Agent-Class>
        <Can-Redefine-Classes>true</Can-Redefine-Classes>
        <Can-Retransform-Classes>true</Can-Retransform-Classes>
        <Can-Set-Native-Method-Prefix>true</Can-Set-Native-Method-Prefix>
      </manifestEntries>
      <addMavenDescriptor>false</addMavenDescriptor>
    </archive>
    <!-- 需要添加到jar包中的文件 -->
    <includes>
      <include>lsieun/**</include>
    </includes>
  </configuration>
</plugin>
```

maven-jar-plugin插件总结，打出来的jar包中没有相关依赖，所以是可以依赖的jar包，但不是可执行的jar包！



如果需要相关依赖，需要使用maven-dependency-plugin处理项目，该插件是处理项目依赖的插件

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-dependency-plugin</artifactId>
  <version>3.2.0</version>
  <executions>
    <execution>
      <!-- id可以随便配置 -->
      <id>lib-copy-dependencies</id>
      <!-- 绑定maven的哪个生命周期 -->
      <phase>package</phase>
      <!--  -->
      <goals>
        <goal>copy-dependencies</goal>
      </goals>
      <configuration>
        <excludeArtifactIds>tools</excludeArtifactIds>
        <!-- 指定了要将所依赖的jar包copy到哪个目录, 要与maven-jar-plugin中的classpathPrefix一致-->
        <outputDirectory>${project.build.directory}/lib</outputDirectory>
        <overWriteReleases>false</overWriteReleases>
        <overWriteSnapshots>false</overWriteSnapshots>
        <overWriteIfNewer>true</overWriteIfNewer>
      </configuration>
    </execution>
  </executions>
</plugin>
```



**优点**
有诸多配置项，很自由，每个步骤都可控

**缺点**
打成的最终jar包中没有所依赖的jar包。依赖跟自己的代码不在一个jar包中。部署或者移动的时候，要考虑到多个文件，比较麻烦。



