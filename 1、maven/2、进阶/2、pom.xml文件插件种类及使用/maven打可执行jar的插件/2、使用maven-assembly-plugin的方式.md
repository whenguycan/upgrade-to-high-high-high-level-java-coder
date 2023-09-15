## 使用maven-assembly-plugin的方式

> maven项目推荐使用，springboot项目不推荐使用

maven-assembly-plugin可以将所有的东西都打包到一个jar包中

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
          	<!-- 绑定maven哪个生命周期 -->
            <phase>package</phase>
            <goals>
              	<!-- 只运行一次 --> 
                <goal>single</goal>
            </goals>
            <configuration>
                <archive>
                  <manifest>
                    	<!-- 项目的入口类 -->
                      <mainClass>
                          com.michealyang.jetty.embeded.EmbeddedJettyServer
                      </mainClass>
                  </manifest>
                </archive>
                <descriptorRefs>
                  	<!-- 会将所有依赖都解压打包到jar包中-->
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
```

执行mvn package后，会在target文件夹下生成两个jar包，一个是不带依赖的jar包，一个是后缀有-dependencies带有依赖的jar包



**优点**
所有的东西都打到一个jar包中，很方便
**缺点**
配置项少，不自由。