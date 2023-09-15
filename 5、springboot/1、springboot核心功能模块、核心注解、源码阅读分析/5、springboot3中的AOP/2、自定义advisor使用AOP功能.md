## 自定义Advisor取代使用@Aspect注解



#### 概念

什么是advisor、advice、pointcut？

- pointcut

  定义切入点，比如是在哪个类的哪个方法上进行切入。

  > 在精确到方法上切入的时候，可以获取到方法的名称，<font color="red">还可以获取到方法上是否使用了注解，使用了什么注解</font>

- advice

  由切入点，我们知道了需要在哪个类的哪个方法上进行切入，那么advice就可以确定是在方法前、后、环绕的方式进行切入。

- advisor

  是整合pointcut和advice的整合器，内部有getAdvice()、getPointcut()方法，返回对应我们自己写的pointcut、advice即可。



#### 实际开发操作

- 编写一个注解

  ```java
  @Target(ElementType.METHOD)
  //使用该注解设置为RetentionPolicy.RUNTIME
  //才能在pointcut中通过反射访问到
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface MyAnnotation {
  }
  ```

- 编写一个ponitcut

  ```java
  public class MyPointcut implements Pointcut {
      @Override
      public ClassFilter getClassFilter() {
        	//这儿表示所有的类都进行放行，如果对类有过滤要求，可以在这儿进行过滤
          return ClassFilter.TRUE;
      }
  
      @Override
      public MethodMatcher getMethodMatcher() {
          return new MethodMatcher() {
            	//主要用这个方法
              @Override
              public boolean matches(Method method, Class<?> targetClass) {
                	//注意自定义注解必须使用内置注解@Retention(RetentionPolicy.RUNTIME)，否则这儿获取不到
                  MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
                	//判断方法是否使用了MyAnnotation注解。
                  if(myAnnotation != null){
                      return true;
                  }
                  return false;
              }
  
              @Override
              public boolean isRuntime() {
                  return false;
              }
  
              @Override
              public boolean matches(Method method, Class<?> targetClass, Object... args) {
                  return false;
              }
          };
      }
  }
  ```

- 编写一个advice

  ```java
  
  //这个是前置通知
  public class MyAdvice implements MethodBeforeAdvice {
  
      @Override
      public void before(Method method, Object[] args, Object target) throws Throwable {
          System.out.println("前置通知");
      }
  }
  
  //这个是后置通知
  public class MyAdvice implements AfterReturningAdvice {
      /*
       * 参数：
       * method-当前执行的方法
       * objects-当前执行方法的参数数组（这里可以对参数进行修改）
       * o-代理方法的返回值对象
       * o1-代理的目标对象，注意是目标对象，不是代理对象
       *
       * */
      @Override
      public void afterReturning(Object o, Method method, 
                                  Object[] objects, Object o1) throws Throwable {
          System.out.println("后置通知"+o1);
  
      }
  }
  
  
  //这个是环绕通知
  public class MyAdvice implements MethodInterceptor {
      @Override
      public Object invoke(MethodInvocation methodInvocation) throws Throwable {
          System.out.println("前");
          //执行目标方法,proceed为目标方法的返回值
          
          Object proceed = methodInvocation.proceed();
          System.out.println(proceed);
  
          System.out.println("后");
          return proceed;
      }
  }
  
  
  ```

- 编写advisor

  ```java
  @Component
  public class MyAdvisor extends AbstractPointcutAdvisor {
  
  
      @Override
      public Advice getAdvice() {
          return new MyAdvice();
      }
  
      @Override
      public boolean isPerInstance() {
          return false;
      }
  
      @Override
      public Pointcut getPointcut() {
          return new MyPointcut();
      }
  }
  ```

- 编写一个config<font color="red">或</font>引入spring-boot-starter-aop依赖

  这一步非常重要，如果不添加该bean，那么spring扫描到Advisor后也不会创建代理对象替换掉原来的bean。

  ```java
  @Configuration
  public class MyConfig {
      @Bean
      @ConditionalOnMissingBean
      public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
          DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
          defaultAAP.setProxyTargetClass(true);
          return defaultAAP;
      }
  }
  ```

- 编写一个controller进行测试

  ```java
  @GetMapping("/isLogin")
  @MyAnnotation
  public String isLogin(){
    System.out.println("已经登录");
    return "isLogin";
  }
  
  ```

  



