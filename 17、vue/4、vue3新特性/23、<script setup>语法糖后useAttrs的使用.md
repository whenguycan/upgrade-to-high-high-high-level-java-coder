## useAttrs的使用

在useAttrs之前被defineProps接收过了，那么useAttrs就接收不到了！



使用范例



父组件中传递数据到子组件

```vue
<template>
  index

  <br />
  <Child v-model:money="money" age="34" gender="男"></Child>
</template>

<script setup>

import Child from './Child.vue'
</script>




```



子组件中使用useAttrs接收参数

```vue
<template>

  child


</template>

<script setup>

import {useAttrs} from "vue";

let $attrs = useAttrs();
console.log($attrs.gender)

</script>


```

