## nacos的server端的端口占用说明



8848端口：在1.x和2.x中都会占用

9848端口：在2.x中，server端开启的client与server的进行grpc通信的端口

9849端口，在2.x中，server端开启的gRPC请求服务端端口，用于服务间同步等