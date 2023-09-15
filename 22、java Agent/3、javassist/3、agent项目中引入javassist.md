## agent项目中引入javassist



#### 1、LoadTime agent下具体操作

在agnet的pom.xml文件中

1. 引入依赖

   ```xml
   <!--         https://mvnrepository.com/artifact/org.javassist/javassist-->
   <dependency>
     <groupId>org.javassist</groupId>
     <artifactId>javassist</artifactId>
     <version>3.27.0-GA</version>
   </dependency>
   ```

2. 在`maven-assembly-plugin`打包插件中加上javassist依赖

   ```xml
   <plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-assembly-plugin</artifactId>
     <version>3.3.0</version>
     <configuration>
       <archive>
         <manifest>
           <addDefaultEntries>false</addDefaultEntries>
         </manifest>
         <manifestEntries>
           <!-- 标签为key，内容为value -->
           <!-- Premain-Class指定loadTime agent的入口类 -->
           <Premain-Class>com.example.agent.LoadTimeAgent</Premain-Class>
           
           <!-- 引入javassist的依赖 -->
           <Boot-Class-Path>javassist-3.27.0-GA.jar</Boot-Class-Path>
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
   ```

   



#### 2、dynamic agent下具体操作

在agnet的pom.xml文件中

1. 引入依赖

   ```xml
   <!--         https://mvnrepository.com/artifact/org.javassist/javassist-->
   <dependency>
     <groupId>org.javassist</groupId>
     <artifactId>javassist</artifactId>
     <version>3.27.0-GA</version>
   </dependency>
   ```

2. 在`maven-assembly-plugin`打包插件中<font color="red">不要</font>加上javassist依赖

   ```xml
   <plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-assembly-plugin</artifactId>
     <version>3.3.0</version>
     <configuration>
       <archive>
         <manifest>
           <addDefaultEntries>false</addDefaultEntries>
         </manifest>
         <manifestEntries>
           <!-- 标签为key，内容为value -->
           <!-- Premain-Class指定loadTime agent的入口类 -->
           <Premain-Class>com.example.agent.LoadTimeAgent</Premain-Class>
           
           <!-- 不要引入javassist的依赖 -->
           <!-- <Boot-Class-Path>javassist-3.27.0-GA.jar</Boot-Class-Path> -->
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
   ```

   
