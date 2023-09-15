## 什么是sonarqube

sonarqube是一个代码质量检测工具，能扫描你代码中的基础bug

sonarqube由2部分组成：

1. sonar-scanner，sonar的客户端扫描工具，启动这个工具会扫描你的所有代码代码，并汇总扫描结果。
2. sonar-server，sonar的服务端，客户端sonar-scan执行之后需要将扫描结果汇总并提交给sonar-server，sonar-server会记录本次扫描的结果，提供web形式的管理和查看。



原理图：

![avatar](../images/5.jpeg)