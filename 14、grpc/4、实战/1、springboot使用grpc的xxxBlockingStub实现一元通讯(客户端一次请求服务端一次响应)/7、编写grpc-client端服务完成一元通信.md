## 使用xxxBlockingStub开发grpc-client端服务完成一元通信

重复第1、2、3篇文章的过程，但是第二篇中编写的.proto文件必须与服务端一模一样



#### 1、移动第3篇文章中生成的文件到项目目录

因为使用插件生成的文件编译文件和通信文件都在target目录中，我们代码中需要使用它就必须把文件移动到项目目录中。



#### 2、编写客户端调用服务端代码

- 方式一

  手动创建连接，发起请求

  ```java
  public class DemoClientGrpcApplication {
  
      public static void main(String[] args) {
          //创建连接
          ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9999).usePlaintext().build();
          HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(channel);
  
          //构建请求数据
          HelloProto.HelloRequest.Builder request = HelloProto.HelloRequest.newBuilder();
          request.setName("tangwei");
          HelloProto.HelloRequest req = request.build();
  
          //发起请求，获取响应对象
          HelloProto.HelloReply helloReply = blockingStub.sayHello(req);
  
          System.out.println(helloReply.getMessage());//获取到响应数据
  
          //关闭连接
          channel.shutdown();
      }
  
  }
  
  
  ```

  

- 方式二

  先配置配置文件

  ```yaml
  spring:
    application:
      name: hello-client
  server:
    port: 8087
  
  grpc:
    client:
      grpc-demo-server: #自定义的名字
        address: 'static://localhost:9092'
        negotiation-type: plaintext
    server:
      port: 9091
  ```

  到具体发起rpc调用的地方写代码

  ```java
  @RestController
  public class IndexController {
  		
    	//用我们配置文件的配置初始化连接工作，并获得到连接句柄
      @GrpcClient("grpc-demo-server")//这儿用配置文件中自定义的名字
      private HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;
  
      @GetMapping("/index")
      public void index(){
          HelloProto.HelloRequest request = HelloProto.HelloRequest.newBuilder().setName("tagnwei").build();//构建请求数据
          HelloProto.HelloReply helloReply = helloServiceBlockingStub.sayHello(request); //发起请求
          System.out.println(helloReply.getMessage());
      }
  
  }
  
  ```

  



