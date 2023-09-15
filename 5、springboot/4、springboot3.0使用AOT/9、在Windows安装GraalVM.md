## 在Windows安装GraalVM



### 1. 安装native-image需要的VisualStudio

下载地址：https://visualstudio.microsoft.com/zh-hans/free-developer-offers/

![img](../images/1.png)



![img](../images/2.png)

别选中文

![img](../images/4.png)



![img](../images/5.png)



记住你安装的地址；



### 2. GraalVM 

#### 1. 安装

下载 GraalVM，下载地址：https://www.graalvm.org/downloads/#

注意选择java的版本和平台，然后解压到一个目录中





#### 2. 配置

修改 JAVA_HOME 与 Path，指向新bin路径

![img](https://cdn.nlark.com/yuque/0/2023/png/1613913/1684144739494-b078d166-5e09-421d-b237-7ee1a2c153f6.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_24%2Ctext_5bCa56GF6LC3IGF0Z3VpZ3UuY29t%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)

![img](https://cdn.nlark.com/yuque/0/2023/png/1613913/1684144848621-d8577753-5a5b-402a-863b-617f43e35db1.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_19%2Ctext_5bCa56GF6LC3IGF0Z3VpZ3UuY29t%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)



验证JDK环境为GraalVM提供的即可：

![img](https://cdn.nlark.com/yuque/0/2023/png/1613913/1684144703862-26be3be1-dd2d-491e-8eca-2317495d77cb.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_28%2Ctext_5bCa56GF6LC3IGF0Z3VpZ3UuY29t%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)



#### 3. 依赖

安装 native-image 依赖：

参考：https://www.graalvm.org/latest/reference-manual/native-image/#install-native-image

```shell
gu install native-image
```



#### 4. 验证

```shell
native-image
```





## 