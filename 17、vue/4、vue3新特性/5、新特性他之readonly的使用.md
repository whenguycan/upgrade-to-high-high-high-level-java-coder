## readonly的使用



将一个对象设置为只读对象，无法修改

```vue
<template>
  index

  <input type="text" v-model="copy.age" />
  {{copy.age}}
</template>

<script>
import {reactive, isReactive, readonly} from "vue";

export default {

  setup(){

    const info = reactive({
      age: 20,
      height: 1.88
    })

    const copy = readonly(info) //这儿将reactive的数据拷贝了一份变成了只读的了

    return {
      info,
      copy
    }
  }

}


</script>

```

