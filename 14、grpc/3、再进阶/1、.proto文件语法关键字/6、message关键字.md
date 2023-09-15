## message关键字

meesage定义了在grpc中，服务与服务之间要传递的消息就是message。

在rpc关键中，我们定义一个rpc的语法是 `rpc 方法名(请求参数类型) returns (返回参数类型)`。这里需要被grpc传递的数据有`请求参数类型`、`返回参数类型`



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

使用protobuf对.proto文件生成java代码的时候，一个message会生成一个类，不过

如果我们对`option java_multiple_files`配置为false，则一个message会以内部类的形式归属于我们在`option java_outer_classname`这儿配置的类。

如果我们对`option java_multiple_files`配置为true，则一个message会生成一个java类文件。







