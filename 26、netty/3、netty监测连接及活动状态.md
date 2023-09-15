## netty监测连接及活动状态



#### 一、图解

![avatar](./images/60.jpg)



#### 二、详细说明

> 这些方法都是写在client/server的Handler类中的

1. handlerAdded，<font color="red">服务器端</font>每当有新的socket连接到服务器端，就会触发一次这个方法

![avatar](./images/22.jpg)

2. handlerRemoved，<font color="red">服务器端</font>每当有socket连接断开，就会触发一次这个方法

   ![avatar](./images/61.jpg)

3. channelActive，<font color="red">服务器端和客户端</font>当socket建立连接完毕，就会触发这个方法，且只会触发一次

   - 服务端

     ![avatar](./images/33.jpg)

     

   - 客户端

     ![avatar](./images/32.jpg)

4. channelnactive，<font color="red">服务器端和客户端</font>当对端socket关闭（通道失效）时，就会触发这个方法，且只会触发一次

- 服务端

  ![avatar](./images/34.jpg)

- 客户端

  ![avatar](./images/35.jpg)