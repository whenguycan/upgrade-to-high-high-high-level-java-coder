## springboot的IOC执行流程源码解析



![avatar](../../images/WechatIMG635.png)

registerListeners()方法，只是把系统中所有的ApplicationListener放到EventMulticaster中，以便我们后续使用publish发布事件。具体可以去看`事件驱动开发源码解析`一文。

![avatar](../../images/WechatIMG636.png)