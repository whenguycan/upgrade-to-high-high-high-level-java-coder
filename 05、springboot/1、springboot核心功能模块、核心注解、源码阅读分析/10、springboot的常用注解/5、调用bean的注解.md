## 调用bean的注解



1. @Autowired注解

   在自动注入的时候，会先根据类型即MyService去Bean的map中寻找对应的bean，如果找到多个再根据变量名即myService去Bean的map中寻找对应的bean，来确定使用哪个。也可以使用@Qualifier("指定bean的名称") 或 在MyService类注册为bean的时候加个@Primary注解，来确定使用哪个bean。

   ```java
   @Autowired 
   Private MyService myService;
   ```

   ```java
   @Qualifier("指定bean的名称") //一旦使用@Qualifier注解指定了@Autowired要使用的bean的名称，就不会安装@Autowired注解的规则去获取bean了
   @Autowired 
   Private MyService myService;
   ```

   

2. @Resource注解

   在自动注入的时候，会先根据变量名即myService去Bean的map中寻找对应的bean，如果找不到再根据类型即MyService去Bean的map中寻找对应的bean。

   ```java
   @Resource
   private MyService myService;
   ```

   ```java
   @Resource(name="myService2")//这样就会找bean的名称为myService2的bean了
   private MyService myService;
   ```

   

3. @Inject

   几乎不用！自行百度！

   

   

   

   

   