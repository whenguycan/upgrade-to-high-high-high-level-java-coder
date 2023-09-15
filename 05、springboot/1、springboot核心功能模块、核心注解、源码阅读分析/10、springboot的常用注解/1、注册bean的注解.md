## 注册bean的注解



1. @Configuration注解

   让springboot知道，被该注解标注的是一个配置类，该类中一般使用@Bean注解去注册bean。

   被@Configuration修饰的类，本身也是bean，也要被加入到ioc中。

   ```java
   @Configuration
   public class MyConfig(){
     
     @Bean
     public User user(){
       
       return new User();
     }
     
   }
   ```

   

2. @Bean注解

   只能在被@Configuration修饰的类中使用，表示注册一个对象到bean中

   ```java
   @Configuration
   public class MyConfig(){
     
     @Bean
     public User user(){ //注册到bean中的名称为方法的名称
       
       return new User();
     }
     
   }
   ```

   或者这样使用

   ```java
   @Configuration
   public class MyConfig(){
     
     @Bean("user01") //在注解中规定bean的名称，就不使用方法名做为bean的名称了
     public User user(){
       
       return new User();
     }
     
   }
   ```

   <font color="red">默认使用@Bean注册的bean都是单例的！</font>

   

   测试单例，在项目启动类中修改如下代码

   ```java
   @SpringBootApplication
   @ComponentScan("com.example")
   public class DemoLogApplication {
   
       public static void main(String[] args) {
           var ioc = SpringApplication.run(DemoLogApplication.class, args); //获取到项目启动后的ioc容器
   
           Object user1 = ioc.getBean("user");
           Object user2 = ioc.getBean("user");
   
           System.out.println(user1 == user2)
       }
   
   }
   ```

   

   

3. @Scope注解

   springboot中默认注册bean都是单例的，使用这个注解可以选择让bean成为单例或者非单例，@Bean、@Component等注册bean的注解一起使用

   ```java
   @Configuration
   public class MyConfig {
   
       @Scope("prototype") //注册bean为非单例！
       @Bean
       public User user(){
           return new User();
       }
   }
   ```

   测试，在项目启动类中修改如下代码

   ```java
   @SpringBootApplication
   @ComponentScan("com.example")
   public class DemoLogApplication {
   
       public static void main(String[] args) {
           var ioc = SpringApplication.run(DemoLogApplication.class, args); //获取到项目启动后的ioc容器
   
           Object user1 = ioc.getBean("user");
           Object user2 = ioc.getBean("user");
   
           System.out.println(user1 == user2)
       }
   
   }
   ```

   

4. @SpringBootConfiguration注解

   就是@Configuration注解，@SpringBootConfiguration只是@Configuration在springboot中的别名，是springboot自己又封了一层。

   ```java
   @Configuration
   public class MyConfig(){
     
     @Bean
     public User user(){
       
       return new User();
     }
     
   }
   ```

   

5. @Controller

   写在类上，把类注册当bean中，注册的bean的名称为类名首字母小写的模式

6. @Service

   写在类上，把类注册当bean中，注册的bean的名称为类名首字母小写的模式

7. @Respository

   写在类上，把类注册当bean中，注册的bean的名称为类名首字母小写的模式

8. @Component

   写在类上，把类注册当bean中，注册的bean的名称为类名首字母小写的模式

9. @Import

   注册的bean的名称为类的全路径

   - @Import内是一个普通类 spring会将该类加载到spring容器中

     ```java
     @SpringBootApplication
     @Import(User.class) //手动import这个User类
     public class DemoLogApplication {
     
         public static void main(String[] args) {
             var ioc = SpringApplication.run(DemoLogApplication.class, args);
     
             Object user1 = ioc.getBean("com.example.demolog.config.User"); //bean的名称为User类的全路径
             Object user2 = ioc.getBean("com.example.demolog.config.User");
     
             System.out.println(user1 == user2);
         }
     
     }
     ```

   - @Import内是一个ImportSelector的实现类， 重写selectImports方法该方法返回了String[]数组的对象，数组里面的类都会注入到spring容器当中

   - @Import内是一个ImportBeanDefinitionRegistrar接口的实现类，在重写的registerBeanDefinitions方法里面，能拿到BeanDefinitionRegistry bd的注册器，能手工往beanDefinitionMap中注册 beanDefinition

   

10. @ComponentScan

    指定bean除了启动类所在包及其子包、自动装配以外的bean扫描路径

    ```java
    @SpringBootApplication
    @ComponentScan("com.example") //指定bean的额外扫描路径
    public class DemoLogApplication {
    
        public static void main(String[] args) {
            var ioc = SpringApplication.run(DemoLogApplication.class, args);
    
            Object user1 = ioc.getBean("user");
            Object user2 = ioc.getBean("user");
    
            System.out.println(user1 == user2);
        }
    
    }
    
    ```

    