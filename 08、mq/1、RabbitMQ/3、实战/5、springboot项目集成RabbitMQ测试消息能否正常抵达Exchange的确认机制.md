## springboot项目集成RabbitMQ测试消息能否正常抵达Exchange的确认机制



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
  rabbitmq:
     host: 192.168.6.108
     port: 5672
     virtual-host: /
     username: tangwei
     password: 123456
     publisher-confirm-type: correlated #这儿是新增的。publisher-confirm-type的默认值是none，禁用发布确认模式。改为correlated这个值，表示消息发送成功到交换器就会触发回调方法
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

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 5:31 PM
 * @Description: 类描述信息
 */
@Component
public class MyRabbitMQConfirmCallback implements RabbitTemplate.ConfirmCallback{


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        //注入
        rabbitTemplate.setConfirmCallback(this);
    }
    /**
     * 交换机不管是否收到消息都调用 回调方法
     * @param correlationData  在发送时设置的消息的扩展信息
     * @param ack              交换机是否收到消息
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
}

```

