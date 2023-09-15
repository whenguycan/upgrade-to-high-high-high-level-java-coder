## toRef的使用

在一个响应式对象的基础上，给响应式对象中的一个property创建一个响应式对象。



目前有如下代码：

```vue
<template>
  index
  <br />
  {{info.name}}
  <button @click="changeage">点击-1</button>
  <br />
  {{info.age}}
</template>

<script>

import {computed, ref, reactive, toRef} from 'vue'

export default {

  //入口
  setup() {

    const info = reactive({
      name: "tangwei",
      height: 1.88,
      age: 32
    })


    const changeage = ()=>{
      info.age --
    }

    return {
      info,
      changeage
    }


  }
}
</script>


```

目前功能正常，当点击`点击-1`的时候，info.age会发生-1，页面上{{info.age}}会跟着响应。因为info是响应式数据，没问题，但是，如果我用一个变量将info.age的数据接回来单独用一个变量接，那么，现在这个单独的变量是响应式的么？

```vue
<template>
  index
  <br />
  {{info.name}}
  <button @click="changeage">点击-1</button>
  <br />
  {{info.age}}
  <br />
  {{age}}
</template>

<script>

import {computed, ref, reactive, toRef} from 'vue'

export default {

  //入口
  setup() {

    const info = reactive({
      name: "tangwei",
      height: 1.88,
      age: 32
    })

    let age = info.age

    const changeage = ()=>{
      age --
    }

    return {
      info,
      changeage,
      age
    }


  }
}
</script>


```

发现点击`点击-1`之后，触发`age --`，这个age不是响应式的了！

当然我们可以使用ref给它包一下，但是有有没有别的方法？

用toRef，代码如下：

```vue
<template>
  index
  <br />
  {{info.name}}
  <button @click="changeage">点击-1</button>
  <br />
  {{info.age}}
  <br />
  {{age}}
</template>

<script>

import {computed, ref, reactive, toRef} from 'vue'

export default {

  //入口
  setup() {

    const info = reactive({
      name: "tangwei",
      height: 1.88,
      age: 32
    })

    let age = toRef(info, 'age') //info为对象名称， 'age'为对象中的一个属性

    const changeage = ()=>{
      console.log(age)
      age.value --
    }

    return {
      info,
      changeage,
      age
    }


  }
}
</script>


```

当修改age数据的时候，info.age的数据也会被修改！