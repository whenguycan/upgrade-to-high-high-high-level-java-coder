## rpc关键字

在service内部的定义的一个远程调用接口



```protobuf
........

// 这里面定义了一个gRPC服务，里面含有一个接口，并且还有这个接口的入参和返回结果的定义
service HelloService {
  // RPC方法定义
  rpc SayHello(HelloRequest) returns (HelloReply) {}
}

//在消息定义中，每个字段都有唯一的一个标识符。
//这些标识符是用来在消息的二进制格式中识别各个字段的，一旦开始使用就不能够再改变。


// 请求参数定义
message HelloRequest {
  string name = 1;
}

// 响应结果定义
message HelloReply {
  string message = 1;
}



```

rpc关键字的语法为：rpc 方法名(请求参数类型) returns (返回类型) 