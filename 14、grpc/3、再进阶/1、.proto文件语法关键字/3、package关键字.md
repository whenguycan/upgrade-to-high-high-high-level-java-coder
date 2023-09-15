## package关键字

给.proto文件中的service、message分配一个逻辑包名，不会真正创建一个目录去存放文件。只是为了隔离service、message。

当另一个.proto文件中需要引入当前.proto中的message，需要带上当前.proto中定义的package，即`当前.proto中定义的package.message名称`



```protobuf
........

package com.czdx.grpc.lib.demo;

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

注意：如果同一个package中，有相同的2个message或者service，则会报错！