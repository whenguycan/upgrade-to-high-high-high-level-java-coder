## 客户端发布配置文件到服务端

```java
@Autowired
private NacosConfigManager nacosConfigManager;

public void index(){
  //发布配置文件到nacos管理端
  nacosConfigManager.getConfigService().publishConfig("文件名称", "所属组", "文件内容，字符串格式的", "配置文件类型");
}
```

