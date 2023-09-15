## 使用xxxBlockingStub开发grpc-client端完成服务端流式通讯

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
           ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9092).usePlaintext().build();
          HelloServiceGrpc.HelloServiceBlockingStub blockingStub = HelloServiceGrpc.newBlockingStub(channel);
  
          //构建请求数据
          HelloProto.HelloRequest.Builder request = HelloProto.HelloRequest.newBuilder();
          request.setName("tangwei");
          HelloProto.HelloRequest req = request.build();
  
          //发起请求，获取响应对象，注意这儿的响应体因为服务端返回了多个，所以，客户端这儿拿到的响应是一个可迭代对象
          Iterator<HelloProto.HelloReply> response= blockingStub.sayHello(req);
          while (response.hasNext()){
              HelloProto.HelloReply res = response.next();
              System.out.println(res.getMessage());
          }
  
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
        	//发起请求，获取响应对象，注意这儿的响应体因为服务端返回了多个，所以，客户端这儿拿到的响应是一个可迭代对象
          Iterator<HelloProto.HelloReply> response= helloServiceBlockingStub.sayHello(req);
          while (response.hasNext()){
              HelloProto.HelloReply res = response.next();
              System.out.println(res.getMessage());
          }
        
      }
  
  }
  
  ```
  
  



