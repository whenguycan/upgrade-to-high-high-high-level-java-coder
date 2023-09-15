## nacos config监听本项目以外的文件

> 参考文档：@NacosConfigListener注解的功能参考，https://nacos.io/zh-cn/docs/nacos-spring.html

#### 1、问题产生背景

都知道，在spring boot项目中nacos config默认读取的配置文件有：

> 如果下面的 `spring.cloud.nacos.config.prefix`没有配置，那么就默认读 `spring.application.name`

1. {spring.cloud.nacos.config.prefix}
2. {spring.cloud.nacos.config.prefix}−{spring.profile.active}
3. {spring.cloud.nacos.config.prefix}−{spring.profile.active}.{spring.cloud.nacos.config.file-extension}

那么，如何在一个项目中读取更多的配置文件，并监听其变化呢？

#### 2、使用@NacosConfigListener注解

用了这个注解在项目启动的时候就会读取一次指定dataId的数据，并在后续dataId更新的时候触发Listener。但是这个注解在引入`spring-cloud-starter-alibaba-nacos-config`这个包的情况下是无法使用的，具体原因未知，可能得去看源码才知道。

#### 3、最终解决

找了一个替代的方案，代码示例如下：

> 如下这样写，项目启动就会去订阅和监听`ttt.properties`这个文件。

```java
@Component
public class LoadNacosConfig {

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @PostConstruct
    public void tt() throws NacosException {
      	//将读取到的nacos数据，写回到本地，模拟系统启动就读取到了数据
        nacosConfigManager.getConfigService().getConfig("ttt.properties", "DEFAULT_GROUP", 3000);
        
				//通过nacos的ConfigService，给文件添加监听器
        nacosConfigManager.getConfigService().addListener("ttt.properties", "DEFAULT_GROUP", new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
              	//当ttt.properties文件有改动，就会触发这个方法执行，不过它这儿是拿到整个文件内容的字符串。
                //将读取到的nacos数据，写回到本地
            }
        });
    }
}

```



