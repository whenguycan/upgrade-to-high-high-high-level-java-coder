## 集成skywalking

> skywalking的java-agent的版本需要与skywalking-server的版本一致

#### 1、下载skywalking的agent

下载地址：https://archive.apache.org/dist/skywalking/java-agent/



#### 2、集成

- 解压下载的压缩包

- 找到config文件夹下的agent.config文件，并编辑主要的几项，并拷贝到固定目录

  ```shell
  # 不同的namespace会导致调用链路追踪中断，同一个链路下的所有应用都用这个namespace
  agent.namespace=${SW_AGENT_NAMESPACE:xxx}
  
  # 页面上展示的service的名称，每个微服务配置自己的，一般通过-Dskywalking.agent.service_name=xxx指定，
  agent.service_name=${SW_AGENT_NAME:xxxxx}
  
  # 平台的调用地址，也可以通过-Dskywalking.collector.backend_service=127.0.0.1:80指定。11800端口是skywalking-server启动之后就默认会监听的agent端与server通讯的端口
  collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:xxx.xxx.xxx.xxx:11800}
  
  # 忽略指定后缀的请求收集
  agent.ignore_suffix=${SW_AGENT_IGNORE_SUFFIX:.jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg}
  
  # 每3秒的采样率，负数代表100%
  agent.sample_n_per_3_secs=${SW_AGENT_SAMPLE:-1}
  ```

- 找到skywalking-agent.jar到固定目录

- 启动项目

  ```shell
  java -javaagent:/xx/xx/skywalking-agent.jar -jar yourJar.jar -Dskywalking.agent.service_name=gps -Dskywalking.collector.backend_service=106.12.254.105:11801
  ```

  启动会自动找到/xx/xx/skywalking-agent.jar同级目录下的config目录下的agent.config文件

​	

​	注意：在idea中，上述 `-javaagent:xxxxx -Dskywalking.agent.service_name=gps -Dskywalking.collector.backend_service=106.12.254.105:11801` 要添加在`VM options`中，注意 javaagent前只有1个`-`。



- <font color="red">skywalking默认不兼容spring-cloud-gateway的不同版本【agent启动时要使用的插件都在plugins文件夹中，里面没有spring-cloud-gateway的包】。在解压后的包中有个`optional-plugins`文件夹，里面提供了兼容spring-cloud-gateway的包。找到对应的版本给他拷贝到plugins文件夹中，然后重启你的微服务即可。</font>agent所有所有支持的中间件，查看如下地址：https://skywalking.apache.org/docs/skywalking-java/next/en/setup/service-agent/java-agent/supported-list/



​	集成成功，在项目启动的时候，日志的最前面会有2行关于skywalking-agent的日志，且会在skywalking的agent的jar包的同级目录会有日志打出（通讯日志居然没有！！）。

​	<font color="green">集成成功，访问项目路径，可以到es的`sw_endpoint_traffic_xxxx`的index中找到数据，能找到数据证明没有问题了！</font>



随后就可以进行测试了！



