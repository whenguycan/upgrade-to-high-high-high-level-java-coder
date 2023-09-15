## springboot3自动装配原理



> <font color="red">当项目启动时，Springboot会扫描当前项目以及所有依赖jar包下面的resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 文件</font>

- 在springboot的项目启动类上有注解

  ```java
  @SpringBootApplication
  ```

- 点开@SpringBootApplication，你会发现有@EnableAutoConfiguration注解，这个注解位于spring-boot-autoconfigure包中。

  ```java
  @EnableAutoConfiguration
  ```

- 点开@EnableAutoConfiguration会有@Import注解

  ```java
  @Import(AutoConfigurationImportSelector.class)
  ```

- @Import注解中加载一个Selector类，实现了DeferredImportSelector接口，点开AutoConfigurationImportSelector类有getAutoConfigurationEntry方法

  ```java
  protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
  		if (!isEnabled(annotationMetadata)) {
  			return EMPTY_ENTRY;
  		}
  		AnnotationAttributes attributes = getAttributes(annotationMetadata);
  		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
  		configurations = removeDuplicates(configurations);
  		Set<String> exclusions = getExclusions(annotationMetadata, attributes);
  		checkExcludedClasses(configurations, exclusions);
  		configurations.removeAll(exclusions);
  		configurations = getConfigurationClassFilter().filter(configurations);
  		fireAutoConfigurationImportEvents(configurations, exclusions);
  		return new AutoConfigurationEntry(configurations, exclusions);
  }
  ```

  但是什么时候会调用到`getAutoConfigurationEntry`方法呢？我们可以在该方法的第一行打个断点，然后看堆栈就能看出什么时候调用的该方法。是在ioc容器refresh的invokeBeanFactoryPostProcessors的时候执行的！

  

- 然后再其中`getAutoConfigurationEntry`方法中调用了getCandidateConfigurations方法

  ```java
  protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
  		 List<String> configurations = ImportCandidates.load(AutoConfiguration.class, this.getBeanClassLoader()).getCandidates();
        Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports. If you are using a custom packaging, make sure that file is correct.");
        return configurations;
  	}
  ```

  这个load方法会去加载根据spring-boot-autoconfigure包下的resources下的META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports中配置好了的自动配置，spring.factories需要配置如下内容：

  ```properties
  org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration
  org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration
  org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration
  ......
  ```

- 而这些路径下的文件中每一行都是一个配置文件就是我们熟悉的被@Configuration修饰的类，被@Bean修饰的方法，这些都会被自动装配到springboot的IOC容器中。

