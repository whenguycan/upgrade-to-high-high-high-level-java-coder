## vue项目详解



#### 1、main.js是项目的入口文件

内容如下

```js
// 引入vue框架
import { createApp } from 'vue'

//引入根组件
import App from './App.vue'

//注册 和 挂载
createApp(App).mount('#app')

```



#### 2、public下的index.html文件

index.html是在第一步中被mount("#app")指向的文件，所以后续的组件可以认为都是在index.html中运行的。



#### 3、App.vue文件

是vue项目的根组件，以后所有的开发的子组件都需要在这儿被引入

```vue
<template>
  <img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App好"/>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue' //导入一个组件

export default {
  name: 'App',
  components: {
     HelloWorld //导入组件后，不能直接使用，需要再在本组件中声明一次
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

```



#### 4、HelloWorld.vue文件

干活的组件，代码略。

