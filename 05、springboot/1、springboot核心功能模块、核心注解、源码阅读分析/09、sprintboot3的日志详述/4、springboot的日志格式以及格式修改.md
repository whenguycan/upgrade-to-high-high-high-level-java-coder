## springboot日志格式以及格式修改



#### 日志默认的格式

日志格式如下：

```tex
2023-06-30T13:28:58.977+08:00  INFO 84488 --- [ main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-06-30T13:28:58.977+08:00  INFO 84488 --- [ main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.10]
```

上述日志以空格分隔

- 日志时间和日期：毫秒级经度
- 日志级别：FATAL、ERROR、WARN、INFO、DEBUG、TRACE
- 日志进程ID
- ---是消息分隔符
- 日志线程名称： 使用[]包裹
- 产生日志的类名
- 日志的内容



修改默认日志的格式：

只需要到配置文件中，进行配置就行

```yaml
logging:
  pattern: 
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{15}  ====> %msg%n"
```

`%d{yyyy-MM-dd HH:mm:ss.SSS}`表示日期进行格式化成我们需要的样子

`%-5level`表示输出日志级别，-5表示左对齐5个字符

`[%thread]`表示输出线程名称

`%logger{15}`表示输出哪个类产生的日志

`%msg%n"`表示输出日志的内容，%n表示换行