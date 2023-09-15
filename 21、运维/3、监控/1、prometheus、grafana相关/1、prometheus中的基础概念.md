## prometheus中的基础概念



#### 1、prometheus的组件

- prometheus server     #监控服务端，用于收集、存储、查询数据
- pushgateway               #将自定义监控项收集整合再推送到prometheus上存储，<font color="red">这个组件可选</font>
- alertmanager               #监控告警服务，当prometheus server匹配到告警
- grafana                         #好看的监控页面
- service discovery        #我们可以通过k8s的api动态发现集群的信息，<font color="red">k8s环境中才用</font>
- job/exporters              #收集节点上的资源指标信息,推送、拉取到pushgateway或prometheus



#### 2、prometheus的数据模型

Prometheus会将所有采集到的样本数据以"时间序列"的⽅式保存在内存数据库中并且定时保存到硬盘上。
而"时间序列"是按照 "时间戳" 和 "值" 的序列顺序存放的，我们称之为向量(vector).
每条"时间序列"通过指标名称(metrics name)和⼀组标签集(labelset)命名

如下图，x轴表示时间，y轴表示数据值

Y轴
4                        |
3               |       |       |
2     |        |       |       |       |
1     |        |       |       |       |
0   9:01 9:02  9:03  9:04 9:05  -->x轴
通常来说，时间序列可以定义为一个数据点的序列，这些数据点会按照时间的顺序，从一个数据源来进行索引

在prometheus的范围内，这些数据点是在固定的时间间隔内收集的。因此这种数据以图形形式表示时，通常会绘制数据随时间进行变更。（动态）



在上图时间序列中的每⼀个"|"称为⼀个样本（sample）,由以下三部分组成
1.指标(metric)                 #被测量的目标的属性，比如服务器的主机名、集群名称等
2.测量值(value)               #一个目标可能有多个值，每个测量值对应一个具体的指标。比如服务器的CPU使用率、磁盘IO等
3.时间戳(timestamp)     #标记数据的时间戳

具体格式如下：

               #指标                             #时间戳         #测量值
<--------------- metric -------------------------------------------><-timestamp -><-----value--->
http_request_total{status="200", method="GET"}@1434417560938 => 94355
http_request_total{status="200", method="GET"}@1434417561287 => 94334
http_request_total{status="404", method="GET"}@1434417560938 => 38473
http_request_total{status="404", method="GET"}@1434417561287 => 38544
http_request_total{status="200", method="POST"}@1434417560938 => 4748



#### 3、获取指标信息

使用格式：

```tex
<metric_name>[{<label_1="value_1">,<label_N="value_N">}] <datapoint_numerical_value>
```

语法含义：

```tex
1. <metric_name>      #指标名称，通常为某个属性的值，比如cpu大小、使用率、延迟等
                      #每个属性都有单独的指标名称，一般情况下名称和作用相同
                      #如 http_request_total 表示当前系统接收到的HTTP请求总量

2. [{<label_1="value_1">,<label_N="value_N">}]   
                      #k/v形式展示，我们通常会设置多个标签来对查询的数据进⾏过滤、聚合等。下面有个小案例
          
3. <datapoint_numerical_value>   #数据值的范围
```

案例：

```tex
#比如，我想要匹配的是localhost主机上prometheus的metrics页面http请求总量
prometheus_http_requests_total{instance="localhost:9090",handler="/metrics"}
#返回
prometheus_http_requests_total{code="200", handler="/metrics", instance="localhost:9090", job="prometheus"}  63
```



注意：

> 在使用标签时要注意，以" _ _ "作为前缀的标签，是系统保留的关键字，只能在系统内部使⽤。
> 在Prometheus的底层实现中,指标名称实际上是以 _ _ name _ _= <metrics name> 的形式保存在数据库中的
>
> 
>
> #如下的prometheus查看/metrics请求总量的时间序列
> prometheus_http_requests_total{instance="localhost:9090",handler="/metrics"}
> #实际上等同于下面的语句
>
> {_ _ name_ _="prometheus_http_requests_total",instance="localhost:9090",handler="/metrics"}

