## springboot的web开发的自动装配原理



1. 引入springboot的web依赖

   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   ```

2. 在`spring-boot-starter-web`中会引入如下依赖

   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter</artifactId>
     <version>3.1.1</version>
     <scope>compile</scope>
   </dependency>
   ```

3. 在`spring-boot-starter`中会引入如下依赖

   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-autoconfigure</artifactId>
     <version>3.1.1</version>
     <scope>compile</scope>
   </dependency>
   ```

4. 找到`spring-boot-autoconfigure`包下的`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`文件，搜索`web`关键字，跟web开发相关的入口配置都在这儿了！

   ```java
   org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
   org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
     
   ===================以下是响应式编程的reactive相关的东西，我们不管=====================
   org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.ReactiveMultipartAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.WebSessionIdResolverAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration
   org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
   ===================以上是响应式编程的reactive相关的东西，我们不管=====================
     
   org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
   org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
   org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
   org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
   org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
   org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
   ```

5. 每一个web开发相关的入口配置文件都会有`@EnableConfigurationProperties`绑定一个相关的配置类。

