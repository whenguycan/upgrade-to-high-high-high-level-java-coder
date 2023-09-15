## toRefs的时候

当一个响应式对象，被导入使用的时候，对象中的每一个值都会变成非响应式的了，代码如下

```vue
<template>
  index
  <br />
  <button @click="changeage">点击-1</button>
  <br />
  {{age}}
</template>

<script>

import {computed, ref, reactive, toRef} from 'vue'


const info = ()=>{
  const info2 = reactive({
    name: "tangwei",
    height: 1.88,
    age: 32
  })
  return info2
}

export default {

  //入口
  setup() {

    let {name, height, age} = info()



    const changeage = ()=>{
      age --
    }

    return {
      changeage,
      age
    }


  }
}
</script>


```

点击`点击-1`是没有反应的！

但是，如果使用toRefs试试，会把对象内所有的属性都包装成响应式返回。

```vue
<template>
  index
  <br />
  <button @click="changeage">点击-1</button>
  <br />
  {{age}}
</template>

<script>

import {computed, ref, reactive, toRef, toRefs} from 'vue'


const info = ()=>{
  const info2 = reactive({
    name: "tangwei",
    height: 1.88,
    age: 32
  })
  return toRefs(info2)
}

export default {

  //入口
  setup() {

    let {name, height, age} = info()

    const changeage = ()=>{
      age.value --
    }

    return {
      changeage,
      age
    }


  }
}
</script>


```

