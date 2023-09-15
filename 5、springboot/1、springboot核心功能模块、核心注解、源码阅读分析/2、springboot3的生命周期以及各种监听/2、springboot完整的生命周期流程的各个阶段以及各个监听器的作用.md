## springboot完整的生命周期流程的各个阶段



![avatar](../../images/WechatIMG629.png)





- 1. BootstrapRegistryInitializer监听器，定义在META-INF/spring.factories中，在引导初始化应用的时候触发执行。该监听器在项目引导阶段的最前面被执行，为引导阶段提供数据准备的！

     

- 2. ApplicationContextInitializer监听器，定义在META-INF/spring.factories中，在ioc容器初始化的时候触发执行。

     

- 3. ApplicationListener监听器，定义在META-INF/spring.factories中、使用Bean注解、使用@EventListener注解都可以，感知系统中的event执行对应的方法的。跟SpringApplicationRunListener一样，在生命周期的各个阶段都可以触发执行，但是功能不如SpringApplicationRunListener强大。

     

- 4. SpringApplicationRunListener监听器，定义在META-INF/spring.factories中，在生命周期的各个阶段都可以触发执行，比ApplicationListener强大，因为它在各个阶段的方法中可以获取到数据，然后可以做一些自定义的操作！<font color="red">**上图中所有的Event都是由EventPublishingRunListener发出的，除了ContextRefreshedEvent。**</font>

  

- 5. ApplicationRunner、CommandLineRunner，使用bean注解，注册到应用中， 当应用变为ready后第一时间执行，如果在runner中阻塞，应用就不会变为就绪状态。

