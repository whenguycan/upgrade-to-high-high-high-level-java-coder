## springboot项目集成RabbitMQ测试使用注解方式的消费者关闭自动应答的情况

> 在springboot的最新的spring-boot-starter-amqp依赖包中已经默认关闭了自动应答，且关闭了批量ack，消费一条会默认帮我们回复ack去应答一条。



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
    listener:
      type: simple  # 监听器的容器类型，这个是Rabbitmq的底层的东西  我们可以不用了解
      simple:
        acknowledge-mode: manual # 这儿就是配置关闭自动应答
```

<font color="red"> 这儿手动配置了手动应答，不采用默认的方式，那么就需要在消费者代码中手动回复ack消息给服务端</font>



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
@Component
@RabbitListener(queues = {"demo2"})
public class QueueListener {

    @RabbitHandler
    public void getMessage(Message message, byte[] msg, Channel channel) throws InterruptedException, IOException {
        Thread.sleep(20000);
        System.out.println(new String(msg));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);//false为关闭批量应答

    }
}
```

#### 9、查看是否有问题

