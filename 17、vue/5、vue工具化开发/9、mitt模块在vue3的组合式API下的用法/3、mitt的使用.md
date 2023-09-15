## mitt的使用



#### 1、初始化mitt对象

```vue
//引入mitt插件
import mitt from 'mitt'

//执行mitt()方法，返回一个Emitter对象
const emitter = mitt();

//暴露这个对象
export default emitter;

```





#### 2、一个组件中等待接收传递的参数

```vue
<template>
  index

  <Child></Child> <!-- 使用子组件，触发子组件中的info事件 -->

</template>

<script setup>

import Child from './Child.vue' //导入子组件

//引入mitt初始化后的对象
import bus from '../bus/Index.ts'

import {onMounted} from "vue";

onMounted(()=>{

    //使用on方法，此时，组件会一直等待bus总线触发指定的方法
    //第一个参数是事件的名称，第二个参数是事件触发的回调， param是传递的数据
    bus.on("car", (param)=>{
      console.log(param)
    })

})

</script>




```





#### 3、一个组件中发送参数

```vue
<template>

  child

  <button @click="sendMessage"></button>


</template>

<script setup>

import bus from '../bus/Index.ts'

import {onMounted} from "vue";

const sendMessage = ()=>{
  //发送参数
  bus.emit("car", "传递的参数！")
}



</script>


```

