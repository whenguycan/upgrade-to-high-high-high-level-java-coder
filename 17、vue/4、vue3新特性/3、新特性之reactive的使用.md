## reavtive的使用

将对象数据包装为响应式数据。



在不使用新特性的时候，我们如下操作

```vue
<template>
  index

  <input type="text" v-model="info.age" />
  {{info.age}}
</template>

<script>


export default {

  data: function (){
    return {
      info: {
        age:20,
        height:20
      }
    }
  }

}


</script>

```

在输入框中修改，{{info.age}}的显示是响应式的，没问题。



但是在vite中使用setup()做为入口，再试试呢？还能用么？

```vue
<template>
  index

  <input type="text" v-model="info.age" />
  {{info.age}}
</template>

<script>


export default {

  setup(){

    const info = {
      age: 20,
      height: 1.88
    }

    return {
      info
    }
  }

}


</script>

```

是不是发现没有效果了？

那么应该怎么修改，才能让setup中定的对象属性有响应式效果呢？

```vue
<template>
  index

  <input type="text" v-model="info.age" />
  {{info.age}}
</template>

<script>
import {reactive} from "vue";

export default {

  setup(){

    const info = reactive({
      age: 20,
      height: 1.88
    })

    return {
      info
    }
  }

}


</script>

```

