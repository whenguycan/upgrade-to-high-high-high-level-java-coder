## 监听器被springboot加载的源码分析



在项目入口文件XxxApplication中的main方法代码如下：

```java
public static void main(String[] args) {
  SpringApplication.run(DemoLogApplication.class, args);
}
```

随后会进入如下的方法中

```java
public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
  return new SpringApplication(primarySources).run(args);
}
```

`new SpringApplication`会进入如下的方法

```java
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		this.webApplicationType = WebApplicationType.deduceFromClasspath();
		this.bootstrapRegistryInitializers = new ArrayList<>(
				getSpringFactoriesInstances(BootstrapRegistryInitializer.class));
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
```

<font color="red">**`getSpringFactoriesInstances(BootstrapRegistryInitializer.class)`，是从META-INF/spring.factories文件中获取BootstrapRegistryInitializer的监听器**</font>

<font color="red"> **`getSpringFactoriesInstances(ApplicationContextInitializer.class) `,是从META-INF/spring.factories文件中获取获取ApplicationContextInitializer的监听器** </font>

<font color="red"> **`getSpringFactoriesInstances(ApplicationListener.class)` ,是从META-INF/spring.factories文件中获取获取ApplicationListener的监听器** </font>



因为都是从getSpringFactoriesInstances()方法中获取到监听器，那么我们以`getSpringFactoriesInstances(BootstrapRegistryInitializer.class))`为例，继续往下跟，跳过很多方法，进入如下方法

```java
private <T> List<T> getSpringFactoriesInstances(Class<T> type, ArgumentResolver argumentResolver) {
  return SpringFactoriesLoader.forDefaultResourceLocation(getClassLoader()).load(type, argumentResolver);
}
```

就一行代码，进入`forDefaultResourceLocation`方法内部

```java
public static SpringFactoriesLoader forDefaultResourceLocation(@Nullable ClassLoader classLoader) {
  return forResourceLocation(FACTORIES_RESOURCE_LOCATION, classLoader);
}
```

其中的`FACTORIES_RESOURCE_LOCATION`是在代码中固定写死了的，值为`META-INF/spring.factories`。`forResourceLocation`方法的内容如下

```java
public static SpringFactoriesLoader forResourceLocation(String resourceLocation, @Nullable ClassLoader classLoader) {
  Assert.hasText(resourceLocation, "'resourceLocation' must not be empty");
  ClassLoader resourceClassLoader = (classLoader != null ? classLoader :
                                     SpringFactoriesLoader.class.getClassLoader());
  Map<String, SpringFactoriesLoader> loaders = cache.computeIfAbsent(
    resourceClassLoader, key -> new ConcurrentReferenceHashMap<>());
  return loaders.computeIfAbsent(resourceLocation, key ->
                                 new SpringFactoriesLoader(classLoader, loadFactoriesResource(resourceClassLoader, resourceLocation)));
}
```

其中`loadFactoriesResource`方法内容如下

```java
protected static Map<String, List<String>> loadFactoriesResource(ClassLoader classLoader, String resourceLocation) {
  Map<String, List<String>> result = new LinkedHashMap<>();
  try {
    Enumeration<URL> urls = classLoader.getResources(resourceLocation);
    
    
    while (urls.hasMoreElements()) {
      UrlResource resource = new UrlResource(urls.nextElement());
      Properties properties = PropertiesLoaderUtils.loadProperties(resource);
      properties.forEach((name, value) -> {
        String[] factoryImplementationNames = StringUtils.commaDelimitedListToStringArray((String) value);
        List<String> implementations = result.computeIfAbsent(((String) name).trim(),
                                                              key -> new ArrayList<>(factoryImplementationNames.length));
        Arrays.stream(factoryImplementationNames).map(String::trim).forEach(implementations::add);
      });
    }
    result.replaceAll(SpringFactoriesLoader::toDistinctUnmodifiableList);
  }
  catch (IOException ex) {
    throw new IllegalArgumentException("Unable to load factories from location [" + resourceLocation + "]", ex);
  }
  return Collections.unmodifiableMap(result);
}
```

`classLoader.getResources(resourceLocation)`是获取到当前项目已经所有用到的jar包中的META-INF/spring.factories的全路径，

` PropertiesLoaderUtils.loadProperties(resource)`是遍历所有的路径，以键值对的形式获取到META-INF/spring.factories文件中的内容。



再回到，入口文件的run方法往下执行到如下代码

```java
public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
  return new SpringApplication(primarySources).run(args);
}
```

调用了`run`方法，run方法层层调用最后进入到了SpringApplication中的run方法

```java
public ConfigurableApplicationContext run(String... args) {
  long startTime = System.nanoTime();
  DefaultBootstrapContext bootstrapContext = createBootstrapContext();
  ConfigurableApplicationContext context = null;
  configureHeadlessProperty();
  SpringApplicationRunListeners listeners = getRunListeners(args);
  listeners.starting(bootstrapContext, this.mainApplicationClass);
  ........
    ........
    ........ //这儿过多的代码省略
    return context;
}
```

上述代码中`getRunListeners`就是去获取项目中已经加载了的所有的SpringApplicationRunListener的，点进去看其具体的内容

```java
private SpringApplicationRunListeners getRunListeners(String[] args) {
  ArgumentResolver argumentResolver = ArgumentResolver.of(SpringApplication.class, this);
  argumentResolver = argumentResolver.and(String[].class, args);
  List<SpringApplicationRunListener> listeners = getSpringFactoriesInstances(SpringApplicationRunListener.class, argumentResolver);
  SpringApplicationHook hook = applicationHook.get();
  SpringApplicationRunListener hookListener = (hook != null) ? hook.getRunListener(this) : null;
  if (hookListener != null) {
    listeners = new ArrayList<>(listeners);
    listeners.add(hookListener);
  }
  return new SpringApplicationRunListeners(logger, listeners, this.applicationStartup);
}
```

<font color="red">**`getSpringFactoriesInstances(SpringApplicationRunListener.class, argumentResolver)`方法就是去获取项目中已经加载了的所有的SpringApplicationRunListener的监听器**</font>