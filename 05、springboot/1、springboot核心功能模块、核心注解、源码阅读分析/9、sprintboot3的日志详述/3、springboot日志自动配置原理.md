## springboot日志自动配置原理



日志的自动装配

在我们引入`spring-boot-starter-web`后，会自动引入`spring-boot-starter`，又会自动引入`spring-boot-autoconfigure`依赖。我们到autoconfigure的`org.springframework.boot.autoconfigure`项目路径中找到logging目录，目录中都是一些Linister、Processor等文件。到此，我们发现，日志使用的是springboot的监听器机制进行配置的！而不是自动装配！

<font color="red">**为什么不使用自动装配，不在自动装配的`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`文件中配置?**</font> 因为，使用`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.import`进行装配是项目启动完毕后才进行自动装配的，但是框架的日志功能，要在项目启动的最前面就需要配置好，所以不能用自动装配！