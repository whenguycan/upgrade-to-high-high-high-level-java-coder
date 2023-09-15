## Maven开发简单的agent

> 目标是，开发探针，当springboot项目每加载一个类，就会打印加载的类名、加载器的类名等信息。



#### 1、新建一个springboot项目

> 这个项目就是用于被agent的目标项目

增加一个HelloWorldController类，类中的代码随便写！



<font color="red">下面所有步骤是开发agent包！跟上面的springboot项目无关</font>

#### 2、进入命令行创建如下层级的目录结构

![avatar](../images/MG424.jpeg)



#### 3、使用idea打开根目录

将java文件夹设置为Sources，将resources设置为Resources，设置好之后如下图

![avatar](../images/MG425.jpeg)

#### 4、新建一个pom.xml文件

pom文件必须与src目录同级，pom文件的内容随便找个现成的抄一抄就行。



#### 5、到java目录中新建package

注意：新建的package，需要与pom文件中的groupId一致



#### 6、开发自己的agent探针

- 编写一个LoadTimeAgent类，内容如下：

  ```java
  public class LoadTimeAgent {
  
      public static void premain(String agentArgs, Instrumentation inst){
          System.out.println("Premain-class:" + LoadTimeAgent.class.getName());
  
          ClassFileTransformer transformer = new InfoTransformer();
          inst.addTransformer(transformer);//将指定的ClassFileTransformer加入到agent中
      }
  
  }
  
  ```

- 编写ClassFileTransformer类，名为InfoTransformer，内容如下：

  ```java
  public class InfoTransformer implements ClassFileTransformer {
      public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
  
          if(className.endsWith("HelloWorldController")){ //判断只有加载到我们自己开发的类，才会打印如下信息
              StringBuilder sb = new StringBuilder();
              Formatter fm = new Formatter(sb);
              fm.format("ClassName: %s%n", className); //打印正在加载的类
              fm.format("ClassLoader: %s%n", loader); //打印加载上面的加载器是类名
              fm.format("ClassBeingRedefined: %s%n", classBeingRedefined);
              fm.format("ProtectionDomain: %s%n", protectionDomain);
              System.out.println(sb.toString());
          }
  
  
          return null;
      }
  }
  ```

  

#### 7、探针项目打包

> 注意，如果探针中依赖了第三方包，在打包的时候，需要将第三方包依赖到jar包，否则探针将无法运行。

- 如果探针中没有依赖第三方依赖，pom文件可以使用如下2个插件进行打包

  > 不要直接拷贝使用，需要针对自己的项目对应修改

  ```xml
  <build>
          <plugins>
  
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-jar-plugin</artifactId>
                  <version>3.2.0</version>
                  <configuration>
                      <archive>
                        
                        <manifest>
                          <!-- 指定项目启动的入口class -->
                          <!-- <mainClass>com.example.mavenplugin01.MavenPlugin01Application</mainClass> -->
                          <!-- 是否在manifest文件中添加classpath。默认为false。如果为true，则会在manifest文件中添加classpath，这样在启动的时候就不用再手动指定classpath了, classpath是指项目所有依赖的jar所在的路径 -->
                          <!-- <addClasspath>true</addClasspath> -->
                          <!-- classpath的前缀，这儿配置是lib，则项目所有的依赖jar包都在lib这个目录中 -->
                          <!--  <classpathPrefix>lib/</classpathPrefix> -->
                          <addDefaultImplementationEntries>false</addDefaultImplementationEntries>
                          <addDefaultSpecificationEntries>false</addDefaultSpecificationEntries>
                        </manifest>
                        
                       
                          <!-- manifestEntries的作用是给manifest文件添加键值对 -->
                          <manifestEntries>
                              <!-- 标签为key，内容为value -->
                            	<!-- Premain-Class指定loadTime agent的入口类 -->
                              <Premain-Class>com.kk.agent.LoadTimeAgent</Premain-Class>
                              <Can-Redefine-Classes>true</Can-Redefine-Classes>
                              <Can-Retransform-Classes>true</Can-Retransform-Classes>
                              <Can-Set-Native-Method-Prefix>true</Can-Set-Native-Method-Prefix>
                          </manifestEntries>
                          <addMavenDescriptor>false</addMavenDescriptor>
                      </archive>
                    	<includes>
                    		<include> <!-- 加了这个，在后面打出来的jar包，就只包含指定路径中的文件 -->
                        	xxx/xxx/xxx  
                        </include>
                    	</includes>
                  </configuration>
              </plugin>
  
  
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
          </plugins>
      </build>
  ```

- 如果探针中有依赖第三方依赖，pom文件可以使用如下插件进行打包

  > 不要直接拷贝使用，需要针对自己的项目对应修改

  ```xml
  <build>
          <plugins>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-assembly-plugin</artifactId>
                  <version>3.3.0</version>
                  <configuration>
                      <archive>
                          <manifest>
                            	<!-- 指定项目启动的入口class -->
                              <!-- <mainClass>com.example.mavenplugin01.MavenPlugin01Application</mainClass> -->
                              <!-- 是否在manifest文件中添加classpath。默认为false。如果为true，则会在manifest文件中添加classpath，这样在启动的时候就不用再手动指定classpath了, classpath是指项目所有依赖的jar所在的路径 -->
                              <!-- <addClasspath>true</addClasspath> -->
                              <!-- classpath的前缀，这儿配置是lib，则项目所有的依赖jar包都在lib这个目录中 -->
                              <!--  <classpathPrefix>lib/</classpathPrefix> -->
                              <addDefaultImplementationEntries>false</addDefaultImplementationEntries>
                              <addDefaultSpecificationEntries>false</addDefaultSpecificationEntries>
                              <addDefaultEntries>false</addDefaultEntries>
                          </manifest>
                          <manifestEntries>
                              <!-- 标签为key，内容为value -->
                              <!-- Premain-Class指定loadTime agent的入口类 -->
                              <Premain-Class>com.example.agent.LoadTimeAgent</Premain-Class>
                              <Can-Redefine-Classes>true</Can-Redefine-Classes>
                              <Can-Retransform-Classes>true</Can-Retransform-Classes>
                              <Can-Set-Native-Method-Prefix>true</Can-Set-Native-Method-Prefix>
                          </manifestEntries>
                      </archive>
  
                      <descriptorRefs>
                          <descriptorRef>jar-with-dependencies</descriptorRef>
                      </descriptorRefs>
  
                  </configuration>
                  <executions>
                      <execution> <!--执行器 mvn assembly:assembly-->
                          <id>make-package</id> <!--名字任意 -->
                          <phase>package</phase> <!-- 绑定到package生命周期阶段上 -->
                          <goals>
                              <goal>single</goal> <!-- 该打包任务只运行一次 -->
                          </goals>
  
                      </execution>
                  </executions>
              </plugin>
          </plugins>
      </build>
  
  ```

  使用assembly-plugin打出来的jar包，叫`xxxx-0.0.1-SNAPSHOT-jar-with-dependencies.jar`



#### 8、使用

```shell
java -javaagent:jarpath -jar springboot项目.jar
```

