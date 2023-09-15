## 测试topic交换机

#### 1、到RabbitMQ中新建一个topic类型的交换机

名字叫demo-topic



#### 2、到到RabbitMQ中新建2个Queue

名字分别为 demo4、demo5、demo6、demo7



#### 3、将上面新建的Queue和交换机进行绑定

绑定需要的Routing Key分别为：cz.#，cz.*， *.cz， #.cz



#### 4、Producter发送消息到Exchange进行测试

- 引入依赖

  ```xml
  <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-amqp</artifactId>
      <version>2.6.6</version> <!-- 具体的版本由你的springboot版本决定 -->
  </dependency>
  ```

- 新增配置

  ```yaml
  spring:
    rabbitmq:
       host: 192.168.6.108
       port: 5672
       virtual-host: /
       username: tangwei
       password: 123456
  ```

- 入口文件添加注解，@EnableRabbit

- 消息发送

  ```java
  import org.springframework.amqp.core.Message;
  import org.springframework.amqp.core.MessageDeliveryMode;
  import org.springframework.amqp.core.MessageProperties;
  import org.springframework.amqp.rabbit.core.RabbitTemplate;
  
  
  @Autowired
  RabbitTemplate rabbitTemplate;
  
  @GetMapping("/index")
  public void index(){
    MessageProperties messageProperties = new MessageProperties();//设置消息持久化存储到磁盘上
    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
    rabbitTemplate.convertAndSend("demo-direct", "demo3", new Message("要发送的消息内容".getBytes(), messageProperties)); //只有消息用Message对象包裹才能实现持久化
  }
  ```

- 发现，上面在发送消息的方法`convertAndSend`的Routing Key为`cz.com` 的时候demo4、5中会有消息。Routing Key为`cz.com.cn` 的时候demo5中会有消息。

  
