## 安装node.js开发环境



#### 1、下载node.js

下载地址：https://nodejs.org/en/download/



#### 2、解压下载的压缩文件



#### 3、将解压中间的bin目录添加到PATH路径中，安装就完成了。



#### 4、运行测试

```shell
node -v
```

如果出现错误，则需要对应解决

- 常见错误1

  ```shell
  ./node: /lib64/libm.so.6: version `GLIBC_2.27' not found (required by ./node)
  ./node: /lib64/libc.so.6: version `GLIBC_2.25' not found (required by ./node)
  ./node: /lib64/libc.so.6: version `GLIBC_2.28' not found (required by ./node)
  ./node: /lib64/libstdc++.so.6: version `CXXABI_1.3.9' not found (required by ./node)
  ./node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.20' not found (required by ./node)
  ./node: /lib64/libstdc++.so.6: version `GLIBCXX_3.4.21' not found (required by ./node)
  ```

  node的版本与glibc的版本不匹配，需要降低node的版本

