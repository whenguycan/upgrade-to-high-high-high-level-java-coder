## modules标签的使用



#### 1、使用范例

```xml
<modules>
  <module>authentication</module>
  <module>tenantds</module>
  <module>formsign</module>
  <module>feign-ribbon-reload</module>
  <module>redis-tools</module>
  <module>oss</module>
  <module>sdk</module>
  <module>ocr</module>
  <module>fadada</module>
  <module>pay</module>
  <module>message</module>
  <module>weichat</module>
  <module>utilities</module>
</modules>
```



#### 2、范例详解

- 标签详解
  1. <modules>标签，用在父项目中，声明项目有哪些子模块。
  2. <module>标签，声明具体有哪些子模块，其中的值，填写子模块的包名（而包名与子模块pom.xml中的name和artifactId的值一致）
  2. 在根模块操作maven命令，所有的子模块都会执行。