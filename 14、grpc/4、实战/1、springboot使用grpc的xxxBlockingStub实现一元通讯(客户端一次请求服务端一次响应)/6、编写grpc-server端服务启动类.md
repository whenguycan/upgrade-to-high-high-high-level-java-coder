## 编写grpc-server端服务启动类



#### 方式一

这个方法在springboot中不用！

我直接把springboot的启动类改造了下就可以了！其实就是一个netty的服务，并把我们前面写的service纳入管理

```java
public class DemoGrpcApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9999).addService(new HelloService()).build().start();
        System.out.println("GRPC正在工作并监听9999端口");
        server.awaitTermination();
    }

}
```



#### 方式二

使用配置文件配置的方式

```yml
spring:
  application:
    name: hello-server
grpc:
  server: # 这个是grpc的端口
    port: 9090
server: # 这个是web服务的端口
  port: 8080
```

