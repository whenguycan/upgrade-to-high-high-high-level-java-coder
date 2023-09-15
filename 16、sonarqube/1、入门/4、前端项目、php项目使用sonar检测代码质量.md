## 前端项目、php项目使用sonar进行代码质量检测





#### 1、本机下载下载sonar-scanner，注意对应自己的操作系统：

下载地址：https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/

#### 2、把下载好的包解压



#### 3、修改配置conf/sonar-scanner.properties文件（这一步可有可无）

sonar.host.url = http://sonarqube-server的地址:9000

sonar.sourceEncoding=UTF-8



#### 4、生成sonar的token

我的账号----->安全---->生成令牌



#### 4、在你需要代码扫描的项目中，运行：

sonar-scanner \

​     -Dsonar.projectKey=projectname \

​     -Dsonar.sources=. \  #要扫描的路径

​     -Dsonar.host.url=http://192.168.2.235:9000 \  #配置文件中配置了 这儿就可以省略

​     -Dsonar.login=215aaf8ec6b26c4a66bbdb8ffddcef738a863ce0 #配置文件中配置了 这儿就可以省略