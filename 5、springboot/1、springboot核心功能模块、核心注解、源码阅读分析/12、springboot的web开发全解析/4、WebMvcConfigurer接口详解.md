## WebMvcConfigurer接口详解

WebMvcConfigurer接口提供了配置springmvc底层所有组件的入口。

WebMvcConfigurer接口的目的是，给系统中添加一些自定义的web配置，该接口中有很多方法，各个方法的作用如下：

1. addArgumentResolvers，给系统添加参数解析器，什么叫参数解析器？

   ```java
   @GetMapping("/index")
   public String index(String a, String b){
   
     ServiceLoader<BaseLog> load = ServiceLoader.load(BaseLog.class);
     load.forEach((item)->{
       System.out.println(item.getClass().getName());
     });
   
   
     return "";
   
   }
   ```

   controller中所有需要从前端接收的参数，都需要使用参数解析器进行解析！



2. addCorsMappings，给系统添加跨域规则

3. addFormatters，给系统添加格式化器，什么叫格式化器？

   我们在配置文件中配置了一个日期，这个日期是我们自定义的格式，系统默认是不识别的，那么我们就需要配置一个格式化器来格式化这个日期让系统能够识别！

4. addInterceptors，给系统添加拦截器

5. addResourceHandlers，给系统添加资源处理器：处理静态资源，比如 图片、css、js等

6. addReturnValueHandlers，给系统添加返回值处理器。在controller中我们一个GET的请求，我们方法上只写了返回一个Object（任意类型的数据），我们只需要返回对应的数据，然后前端拿到数据就是json格式的了！

7. configureAsyncSupport，给系统配置异步支持

8. configureContentNegotiation，给系统配置内容协商

9. configureDefaultServletHandling，给系统配置默认处理路径，这个一般我们不改，系统中的默认处理路径是"/"

10. configureHandlerExceptionResolvers，给系统配置异常解析器，统一处理异常。

11. configureMessageConverters，给系统配置消息转换器

12. configurePathMatch，给系统配置路径匹配，在controller，我们每一个方法都会对应一个路由的路径，只有路径跟我们配置的路径匹配规则相匹配才会执行对应的方法。

13. extendHandlerExceptionResolvers，给系统扩展异常解析器

14. extendMessageConverters，给系统扩展消息转换器