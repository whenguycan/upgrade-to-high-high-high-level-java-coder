## springboot3的@SpringBootApplication注解源码详解



进入@SpringBootApplication注解中，是如下代码

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

  ........

}
```

其中有主要是

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })

这三个注解起作用！



<font color="red">这三个注解的执行顺序是：@ComponentScan执行完了再执行@EnableAutoConfiguration再执行@SpringBootConfiguration。</font>



- @SpringBootConfiguration注解详解：

  进入到这个注解的源码中，发现并没有做任何事情，有个我们常用的注解@Configuration，我们查资料发现，@SpringBootConfiguration只是@Configuration注解的派生注解；它与@Configuration注解的功能一致；只不过@SpringBootConfiguration是springboot的注解，而@Configuration是spring的注解。



- @EnableAutoConfiguration注解详解：

  进入到咋合格注解的源码中，发现起作用的注解有两个

  - @AutoConfigurationPackage的作用：进入到这个注解的源码中，发现`@Import(AutoConfigurationPackages.Registrar.class)`这个注解，随后我们进入到`AutoConfigurationPackages.Registrar`这个类中，有如下的方法

    ```java
    @Override
    		public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
    			register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]));
    		}
    ```

    `new PackageImports(metadata).getPackageNames()`会得到项目启动类所在包的包路径，然后register方法会扫描启动类所在包及其子包下所有的@Component、@Bean、@Controller、@Service、@Repository等等修饰的类加入到ioc容器中。

  

  - @Import(AutoConfigurationImportSelector.class)的作用：<font color="red">参考，springboot3的自动装载源码解析，</font>就会发现，它作用就是让系统自动扫描所有依赖jar包中的META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件中所有的类，然后自动装载这些类和自动配置相关配置！

  

- @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
  		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })注解详解：

  @ComponentScan本身就是包扫描的功能，不过这儿只是用于排除掉一些不需要被扫描的类，进入到`AutoConfigurationExcludeFilter`类，找到match方法，发现是判断isAutoConfiguration && isConfiguration 即被@Configuration修饰且自动装载的类排除掉！

  



