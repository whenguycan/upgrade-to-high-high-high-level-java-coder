## Mac安装GraalVM



#### 1、下载GraalVM

下载地址：https://www.graalvm.org/downloads/#

注意选择java的版本和平台



#### 2、使用如下命令解压并安装到指定目录

```shell
tar -zxvf graalvm-jdk-17_........tar.gz #解压graalvm的安装包

mv graalvm-jdk-<version>_macos-<architecture> /Library/Java/JavaVirtualMachines #将解压出来的目录移动到指定目录中
```

因为graalvm其实是在jdk的基础上的再封装，所以进入到安装目录中的bin目录，发现java等命令还在



#### 3、配置graalvm的bin路径

到~/.bash_profile目录中新增内容如下

```shell
export JAVA_HOME=/usr/graalvm/graalvm-jdk-17.0.7+8.1
export PATH=$PATH:$JAVA_HOME/bin
```

然后记得`~/.bash_profile`让graalvm.sh文件生效



#### 4、运行如下命令查看graalvm是否安装成功

```shell
java -version
```



如何查看graalvm的版本

```shell
gu --version
```



#### 5、安装native-image

参考：https://www.graalvm.org/22.0/reference-manual/native-image/#install-native-image

运行如下命令即可安装好native-image

```shell
gu install native-image
```

当出现`Component Native Image (org.graalvm.native-image) is already installed.`表示native-image已经被成功安装了！

可以尝试在终端使用下`native-image`命令





至此，graalvm在linux上的安装就全部完成了！