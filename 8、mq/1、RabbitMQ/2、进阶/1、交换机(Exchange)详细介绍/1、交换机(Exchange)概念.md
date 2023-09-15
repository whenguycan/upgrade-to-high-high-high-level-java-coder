## Exchange概念

客户端生产者将消息发送给RabbitMQ，消息随后会被转到指定的交换机Exchange中，在Exchange中会首先根据Exchange的<font color="red">类型</font>结合Binding的Queue的Routing key，将消息转发到具体的Queue。