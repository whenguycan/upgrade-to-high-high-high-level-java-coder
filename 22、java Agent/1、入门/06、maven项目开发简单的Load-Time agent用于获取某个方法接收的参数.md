## Maven开发简单的agent

> 目标是，开发探针，当springboot中一个方法被调用了，会打印出这个方法的入参



#### 1、新建一个springboot项目

> 这个项目就是用于被agent的目标项目

增加一个HelloWorldController类，类中代码如下：

```java
package com.example.agentspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:33 PM
 * @Description: 类描述信息
 */
@RestController
public class HelloWorldController {

    @GetMapping("/index")
    public void index() throws InterruptedException {

        //打印jvm的进程id号
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(nameOfRunningVM);


        int count = 600;
        for (int i = 0; i < count; i++) {

            String info = String.format("|%03d| %s remains %03d seconds", i, nameOfRunningVM, (count - 1));
            System.out.println(info);

            Random random = new Random(System.currentTimeMillis());
            int a = random.nextInt(10);
            int b = random.nextInt(10);
            boolean flag = random.nextBoolean();
            String message;
            if(flag){
                message = String.format("a + b = %d", HelloWorldController.add(a, b));
            }else{
                message = String.format("a - b = %d", HelloWorldController.sub(a, b));
            }
            System.out.println(message);

            TimeUnit.SECONDS.sleep(1);

        }
    }


    public static int add(int a, int b){
        return a + b;
    }

    public static int sub(int a, int b){
        return a - b;
    }
}

```

调用index路径，会打印以下的日志

```shell
|002| 20518@tangweideMacBook-Pro.local remains 598 seconds
a - b = 5
|003| 20518@tangweideMacBook-Pro.local remains 597 seconds
a + b = 9
```





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
  package com.example.agent;
  
  //import cn.hutool.core.date.DatePattern;
  //import cn.hutool.core.date.DateTime;
  
  import jdk.internal.org.objectweb.asm.ClassReader;
  
  import java.lang.instrument.ClassFileTransformer;
  import java.lang.instrument.IllegalClassFormatException;
  import java.security.ProtectionDomain;
  import java.util.Formatter;
  
  /**
   * @Auther: tangwei
   * @Date: 2023/5/6 12:42 PM
   * @Description: 类描述信息
   */
  public class InfoTransformer implements ClassFileTransformer {
      public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        
  
          //判断一些不需要处理的类，直接返回null，代表不处理。
          if (className == null) return null;
          if (className.startsWith("java")) return null;
          if (className.startsWith("javax")) return null;
          if (className.startsWith("jdk")) return null;
          if (className.startsWith("sun")) return null;
          if (className.startsWith("org")) return null;
          if (className.startsWith("com")) return null;
  
          if(className.endsWith("HelloWorldController")){
  
              //读取到类文件的字节码
              ClassReader classReader = new ClassReader(classfileBuffer);
  
              //具体的字节码操作都在这儿进行，目前暂时不用知道怎么处理的！
  
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



- 如果项目中使用了内置的asm库，是在java的的rt.jar包中的，我们的项目打包rt.jar是不会被引入的，导致打包不通过，需要我们在pom.xml文件中，手动把rt.jar在编译的时候加进去，就能正常打包了！

  ```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.1</version>
    <configuration>
      <!--  rt包没有打到项目中去 -->
      <!-- 多个包用;号分隔！ -->
      <compilerArguments>
       <bootclasspath>/Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/lib/rt.jar</bootclasspath>
      </compilerArguments>
  
    </configuration>
  </plugin>
  ```

  

  

#### 8、使用

```shell
java -javaagent:jarpath -jar springboot项目.jar
```

