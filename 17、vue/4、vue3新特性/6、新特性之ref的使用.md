## ref的使用

将普通（基本）数据包装为响应式数据



我们在setup中创建一个普通数据，不适用ref包装的话是无法实现响应式的

```vue
<template>
  index

  <input type="text" v-model="age" />
  {{age}}
</template>

<script>
import {reactive, isReactive, readonly} from "vue";

export default {

  setup(){

    const age = 10

    return {
      age
    }
  }

}


</script>

```



得使用ref对数据先绑定一下,才能实现普通数据的响应式，代码如下

```vue
<template>
  index
  <br />
  <button @click="addOne">点击+1</button>
  {{counter}}
  <br />
  {{age}}
</template>

<script>

import {computed, ref} from 'vue'

export default {

  //入口
  setup(){

    //取代data的使用，注意：属性的值一定要用ref()，否则无法实现属性值的响应式
    let counter = ref(0);


    //取代methods的使用
    const addOne = function (){
      counter.value ++ //注意：使用data中的被ref()修饰的数据的值，需要加上.value才能获取到值
      console.log(counter.value)
    }

    //取到omputed的计算属性的使用
    const age = computed(()=>{
      return "v1.0"
    })

    //这儿一定要返回，否则一个都无法使用
    return {
      counter,
      addOne,
      age
    }

  }
}
</script>


```

