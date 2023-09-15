## defineEmit的使用



取代emits，在子组件中传递事件到父组件的时候使用

```vue
<template>

  child


</template>

<script setup>
import {ref} from 'vue'

const emit = defineEmits(['info'])

emit('info', "需要传递的参数~")

</script>


```

