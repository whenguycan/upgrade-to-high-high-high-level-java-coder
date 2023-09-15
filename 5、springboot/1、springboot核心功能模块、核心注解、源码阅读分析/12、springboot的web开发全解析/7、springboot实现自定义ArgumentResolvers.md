## 实现自定义ArgumentResolvers



1. 写个注解：

   该注解没有实质意义，目的只是为了区分是我们自己的请求

   ```java
   
   @Target(ElementType.PARAMETER)
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   @Component
   public @interface CurrentUser {
   }
   ```

2. 写个实体类：

   ```java
   @Data
   public class UserVo {
   
       private Integer age;
   
       private String name;
   
       private Float score;
   }
   ```

3. 写一个controller

   ```java
   @PostMapping("/add-user")
   public String addUser(@CurrentUser UserVo userInfo){//这儿使用我们自己的注解
   
     System.out.println(userInfo);
   
     return "";
   }
   ```

4. 写一个自定义的ArgumentResolvers

   ```java
   public class CurrentUserMethodArgementResolve implements HandlerMethodArgumentResolver {
        @Override
         public boolean supportsParameter(MethodParameter methodParameter) {
             if(methodParameter.hasParameterAnnotation(CurrentUser.class)){
   
                 System.out.println("===========================》符合条件");
                 return true;
             }
             return false;
         }
         @Override
         public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
             System.out.println("------------------------------------------");
             UserVo target=new UserVo();
             target.setAge(91);
             target.setName("tangwei");
             target.setScore(100.00F);
             return target;
         }
     }
   ```

5. 往系统中添加我们自定义的ArgumentResolvers

   ```java
   @Component
   public class MyWebMvcConfigurer implements WebMvcConfigurer {
   
       @Override
       public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
           resolvers.add(new CurrentUserMethodArgementResolve());
       }
   }
   ```

   