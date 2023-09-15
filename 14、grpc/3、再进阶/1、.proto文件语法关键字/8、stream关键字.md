## stream关键字



表示一个流式的请求或流式响应，什么叫流式响应（就是会有多次响应）什么叫流式请求（就是会有多次请求）



```java
......
  
  service HelloService {
  // RPC方法定义
 	 rpc SayHello(stream HelloRequest) returns (stream HelloReply) {}
	}
  
......
```

