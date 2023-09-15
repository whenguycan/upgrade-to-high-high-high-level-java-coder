## springboot请求静态资源源码分析

所谓静态资源就是存放在项目中的js、css、图片、静态html页面等。



#### 一、静态css、js图片的访问

1. 静态资源的目录

   ```tex
   只要放在类路径下： classpath:/META-INF/resources/、classpath:/resources/、 classpath:/static/、classpath:/public/ 
   访问当前项目路径/ + 静态资源名，只需要写静态资源名就能访问
   ```

2. 原理：/**
   收到请求后先去匹配 controller，如果不能处理就去交给静态资源处理器。

3. 通过配置文件可以修改静态资源的位置、静态资源的访问路径前缀

   ```yaml
   spring:
     web:
       resources:
         static-locations:  classpath:/path  #设置静态资源的存放地址
   ```

   ```yaml
   spring:
     mvc:
       static-path-pattern: /static/** #设置静态资源的访问前缀
   ```



#### 二、静态html页面的支持

在静态资源目录中 classpath:/META-INF/resources/、classpath:/resources/、 classpath:/static/、classpath:/public/ 可以存放静态html页面，可以直接使用 路径+html文件名的方式直接访问，注意：如果是访问html页面，是不能配置静态资源访问前缀的！



#### 三、favicon支持

将 favicon.ico 放置在静态资源路径下，会自动将favicon.ico 作为页面小图标



#### 四、静态资源配置源码分析

我们已经知道了WebMvcAutoConfiguration已经被自动装配进行了加载。

WebMvcAutoConfiguration类代码如下：

```java
@AutoConfiguration(after = { DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@ImportRuntimeHints(WebResourcesRuntimeHints.class)
public class WebMvcAutoConfiguration {

	......
  ......
  ......
	// Defined as a nested config to ensure WebMvcConfigurer is not read when not
	// on the classpath
	@Configuration(proxyBeanMethods = false)
	@Import(EnableWebMvcConfiguration.class)
	@EnableConfigurationProperties({ WebMvcProperties.class, WebProperties.class })
	@Order(0)
	public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {
    ......
    ......
    ......
    
  }
  
  @Configuration(proxyBeanMethods = false)
	@EnableConfigurationProperties(WebProperties.class)
	public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration implements 				ResourceLoaderAware {
  
    ......
    ......
    ......
    
  }
 ......
 ......
 ......
}
```

- WebMvcAutoConfigurationAdapter类

  这个类实现了`WebMvcConfigurer`接口，且没有配置@EnableWebMvc注解，<font color="red">是典型的手自一体使用web开发组件的方式。这儿实现了WebMvcConfigurer接口的目的是给系统中添加一些自定义的功能！</font>

  1. `WebMvcAutoConfigurationAdapter`这个静态内部类上，使用了`@EnableConfigurationProperties`注解，表示启用了`WebMvcProperties`、`WebProperties`这两个配置文件！

  2. 详细分析`addResourceHandlers`处理静态资源的方法，代码如下：

     ```java
     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
       if (!this.resourceProperties.isAddMappings()) {
         logger.debug("Default resource handling disabled");
         return;
       }
       addResourceHandler(registry, this.mvcProperties.getWebjarsPathPattern(),
                          "classpath:/META-INF/resources/webjars/");
       addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
         registration.addResourceLocations(this.resourceProperties.getStaticLocations());
         if (this.servletContext != null) {
           ServletContextResource resource = new ServletContextResource(this.servletContext, SERVLET_LOCATION);
           registration.addResourceLocations(resource);
         }
       });
     }
     ```

     - 使用`addResourceHandler`，添加一个访问规则，访问/**就会到 classpath:/META-INF/resources/、classpath:/resources/、 classpath:/static/、classpath:/public/ 这四个地址去找对应的资源。

     

- EnableWebMvcConfiguration类

  该类中有一个 WelcomePageHandlerMapping方法，在访问这个WelcomePageHandlerMapping之前，其实SpringBoot 会先去访问 RequestMappingHandlerMapping，即程序员自己创建的 controller 的HandlerMapping，这也说明了我们编写的Controller的优先级是最高的。

  ```java
  @Bean
  public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext,
                                                             FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
    return createWelcomePageHandlerMapping(applicationContext, mvcConversionService, mvcResourceUrlProvider,
                                           WelcomePageHandlerMapping::new);
  }
  
  
  ```

  其中的createWelcomePageHandlerMapping方法会调用如下代码

  ```java
  private <T extends AbstractUrlHandlerMapping> T createWelcomePageHandlerMapping(
    ApplicationContext applicationContext, FormattingConversionService mvcConversionService,
    ResourceUrlProvider mvcResourceUrlProvider, WelcomePageHandlerMappingFactory<T> factory) {
    TemplateAvailabilityProviders templateAvailabilityProviders = new TemplateAvailabilityProviders(
      applicationContext);
    String staticPathPattern = this.mvcProperties.getStaticPathPattern(); //这儿会得到访问的地址为 /**
    
    T handlerMapping = factory.create(templateAvailabilityProviders, applicationContext, getIndexHtmlResource(), staticPathPattern); //getIndexHtmlResource()最终会得到 classpath:/META-INF/resources/、classpath:/resources/、 classpath:/static/、classpath:/public/ 这4个路径
    
    handlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
    handlerMapping.setCorsConfigurations(getCorsConfigurations());
    return handlerMapping;
  }
  ```

  

  

