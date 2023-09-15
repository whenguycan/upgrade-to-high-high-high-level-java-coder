## composition API的使用



#### 1、为什么要使用composition API

因为当项目中一个组件(.vue)中的功能非常复杂，那么组件中的data、computed、watch、methods等就会异常庞大难以维护。

如果使用composition API，我们可以将相同的逻辑点组合在一起，即便到时候组件变得庞大，我们只要知道一个composition API就是一个独立的功能点，就变得不在复杂了！





#### 2、composition API的简单使用

composition API使用setup()做为入口，然后在setup内部去实现各自的data、computed、watch、methods等

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

上面这样写，完全没有提现将相同的逻辑点组合在一起啊，不还是都在export default 里面？如何拆分呢？

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

    const{counter, addOne, age}  = search(); //这儿调用拆出去的代码，拆出去的部分返回几个参数，就需要用几个参数去接
    return {//这儿需要再次return，在其它地方才能调用到，不return出去，无法使用
      counter,
      addOne,
      age
    }
  },
}

  
//将功能拆分到export default外
const search = function() {
  //取代data的使用，注意：属性的值一定要用ref()，否则无法实现属性值的响应式
  let counter = ref(0);


  //取代methods的使用
  const addOne = function () {
    counter.value++ //注意：使用data中的被ref()修饰的数据的值，需要加上.value才能获取到值
    console.log(counter.value)
  }

  //取到omputed的计算属性的使用
  const age = computed(() => {
    return "v1.0"
  })

  //这儿一定要返回，否则一个都无法使用
  return {
    counter,
    addOne,
    age
  }
}
</script>

```

