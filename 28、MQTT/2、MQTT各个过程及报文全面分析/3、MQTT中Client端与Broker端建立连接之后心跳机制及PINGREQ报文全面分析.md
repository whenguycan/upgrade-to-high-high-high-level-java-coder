## MQTT中Client端与Broker端建立连接之后心跳机制及PINGREQ报文全面分析

> 心跳机制是MQTT内部提供的，所以心跳包不需要我们手动发送，只要开启了心跳保活机制，MQTT内部就会自动发送！

在CONNECT报文的可变报头中，有个Keep Alive，2个字节长度的无符号整形，用于表示客户端发送完一次消息后，到下一次发送消息，这中间最大的时间间隔。如果2次发送数据超过了时间间隔，客户端必须发送一个PINGREQ报文。服务端在收到PINGREQ报文后都会返回一个PINGRESP报文。Broker会在设置的Keep Alive的时间的1.5倍的时候后，还没有收到任何消息，就判断为连接断开！如果Keep Alive设置为0，那么保活机制就关闭了！

![avatar](../images/WechatIMG730.png)



#### PINGREQ报文

![avatar](../images/WechatIMG717.png)

心跳报文中只有固定报头，没有可变报头和有效载荷

