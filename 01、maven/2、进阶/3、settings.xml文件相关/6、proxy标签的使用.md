## proxy标签的使用



#### 1、使用范例

```xml
<proxies>
  <proxy>
    <id>sdc</id>
    <protocol>http</protocol>
    <host>10.10.210.19</host>
    <port>8888</port>
    <active>true</active>
  </proxy>
</proxies>
```





#### 2、范例详述

proxy是配置maven的访问外网的代理的，默认情况下宿主机配置的网络代理对maven是无效的，maven需要单独配置自己的代理。

`<id>`给maven的代理分配一个id

`<protocol>`maven代理的协议

`<host>`maven代理的ip

`<port>`maven代理的端口

`<active>` 是否激活maven代理。