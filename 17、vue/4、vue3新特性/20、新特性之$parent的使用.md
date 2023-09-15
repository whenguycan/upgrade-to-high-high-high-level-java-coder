## $parent的使用



在子组件中可以使用$parent获取到父节点的信息和暴露的数据



```vue
<template>

  child: {{money}}

  <button @click="ccc($parent)">点击</button>  <!-- 注意：这儿的传参一定要是$parent,不能改，否则拿不到当前子组件的父组件 -->


</template>

<script setup>


import {useAttrs, ref, defineExpose} from "vue";

let money = ref(100)

const ccc = ($parent)=>{
    console.log($parent.money) //拿到父组件中暴露的数据
}

defineExpose({
  money
})



</script>


```

