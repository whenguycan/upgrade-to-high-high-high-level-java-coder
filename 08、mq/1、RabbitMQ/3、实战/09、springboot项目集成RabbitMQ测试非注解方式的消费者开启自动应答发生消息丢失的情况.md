## springboot项目集成RabbitMQ测试非注解方式的消费者开启自动应答发生消息丢失的情况



#### 1、引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.6.6</version> <!-- 具体的版本由你的springboot版本决定 -->
</dependency>
```



#### 2、新增配置

```yaml
       
spring:
  application:
    name: demo-rabbitmq
  rabbitmq:
    host: 172.16.4.135
    port: 5672
    virtual-host: /
    username: guest
    password: tangwei
    publisher-confirm-type: correlated #这儿是新增的。publisher-confirm-type的默认值是none，禁用发布确认模式。改为correlated这个值，表示消息发送成功到交换器就会触发回调方法
    template:
      mandatory: true   #开启这个配置才能触发消息不能正常抵达Queue的回调
```



#### 3、到RabbitMQ中新建一个direct类型的交换机

名字叫demo-direct



#### 4、到到RabbitMQ中新建2个Queue

名字分别为demo2、demo3



#### 5、将上面新建的Queue和交换机进行绑定

绑定需要的Routing Key与Queue的名称一致



#### 6、入口文件添加注解，@EnableRabbit



#### 7、发送消息

```java
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/index")
public void index(){
  CorrelationData correlationData = new CorrelationData();
  correlationData.setId(UUID.randomUUID().toString());//这儿存放关于消息的扩展信息
  
  MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
  messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
  rabbitTemplate.convertAndSend("demo-direct", "demo3", 
                                new Message("要发送的消息内容".getBytes(), messageProperties),
                                correlationData //这儿将消息的扩展信息也发送出去
                               ); //只有消息用Message对象包裹才能实现持久化
}
```



#### 8、编写消费者代码

```java
package com.example.demorabbitmq.worker;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: tangwei
 * @Date: 2023/3/2 10:19 AM
 * @Description: 类描述信息
 */
public class FirstWorker {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.16.4.135");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("tangwei");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        DeliverCallback deliverCallback = (ConsumerTag, message)->{
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (ConsumerTag)->{
            System.out.println(ConsumerTag + "该消息被取消消费了！");
        };
        System.out.println("C1工作线程等待中......");
      
      	//第1个参数是指消费那个queue
        //第2个参数是指 是否开启自动应答，我们这儿就是测试的为true的情况下的问题！如果这个参数为false，则需要我们处理条就ack一条。
      	//deliverCallback 写成功处理消息的逻辑
      	//cancelCallback
        channel.basicConsume("demo2", true, deliverCallback, cancelCallback);
    }
}

```

<font color="red">我们这次的测试，就是测试channel.basicConsume的第二个参数为true的情况！</font> 

#### 9、查看是否有问题

