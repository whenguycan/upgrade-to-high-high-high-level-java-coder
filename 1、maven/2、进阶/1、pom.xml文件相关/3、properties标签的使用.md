## properties标签



#### 1、使用范例

```xml
<properties>
  <java.version>1.8</java.version>
</properties>


......

<version>${java.version}</version>
```



#### 2、范例详述

- 定义变量

  <properties>标签内部，是用来自定义变量的，举例：<java.version>是自定义变量的名称，1.8为自定义变量的值。

- 使用变量

  使用变量只要使用${自定义变量名称} 就可以了！