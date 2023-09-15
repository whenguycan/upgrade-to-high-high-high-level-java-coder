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
      public void sayHello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloReply> responseObserver) {
        
          
        request.getXXXX();
        
        HelloProto.HelloReply.Builder helloReply = HelloProto.HelloReply.newBuilder();
        helloReply.setMessage("xxxxx");
        HelloProto.HelloReply reply = helloReply.build();
        
        
        
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
      }
  }
  ```
  
  注意点：
  
  - @GrpcService 添加一个注解，会在项目启动的时候，自动扫描带有该注解的service，然后注册到grpc中。
  - 在grpc看来，sayHello的第一个参数是请求参数，最后一个（第二个）参数是返回值。
  - request.getXXXX() 是获取到用户传入的HelloRequest中的xxxx参数。
  - HelloProto.HelloReply.Builder builder = HelloProto.HelloReply.newBuilder(); 用于构建响应消息的内容载体。注意这儿HelloReply对象并没有被创建出来！创建的只是HelloReply.Builder这个对象。
  - helloReply.setMessage("xxxxx"); 往返回体中写数据！
  - HelloProto.HelloReply reply = helloReply.build();  HelloReply到这儿才真正被创建出来！
  - <font color="red">如果message中的属性是repeated，那么有具体的addxxx()的方法是往这个属性中存放值！</font>
  - responseObserver.onNext(reply); 将数据返回！
  - responseObserver.onCompleted(); 告诉底层netty本次处理已经完毕了！