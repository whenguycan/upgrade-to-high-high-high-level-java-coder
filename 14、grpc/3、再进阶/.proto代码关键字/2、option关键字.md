## option关键字

配置从.proto文件，生成java代码的一些配置信息



使用范例

```protobuf
// 根据.proto文件编译，是否生成多个类？不生成多个类的话，在一个类中会有很多个子类。推荐不生成多个类，方便管理
option java_multiple_files = true;
// 生成java代码的包名
option java_package = "com.czdx.grpc.lib.demo";
// 是用一个class文件来定义所有的message对应的java类
option java_outer_classname = "HelloProto";
```

