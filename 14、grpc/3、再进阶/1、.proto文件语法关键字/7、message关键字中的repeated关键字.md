## repeated关键字

```protobuf
message HelloReply {
  repeated News nnn = 1;
}

message News {
	
	......

}
```

针对上面的例子：

如果不写repeated即`News nnn = 1`，到java那么就是 News nnn的数据。

而如果带了repeated即`repeated News nnn`到java就是List<News> nnn的数据，同时会给该数据一个对应的方法getNnnList()