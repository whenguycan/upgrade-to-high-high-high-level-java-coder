## springboot2自动装配原理



> <font color="red">当项目启动时，Springboot会扫描当前项目以及所有依赖jar包下面的resources/META-INF/spring.factories 文件</font>

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

  但是什么时候会调用到`getAutoConfigurationEntry`方法呢？我们可以在该方法的第一行打个断点，然后看堆栈就能看出什么时候调用的该方法

  

- 其中getCandidateConfigurations方法

  ```java
  protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
  		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
  				getBeanClassLoader());
  		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
  				+ "are using a custom packaging, make sure that file is correct.");
  		return configurations;
  	}
  ```

  这个loadFactoryNames方法会去加载根据spring-boot-autoconfigure包下的resources下的META-INF/spring.factories中配置好了的自动配置，spring.factories需要配置如下内容：

  ```properties
  org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
  org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
  org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
  org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
  org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
  ......
  ```

- 而这些路径下的文件（上面的绿色部分），就是我们熟悉的被@Configuration修饰的类，被@Bean修饰的方法，这些都会被自动装配到springboot的IOC容器中。

