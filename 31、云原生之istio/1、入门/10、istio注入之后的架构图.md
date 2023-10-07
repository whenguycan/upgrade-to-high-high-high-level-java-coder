## istio注入之后的架构图

![avatar](../images/3.jpeg)

Galley是负责，把istio-init、istio-proxy注入到需要注入的pod、deployment中去的。

Citadel是负责管理证书的。