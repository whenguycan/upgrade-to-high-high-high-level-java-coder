## 使用xxxStub开发grpc-client端服务完成双向流式通讯

重复第1、2、3篇文章的过程，第2篇中编写的.proto文件必须与服务端一模一样



#### 1、移动第3篇文章中生成的文件到项目目录

因为使用插件生成的文件编译文件和通信文件都在target目录中，我们代码中需要使用它就必须把文件移动到项目目录中。



#### 2、编写客户端调用服务端代码

- 方式一

  手动创建连接，发起请求

  ```java
  public class DemoClientGrpcApplication {
  
      public static void main(String[] args) {
          ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9092).usePlaintext().build();
          HelloServiceGrpc.HelloServiceStub stub = HelloServiceGrpc.newStub(channel); //注意，这儿实例化的是HelloServiceStub 而不是HelloServiceBlockingStub
  
          //构建监听服务器返回响应
          StreamObserver<HelloProto.HelloReply> responseObserver =new StreamObserver<HelloProto.HelloReply>(){
  
              //获取到从服务端获取到的响应内容，响应一次获取一次
              @Override
              public void onNext(HelloProto.HelloReply helloReply) {
                  System.out.println(helloReply.getMessage());
              }
  
              @Override
              public void onError(Throwable throwable) {
  
              }
  
              @Override
              public void onCompleted() {
                  System.out.println("处理完毕");
              }
          };
  
          //构建请求observer，用于发起请求
          StreamObserver<HelloProto.HelloRequest> requestOberver = stub.sayHello(responseObserver);
  
          //发送一个请求
          requestOberver.onNext(HelloProto.HelloRequest.newBuilder().setName("tangwei").build());
          //发送一个请求
          requestOberver.onNext(HelloProto.HelloRequest.newBuilder().setName("tangwei").build());
  
          //告诉服务端，请求全部发完了
          requestOberver.onCompleted();
  
          //一定要休息一下，否则直接关闭了通讯了
          try {
              Thread.sleep(10000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
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
      private HelloServiceGrpc.HelloServiceStub helloServiceStub;
  
      @GetMapping("/index")
      public void index(){
  
          //构建监听服务器返回响应
          StreamObserver<HelloProto.HelloReply> responseObserver =new StreamObserver<HelloProto.HelloReply>(){
  
              //获取到从服务端获取到的响应内容
              @Override
              public void onNext(HelloProto.HelloReply helloReply) {
                  System.out.println(helloReply.getMessage());
              }
  
              @Override
              public void onError(Throwable throwable) {
  
              }
  
              @Override
              public void onCompleted() {
                  System.out.println("处理完毕");
              }
          };
  
          //构建请求observer，用于发起请求
          StreamObserver<HelloProto.HelloRequest> requestOberver =helloServiceStub.sayHello(responseObserver);
  
          //发送一个请求
          requestOberver.onNext(HelloProto.HelloRequest.newBuilder().setName("tangwei").build());
          //发送一个请求
          requestOberver.onNext(HelloProto.HelloRequest.newBuilder().setName("tangwei").build());
  
          //告诉服务端，请求全部发完了
          requestOberver.onCompleted();
  
          //一定要休息一下，否则直接关闭了通讯了
          try {
              Thread.sleep(10000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
        
      }
  
  }
  
  ```
  
  



