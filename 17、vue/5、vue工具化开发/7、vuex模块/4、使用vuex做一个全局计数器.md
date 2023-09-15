## 使用vuex做一个全局计数器



#### 1、在store/index.js中做如下修改

```js
import {createStore} from "vuex";

export default createStore({
    state: {
        counter: 20 //初始化一个store的数据
    },
    mutations: {
        add(state, "接收组件的传参"){ //编写对state中数据的更改方法，其中的参数是 就是上面的state
            state.counter ++
        }
    },
    actions: {

    },
    modules: {

    }
})

```



#### 2、在入口index.js中引入store并注册

```js
import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/index.js'
import store from './store/index.js' //引入store

//注册store
createApp(App).use(store).use(router).mount('#app')

```



#### 3、到对应的组件中写代码

```vue
<template>

  <div>
    <button @click="addCounter">{{$store.state.counter}}</button> <!-- {{$store.state.counter}}可以直接获取到store中的数据 -->
  </div>

</template>

<script>
export default {


  methods: {
    addCounter: function (){
      this.$store.commit('add', "要传入的参数") //add对应store的mutations中的方法
    }
  }





}

</script>

```

