## 修改openfeign的请求头

当User服务调用Goods服务，Goods服务中对Http的请求头有限制，必须带一个标识，才能请求成功，那么我们就需要在User服务中修改openfeign的请求头。

> requestInterceptor是openFeign提供的拦截器，在openfeign请求发送之前进行拦截后处理请求的！

```java
@Configuration
public class MyFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        RequestInterceptor requestInterceptorTmp = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); //获取到HttpServletRequest
                requestTemplate.header("username", "tangwei"); //设置请求头
            }
        };

        return requestInterceptorTmp;
    }
}
```



在Goods服务中编写拦截器获取到头信息

- 编写拦截器

  ```java
  @Component
  public class LoginInterceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { //执行标的方法之前执行
  
          System.out.println(request.getHeader("username"));
          return true;
      }
  
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception { //执行标的方法之后执行
  
      }
  }
  ```

- 注册拦截器

  ```java
  @Configuration
  public class WebInterceptorConfig implements WebMvcConfigurer {
  
      @Autowired
      public LoginInterceptor loginInterceptor; //上面的拦截器加载进来
  
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(loginInterceptor).addPathPatterns("/**");//.excludePathPatterns(new String[]{"/login", "/logout"});
      }
  }
  
  ```

  

