## 开发grpc-server服务端具体业务接口



#### 1、移动第3篇文章中生成的文件到项目目录

因为使用插件生成的文件编译文件和通信文件都在target目录中，我们代码中需要使用它就必须把文件移动到项目目录中。



#### 2、编写grpc的service中的每一个具体的方法

- 新建一个service，这个类一定要继承生成的通信文件`HelloServiceGrpc.HelloServiceImplBase`

  ```jaav
  public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {
  }
  ```

- 编写service的具体方法

  因为在.proto中定义的service中定义了很多方法，这儿都需要一一实现

  ```java
  @GrpcService
  public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {
  
      @Override
      public StreamObserver<HelloProto.HelloRequest> sayHello(StreamObserver<HelloProto.HelloReply> responseObserver) {
  
          return new StreamObserver<HelloProto.HelloRequest> (){
  
              @Override
              public void onNext(HelloProto.HelloRequest helloRequest) {
                  System.out.println("获取到一次请求");
              }
  
              @Override
              public void onError(Throwable throwable) {
                  throwable.printStackTrace();
              }
  
              @Override
              public void onCompleted() {
                  responseObserver.onNext(HelloProto.HelloReply.newBuilder().setMessage("xxxxx").build());
                  responseObserver.onCompleted();
              }
          };
      }
  }
  
  ```
  
  注意点：
  
  - @GrpcService 添加一个注解，会在项目启动的时候，自动扫描带有该注解的service，然后注册到grpc中。
  - 在grpc看来，sayHello的第一个参数是请求参数，最后一个（第二个）参数是返回值。
  - 流式请求是需要有返回值的
  - onNext()方法是有一次请求就会执行一次
  - onCompleted()方法是服务端接收到客户端表名本次的请求已经完毕发送完毕才执行。
  - HelloProto.HelloReply reply = helloReply.build();  HelloReply到这儿才真正被创建出来！
  - <font color="red">如果message中的属性是repeated，那么有具体的addxxx()的方法是往这个属性中存放值！</font>
  - responseObserver.onNext(reply); 将数据返回！
  - responseObserver.onCompleted(); 告诉底层netty本次处理已经完毕了！