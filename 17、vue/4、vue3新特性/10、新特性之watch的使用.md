## watch的使用



#### 1、监听单个数据源

```vue
<template>
  index
  <br />
  {{count}}
</template>

<script>

import {ref, watchEffect, reactive, watch} from 'vue'

export default {

  //入口
  setup() {

    let count = ref(0)

    //写法1
    watch(count, (newVal, oldVal)=>{//count为要监听的数据，newVal是变更后的新值，oldVal是变更前的值
      console.log("newVal:" + newVal + ", oldVal:" + oldVal)
    })

    //写法2
    watch(()=>{return count.value},(newVal, oldVal)=>{//前一个是一个回调函数，需要返回要监听数据的值，，newVal是变更后的新值，oldVal是变更前的值
      console.log("newVal:" + newVal + ", oldVal:" + oldVal)
    })



    count.value ++


    return {
      count
    }

  }
}
</script>


```





#### 2、监听多个数据源

```vue
<template>
  index
  <br />
  {{count}}
  <br />
  {{num}}
</template>

<script>

import {ref, watchEffect, reactive, watch} from 'vue'

export default {

  //入口
  setup() {

    let count = ref(0)

    let num = ref(10)

    watch([count, num], ([newVal, oldVal], [newNum, oldNum])=>{//count、num为要监听的数据，newVal是变更后的新值，oldVal是变更前的值
      console.log("newVal:" + newVal + ", oldVal:" + oldVal)
    })





    count.value ++
    num.value ++


    return {
      count,
      num
    }

  }
}
</script>


```

