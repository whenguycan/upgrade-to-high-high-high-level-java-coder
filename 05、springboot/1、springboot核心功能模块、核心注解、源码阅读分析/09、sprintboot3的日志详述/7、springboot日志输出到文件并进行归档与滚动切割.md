## springboot日志输出到文件并进行归档与滚动切割



#### 配置日志输出到文件

配置示例

```yaml
logging:
  file:
    path: ./ #指定日志文件的保存路径，如果不配置path，那么就直接生成到当前目录
    name: ./demo.log #指定日志文件的保存名称
```

这样配置日志就会顺利写到文件中。



#### 配置日志归档和滚动切割

在生产环境下，我们的服务一直在运行，日志写在文件中，那么日志会越来越大，如果运行达到一定的时候一个文件会上T的大小，我们如果要分析这个日志文件，根本无法打开！所以，我们就需要进行日志归档和滚动切割

所谓的归档：每天的日志单独存到一个文档中

所谓的切割：每个日志文件10M的大小，超过10M就会进行切割，存到另外一个日志文件中。



springboot的logback底层已经帮我们都做好了这些工作，我们只需要进行配置文件的配置就可以实现这些功能。

配置示例

```yaml
logging:
  file:
    path: ./ #指定日志文件的保存路径
    name: ./demo.log #指定日志文件的保存名称
  logback:
    rollingpolicy: #配置滚动策略
      file-name-pattern: ${LOG_FILE}.%d{yyyy-MM-dd}.%i.gz # 配置归档的时候文件名叫什么  ${LOG_FILE}就是我们logging.file.name配置的，%d{yyyy-MM-dd}是指时间，%i是当前日期下的第几个文件，gz是指对日志进行压缩存储
      max-file-size: 10KB #配置超过多少就需要进行文件切割，这儿是10KB。兆用这个单位：MB
      clean-history-on-start: false #配置应用启动时删除以前的归档文件，默认为false
      total-size-cap: 1GB #配置所有的归档的大小的总和不能超过1GB，超过1GB就会删除一些老的归档文件
      max-history: 7 #日志文件最大保存天数，默认是7天

```

