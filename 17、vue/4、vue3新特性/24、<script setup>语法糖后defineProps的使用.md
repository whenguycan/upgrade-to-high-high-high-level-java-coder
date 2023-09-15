## defineProps的使用

取代新特性的props，在子组件中接收父组件中传递的数据

```vue
<template>

  child

  {{ppp.age}}

  {{age}} <!-- ppp是可以省略的 -->
</template>

<script setup>
import {ref} from 'vue'

let ppp = defineProps(['age']) //用于接收，父组件传递的数据age

</script>
```