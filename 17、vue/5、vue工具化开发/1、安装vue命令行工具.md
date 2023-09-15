## 安装vue命令行工具



#### 1、运行命令

```shell
npm install -g @vue/cli@next
```

安装最新的vue cli



如果需要升级vue cli的版本，需要先`npm uninstall -g @vue/cli`写在之前安装的vue，然后再重新装新版本。



#### 2、安装到哪里去了？

通过`npm config get prefix`查看npm的全局安装的目录，vue就被安装到了全局安装目录中的bin目录中。

