## option关键字

根据.proto文件变成生成java代码的可选配置



```protobuf
// 是否对一个.proto文件编译生成多个java文件，推荐使用false，一个.proto只生成一个文件，所有的类都在该类的内部类。
option java_multiple_files = true;
// 根据.proto文件编译得到的java代码的包名和java代码存放路径，最终打成jar包就会存放在该路径下。
option java_package = "com.czdx.grpc.lib.demo";
// 根据.proto文件编译得到的java大类名，如果java_multiple_files配置为false，那么只会生成这一个文件。如果配置的为true，除了生成这个文件外，还会生成其它.proto中的service、message文件。
option java_outer_classname = "HelloProto";
```

