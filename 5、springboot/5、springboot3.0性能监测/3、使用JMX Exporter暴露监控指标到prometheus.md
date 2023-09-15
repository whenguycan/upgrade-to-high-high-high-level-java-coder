## 使用JMX Exporter暴露监控指标到prometheus

可以使用Prometheus 社区开发了 JMX Exporter 来导出 JVM 的监控指标，以便使用 Prometheus 来采集监控数据。



#### 项目如何集成

> 我们下面采用的是在项目启动的时候，指定javaagent的形式运行JMX-Exporter 的 jar 包，进程内读取 JVM 运行时状态数据，转换为 Prometheus metrics 格式，并暴露端口让 Prometheus 采集

- 操作步骤

  - 准备配置文件config.yaml

    > 在启动指定javaagent的时候也需要指定配置文件

    ```yaml
    ssl: false
    ...
    ```

    更多参数可以参考：https://github.com/prometheus/jmx_exporter

    

  - 下载jmx-exporter的jar包：

    现成包下载地址：https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/

    源码下载地址：https://github.com/prometheus/jmx_exporter/tags

    

  - 启动项目的时候，使用如下命令

    ```shell
    java -javaagent:/xx/xx/jmx_prometheus_javaagent-0.13.0.jar=11011:config.yaml -jar yourJar.jar
    ```

    注意：在idea中，上述 -javaagent 要添加在`VM options`中，注意 javaagent前只有1个`-`。

    

  - 项目启动之后，使用上面我们指定的jmx-exporter的端口，访问如下地址，能访问通证明jmx-exporter部署没有问题

    访问地址：http://localhost:11011/metrics

