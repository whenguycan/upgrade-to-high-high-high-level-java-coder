## service关键字

定义一个gRPC服务



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

service后面跟着的servicename（上面写的是HelloService），这个servicename是protobuf根据.proto生成的java代码的类名前缀！