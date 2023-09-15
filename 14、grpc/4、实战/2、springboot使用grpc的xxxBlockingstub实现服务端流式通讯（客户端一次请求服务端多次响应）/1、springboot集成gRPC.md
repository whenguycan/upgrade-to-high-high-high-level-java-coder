## springboot集成gRPC



#### 1、引入依赖

根据你当前服务器是服务端还是客户端，引入的依赖是不同的

- 当前服务是 客户端 + 服务端

  ```xml
  <dependency>
    <groupId>net.devh</groupId>
    <artifactId>grpc-spring-boot-starter</artifactId>
    <version>2.13.1.RELEASE</version>
  </dependency>
  ```

- 当前服务是 客户端

  ```xml
  <dependency>
    <groupId>net.devh</groupId>
    <artifactId>grpc-client-spring-boot-starter</artifactId>
    <version>2.13.1.RELEASE</version>
  </dependency>
  ```

- 当前服务是 服务端

  ```xml
  <dependency>
    <groupId>net.devh</groupId>
    <artifactId>grpc-server-spring-boot-starter</artifactId>
    <version>2.13.1.RELEASE</version>
  </dependency>
  ```

​	



引入了上面的依赖，会自动引入如下依赖

- grpc-netty-shaded： 这个模块是代表grpc底层的通信组件是基于netty开发的
- grpc-protobuf： grpc支持protobuf的工具包
- grpc-stub ：处理客户端stud(存根)的工具。



#### 2、引入maven插件

不管是客户端还是服务端都需要引入

```xml
<build>
  <extensions>
    <extension>
      <groupId>kr.motd.maven</groupId>
      <artifactId>os-maven-plugin</artifactId>
      <version>1.6.1</version>
    </extension>
  </extensions>

  <plugins>
    <plugin>
    <groupId>org.xolstice.maven.plugins</groupId>
    <artifactId>protobuf-maven-plugin</artifactId>
    <version>0.6.1</version>
    <configuration>
      <protocArtifact>com.google.protobuf:protoc:3.19.2:exe:${os.detected.classifier}</protocArtifact>
      <pluginId>grpc-java</pluginId>
      <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.53.0:exe:${os.detected.classifier}</pluginArtifact>
    </configuration>
    <executions>
      <execution>
        <goals>
          <goal>compile</goal>
          <goal>compile-custom</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
  </plugins>
</build>
```



其中`os-maven-plugin`是帮忙获取到下面插件要用的环境变量`os.detected.classifier`。



这个`protobuf-maven-plugin`插件，在java代码运行中会自动扫描项目中/src/main/proto文件夹中的.proto文件，使用这个插件把我们写好的.proto文件编译成java代码。

`<protocArtifact>com.google.protobuf:protoc:3.19.2:exe:${os.detected.classifier}</protocArtifact>`这一段是使用我们进阶中`本地安装编译.proto文件的工具`一文中安装好的protoc工具，对项目中的.proto文件进行编译生成java代码。

`<pluginArtifact>io.grpc:protoc-gen-grpc-java:1.53.0:exe:${os.detected.classifier}</pluginArtifact>`用于生成grpc的底层通信类，简化我们开发中底层的数据处理过程。

