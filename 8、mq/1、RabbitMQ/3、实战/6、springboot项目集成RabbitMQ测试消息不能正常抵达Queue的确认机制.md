## springboot项目集成RabbitMQ测试消息不能正常抵达Queue的确认机制



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



#### 8、编写ConfirmCallback代码

```java
package com.example.demorabbitmq.callback;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther: tangwei
 * @Description: 类描述信息
 */
@Component
public class MyRabbitMQConfirmCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    /**
     * 交换机不管是否收到消息都调用 回调方法
     * 1. 发消息 交换机接收到了 回调
     * @param correlationData  保存回调信息的Id及相关信息
     * @param ack              交换机收到消息 为true
     * @param cause            未收到消息的原因
     *
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData!=null?correlationData.getId():"";
        if(ack){
            System.out.println("交换机已经收到了ID为:{"+id+"}的消息");
        }else {
            System.out.println("交换机还未收到ID为:{"+id+"}的消息，由于原因:{"+cause+"}");
        }
    }

    //可以在当消息传递过程中不可达目的地时将消息返回给生产者
    //该方法只有不可达目的地的时候 才会被触发进行回退
    /**
     * 当消息无法路由的时候的回调方法
     *  message      消息
     *  replyCode    编码
     *  replyText    退回原因
     *  exchange     从哪个交换机退回
     *  routingKey   通过哪个路由 key 退回
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.println("消息{"+new String(returnedMessage.getMessage().getBody())+"},被交换机{"+returnedMessage.getExchange()+"}退回，退回原因:{"+returnedMessage.getReplyText()+"},路由key:{"+returnedMessage.getRoutingKey()+"}");

    }
}

```

