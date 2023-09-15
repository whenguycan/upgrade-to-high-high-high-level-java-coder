## 测试fadout交换机

#### 1、到RabbitMQ中新建一个fadout类型的交换机

名字叫demo



#### 2、到RabbitMQ中新建2个Queue

名字分别为demo、demo1



#### 3、将上面新建的Queue和交换机进行绑定

绑定需要的Routing Key随便写！



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
    rabbitTemplate.convertAndSend("demo", "", new Message("要发送的消息内容".getBytes(), messageProperties)); //只有消息用Message对象包裹才能实现持久化
  }
  ```

- 发现，上面的即使在发送消息的方法`convertAndSend`的Routing Key没有指定，指定交换机下所有的Queue中都会接到对应的消息

  
