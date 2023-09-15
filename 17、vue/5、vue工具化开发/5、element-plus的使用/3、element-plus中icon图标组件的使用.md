## element-ui中icon图标的使用

官方参考：https://element-plus.gitee.io/zh-CN/component/icon.html

所有的图标：https://element-plus.gitee.io/zh-CN/component/icon.html#icon-collection



#### 1、安装依赖

使用如下命令即可安装成功

```shell
npm install @element-plus/icons-vue
```



#### 2、注册所有图标

在需要使用图标的.vue中去使用

```js
<script setup lang="ts">
  import {Plus} from "@element-plus/icons-vue";   //引入需要用的图标组件

  const xxxxxx = ()=>{
    console.log("xxxx")
  }
</script>

<template>
  <el-button type="primary" @click="xxxxxx" :icon="Plus">点击</el-button> <!-- :icon是使用引入的icon组件 -->
</template>

<style>
</style>

```

