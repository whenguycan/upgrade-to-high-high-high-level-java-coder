## springboot集成RabbitMQ测试使用延迟消息

延迟消息插件安装完成之后，我们需要手动写代码去配置交换机，否则无法配置Exchange的延迟时间！



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
```



#### 3、入口文件添加注解，@EnableRabbit



#### 4、代码配置好Exchange和Queue以及Routing Key

新建一个config类

```java
@Configuration
public class DelayedMessageConfig {

    private static final String DELAYED_MESSAGE_EXCHANGE = "delayed.message.exchange";

    private static final String DELAYED_MESSAGE_QUEUE = "delayed.message.queue";

    private static final String DELAYED_MESSAGE_ROUTINGKEY = "delayed.message.routingkey";

    //声明一个交换机，因为我要使用的是延迟交换机，而内置交换机的类型并不支持所以只能使用自定义的交换机类型CustomExchange了。
    //内置交换机类型 DirectExchange、FanoutExchange、TopicExchange
    @Bean
    public CustomExchange delayedExchange(){

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");//声明延迟类型，即延迟队列中的数据到时间了，根据什么样的Routing Key规则投递到Queue中。

        //自定义交换机的参数
        //1. 交换机的名字
        //2. 交换机的类型
        //3. 交换机数据是否需要持久化
        //4. 交换机是否需要自动删除
        //5. 其它配置参数
        return new CustomExchange(DELAYED_MESSAGE_EXCHANGE, "x-delayed-message", true, false, arguments);
    }

    //声明一个Queue
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_MESSAGE_QUEUE);
    }

    //将Exchange与Queue进行绑定
    @Bean
    public Binding delayedBinding(
            @Qualifier("delayedQueue") Queue queue,
            @Qualifier("delayedExchange") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with(DELAYED_MESSAGE_ROUTINGKEY).noargs();
    }

}
```



#### 5、发送消息到指定的延迟Exchange

在消息中指定延迟的时间

```java
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/index")
public void index(){
  MessageProperties messageProperties = new MessageProperties();
  messageProperties.setDelay(10000); //设置消息延迟时间，单位是毫秒
  messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置消息持久化存储到磁盘上
  rabbitTemplate.convertAndSend("delayed.message.exchange",
                                "delayed.message.routingkey",
                                new Message("要发送的消息内容".getBytes(), messageProperties)
                               ); //只有消息用Message对象包裹才能实现持久化
}
```



#### 6、编写消费消息的代码去消费延迟队列中的数据

```java
@Component
@RabbitListener(queues = {"delayed.message.queue"}) //监听的队列
public class QueueListener {

    @RabbitHandler
    public void getMessage(byte[] message){
        System.out.println(new String(message)); //打印消息

    }
}
```



#### 7、编写测试数据

将2个消息延迟的时间区别开，看看那条被先消费。自己玩

