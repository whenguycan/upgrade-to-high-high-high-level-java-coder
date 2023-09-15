## 自定义logback的配置

只要在项目的resource目录下，新建logback.xml或者logback-spring.xml文件，springboot就会读取这个文件中的配置做为logback的配置了。

**springboot会先加载配置文件，再加载logback-spring.xml文件，而logback.xml是在配置文件加载之前加载的**





logback.xml或者logback-spring.xml的内容，详细分析如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="10 seconds">  <!-- scan: 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。scanPeriod: 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟 -->

    <!--
      配置几种管理属性
      我们可以直接改属性的value值
      格式：${name} 即可
    -->
    <property name="devel" value="[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} %c %M %L %thread ------------------ %m%n"></property>
    <!--
      日志输出格式：
      %logger{50} 产生日志的类名
      %-5level -5为向左对齐5个字符
      %d{yyyy-MM-dd HH:mm:ss.SSS}日期
      %c类的完整名称
      %M为method的名称
      %L为行号
      %thread为线程名称
      %m或%msg为信息
      %n为换行
    -->

    <!-- 控制台日志输出的 appender-->
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <!-- 控制台输出流对象 默认 System.out 改为 System.err -->
        <target>System.err</target> <!-- 这儿是修改打印的字体颜色为红色 -->

        <!-- 日志消息格式配置 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${devel}</pattern> <!-- ${上面定义的proerty的name的值} -->
        </encoder>
    </appender>


    <!--日志文件输出的 appender -->
    <appender name="file" class="ch.qos.logback.core.FileAppender">
        <!-- 日志文件的保存路径 -->
        <file>./yy.log</file>
        <!-- 日志消息格式配置 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${devel}</pattern> <!-- ${上面定义的proerty的name的值} -->
        </encoder>
    </appender>


    <!-- html格式的日志文件输出的 appender -->
    <appender name="html" class="ch.qos.logback.core.FileAppender">
        <!-- 日志文件的保存路径 -->
        <file>./yyyy.html</file>
        <!-- htm的 日志消息格式配置 -->
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="ch.qos.logback.classic.html.HTMLLayout">
                <pattern>${devel}</pattern> <!-- ${上面定义的proerty的name的值} -->
            </layout>
        </encoder>
    </appender>

  	<!-- 日志拆分和归档压缩的 appender 对象 -->
    <appender name="rollFile" class="ch.qos.logback.core.rolling.RollingFileAppender">

        <!-- 滚动日志首先会记录到一个文件中，等到了一定量了才开始滚动切割 -->
        <file>./roll.log</file>
        <!-- 日志消息格式配置 -->
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${devel}</pattern> <!-- ${上面定义的proerty的name的值} -->
        </encoder>

        <!-- 指定拆分规则 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <!-- 按照时间和压缩格式声明拆分的文件名 -->
            <fileNamePattern>./rolling.%d{yyyy-MM-dd}.log-%i.gz</fileNamePattern> <!-- .gz结尾表示压缩文件，可以不带，直接.log结尾 -->

            <!-- 按照文件大小来拆分 KB、MB、GB -->
            <maxFileSize>10KB</maxFileSize>

            <!-- 文件保存天数 -->
            <maxHistory>7</maxHistory>
            <!-- 这儿的配置是，每天生成的日志是以日期为文件名，如果一天内的日志没超过1MB就会切割日志到不同的文件中以0、1、2、3、4.....排序 -->

        </rollingPolicy>


        <!-- 日志级别过滤器 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 日志过滤规则-->
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch> <!-- 达到或超过ERROR的日志被记录 -->
            <onMismatch>DENY</onMismatch> <!-- 没超过ERROR的日志被拦截，不会写入日志 -->
        </filter>
    </appender>

    <!-- 异步日志 因为同步日志的话，主线程要等日志记录完毕才会退出太影响性能了-->
    <appender name="xxx" class="ch.qos.logback.classic.AsyncAppender">
        <!-- 指定上面具体哪个appender需要走异步日志记录 -->
        <!-- <appender-ref ref="appender的名称"></appender-ref> -->
        <appender-ref ref="rollFile"></appender-ref>
    </appender>


    <!-- root logger 配置,即整个项目的日志级别， 只能有一个root标签 -->
    <!-- DEBUG ->INFO -> WARN ->ERROR 如果写的是INFO，那么ERROR也会被记录 -->
    <root level="INFO">
      	<!-- 在该级别下，要进行的操作！ -->
        <appender-ref ref="stdout"></appender-ref>
        <appender-ref ref="file"></appender-ref>
        <appender-ref ref="rollFile"></appender-ref>
        <!-- <appender-ref ref="上面配置好的日志输出到控制台的appender的name的名称"></appender-ref> -->
        <!-- <appender-ref ref="上面配置好的日志输出到文件的appender的name的名称"></appender-ref> -->
        <!-- <appender-ref ref="上面配置好的日志输出到html文件的appender的name的名称"></appender-ref> -->
        <!-- <appender-ref ref="上面配置好的日志异步记录的name的名称"></appender-ref> -->
    </root>

    <!-- 自定义logger对象 , 即调整项目中某个类、某个包的日志级别。additivity = "false" 自定义 logger对象是否继承 root logger-->
<!--    <logger name="需要记录日志的package的路径 eg: com.twmall" level="info" additivity = "false">-->
  					<!-- 在该级别下，要进行的操作！ -->
<!--        <appender-ref ref="上面配置好的日志appender的name的名称"></appender-ref>-->
<!--    </logger>-->

    <!-- 根据配置文件激活参数(active) 选择性的包含和排查部分配置信息 -->
<!--    <springProfile name="根据配置文件激活参数(active) 选择性的包含和排查部分配置信息">-->
<!--        上面的root、appender标签都可以包含到这个标签中-->
<!--    </springProfile>-->

</configuration>
```

