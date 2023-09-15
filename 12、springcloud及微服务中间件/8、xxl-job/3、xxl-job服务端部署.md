## xxl-job服务端部署



#### 1、下载源码

> 我们下载的是2.3.0的版本

下载地址：https://github.com/xuxueli/xxl-job/tags



#### 2、IDEA打开xxl-job项目



#### 3、找到项目中的doc > db > tables_xxl_job.sql 文件，将sql导入到mysql数据库中。

其中

1. spring.mail开头的是配置，服务告警的！
2. xxl.job.accessToken 是用于客户端与服务端通讯安全使用的，可以不填



#### 4、找到xxl-job-admin这个module的配置文件，修改数据库连接信息即可



#### 5、如果是想在本地启动，就直接启动xxl-job-admin这个module，如果是想在服务端启动就mvn package下这个module，生成jar包就行！

如果遇到`/data/applogs/xxl-job/xxl-job-admin.log (No such file or directory)`这个错误，就找到xxl-job-admin这个module中的logback.xml，把`<property name="log.path" value="/data/applogs/xxl-job/xxl-job-admin.log"/>`改为`<property name="log.path" value="./data/applogs/xxl-job/xxl-job-admin.log"/>`

<font color="red">经尝试，打包到centos运行的时候，是不会出现上面的错误的</font>



#### 6、访问http://localhost:8080/xxl-job-admin 即可登录xxl-job的管理后台，用户名/密码：admin/123456