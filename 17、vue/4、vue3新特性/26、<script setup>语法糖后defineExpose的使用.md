## defineExpose的使用



在没有使用<script setup>的时候，我们是这样写的

```vue
<script>

  export default {
    
    
    setup(){
      
      
      
      
      return {
        
        ....
      }
      
    }
    
  }

</script>
```

在export default中的代码都是被暴露出去的，在本组件外都是可以获取到的，但是使用<script setup>后，没有了export语句，那么在<script setup>中的内容默认是不对外暴露的，只能在本组件内部使用，那么如果需要暴露一些数据出去给别的组件使用，需要怎么办？就需要使用defineExpose了

```vue
<template>

  child: {{money}}

  <slot></slot>


</template>

<script setup>


import {useAttrs, ref, defineExpose} from "vue";

let money = ref(100)

defineExpose({ //需要对外暴露的数据
  money
})

</script>


```

