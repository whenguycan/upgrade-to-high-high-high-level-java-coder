## 项目中手动引入并使用

官网参考：https://element-plus.gitee.io/zh-CN/guide/quickstart.html



#### 1、在main.js中引入并使用

```js
import { createApp } from 'vue'
import ElementPlus from 'element-plus' //引入element-plus
import 'element-plus/dist/index.css'    //引入element-plus的css
import App from './App.vue'

createApp(App).use(ElementPlus).mount('#app') //use(ElementPlus)是显示的在项目中安装并使用element-plus

```





#### 2、测试使用

在App.vue中写入如下代码

```vue
<script setup lang="ts">
  const xxxxxx = ()=>{
    console.log("xxxx")
  }
</script>

<template>
  <el-button type="primary" @click="xxxxxx"></el-button> <!-- 使用element-plus中的组件测试下 -->
</template>

<style>
</style>

```

