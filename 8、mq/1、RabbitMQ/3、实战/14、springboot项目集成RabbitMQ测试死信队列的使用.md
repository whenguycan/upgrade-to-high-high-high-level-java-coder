## springboot项目集成RabbitMQ测试死信队列



#### 1、大致架构如下：

![avatar](/Users/tangwei/Desktop/课件/7、mq/1、RabbitMQ/images/WechatIMG501.jpeg)



#### 2、引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.6.6</version> <!-- 具体的版本由你的springboot版本决定 -->
</dependency>
```



#### 3、添加配置

因为springboot依赖RabbitMQ已经默认关闭了自动应答，我们配置成手动应答。

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
    listener:
      type: simple  # 监听器的容器类型，这个是Rabbitmq的底层的东西  我们可以不用了解
      simple:
        acknowledge-mode: manual # 这儿就是配置关闭自动应答
```





#### 4、代码编写创建如上图的2个交换机和队列

在配置普通队列的时候，需要指定，产生死信需要将死信转发到哪个死信队列，且需要配置转发死信的Routing Key

```java
package com.example.demorabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: tangwei
 * @Date: 2023/3/3 10:11 AM
 * @Description: 类描述信息
 */
@Configuration
public class DeadQueueConfig {

    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    //普通队列名称
    private static final String NORMAL_QUEUE = "normal_queue";

    //普通Routing Key
    private static final String NOMAL_ROUTING_KEY = "zhangsan";

    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    //死信队列名称
    private static final String DEAD_QUEUE = "dead_queue";

    //死信Routing Key
    private static final String DEAD_ROUTING_KEY = "lisi";

    //声明普通交换机
    @Bean
    public DirectExchange normalExchange(){
        return new DirectExchange(NORMAL_EXCHANGE, true, false);
    }

    //声明普通Queue
    @Bean
    public Queue normalQueue(){
        HashMap<String, Object> arguments = new HashMap<>();
        //设置消息过期时间，也可以不在这儿设置，在发送消息的时候设置消息的过期时间
        //arguments.put("x-message-ttl", 10000);

        //配置普通队列 产生死信 需要转发消息到死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);

        //配置普通队列 产生死信 转发到死信队列用什么Routing Key
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
      
      	//配置普通队列中消息的最大个数，超过这个值的消息会被直接放入死信队列
      	arguments.put("x-max-length", 6);

        return new Queue(NORMAL_QUEUE, true, false, false, arguments);
    }

    //将普通Exchange与普通Queue进行绑定
    @Bean
    public Binding normalBinding(
            @Qualifier("normalQueue") Queue queue,
            @Qualifier("normalExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(NOMAL_ROUTING_KEY);
    }

    //声明死信交换机
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE, true, false);
    }

    //声明死信Queue
    @Bean
    public Queue deadQueue(){
        return new Queue(DEAD_QUEUE);
    }

    //将死信Exchange与死信Queue进行绑定
    @Bean
    public Binding deadBinding(
            @Qualifier("deadQueue") Queue queue,
            @Qualifier("deadExchange") DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with(DEAD_ROUTING_KEY);
    }
}

```

#### 5、编写普通队列的消费者

```java
@Component
@RabbitListener(queues = {"normal_queue"})
public class QueueListener {

    @RabbitHandler
    public void getMessage(Message message, byte[] msg, Channel channel) throws InterruptedException, IOException {
        System.out.println(new String(msg));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
```



#### 6、编写普通队列的生产者代码

```java
@RestController
public class IndexController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/index")
    public void index(){
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());

        MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
        //配置消息过期时间，单位是毫秒
        messageProperties.setExpiration("10000");
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        rabbitTemplate.convertAndSend("normal_exchange",
                "zhangsan",
                new Message("要发送的消息内容".getBytes(), messageProperties),
                correlationData
        ); //只有消息用Message对象包裹才能实现持久化
    }
}
```

至此我们可以开始测试，

- 消息过期消息自动进入死信队列：调用生产者代码，往普通队列中写数据，写入的数据过期时间为10秒，此时注释掉消费者代码，等消息过期观察是否进入死信队列。

- 消息被消费者手动拒绝，即消费者消费消息调用.basicNack()或.basicReject() 消息自动进入死信队列。

  - 调用.basicNack() 的注意点：

    ```java
    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false, 是否重新放回队列true/fasle);
    ```

    第三个参数一定要配置为false，意为不重新放回队列，才会进入死信队列，否则无限重放出现死循环！

- 消息超过了普通队列的最大个数，消息直接进入死信队列。

