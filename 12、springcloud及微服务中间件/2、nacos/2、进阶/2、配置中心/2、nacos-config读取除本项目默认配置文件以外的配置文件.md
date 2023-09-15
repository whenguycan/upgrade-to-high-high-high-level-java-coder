## nacos config读取本项目配置文件以外的配置文件



#### 1、问题产生背景

都知道，在spring boot项目中nacos config默认读取的配置文件有：

> 如果下面的 `spring.cloud.nacos.config.prefix`没有配置，那么就默认读 `spring.application.name`

1. {spring.cloud.nacos.config.prefix}
2. {spring.cloud.nacos.config.prefix}−{spring.profile.active}
3. {spring.cloud.nacos.config.prefix}−{spring.profile.active}.{spring.cloud.nacos.config.file-extension}

那么，如何在一个项目中读取更多的配置文件，并监听其变化呢？



#### 2、如何解决问题？

> 下面的配置写在bootstrap配置文件中

只要在配置文件中指明你另外要读取的配置文件即可：

```properties
spring.cloud.nacos.config.extension-configs[0].data-id=kang.yaml
spring.cloud.nacos.config.extension-configs[0].refresh=true  #如果在nacos上修改了配置，这个配置是实时刷新的
spring.cloud.nacos.config.extension-configs[0].group=DEFAULT_GROUP
```

