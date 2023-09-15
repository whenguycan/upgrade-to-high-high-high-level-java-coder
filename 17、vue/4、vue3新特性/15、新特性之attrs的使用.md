## attrs的使用

attrs是从setup(props, context)的第二个参数context中解构得来的，用于接收父组件在调用子组件标签中传递的除props的数据。

attrs本身是一个响应式的数据，但是attrs中的数据都是非响应式的。



父组件传递数据到子组件的attrs中

```vue
<template>
  index

  <Child :age="10" name="child" gernder="男"></Child> <!-- 使用子组件，将name=child gernder=男 传递到子组件的attrs中-->

</template>

<script>

import Child from './Child.vue' //导入子组件

export default {
  components: {Child}, //声明子组件

  //入口
  setup() {



  }
}
</script>




```





子组件接收attrs参数

```vue
<template>

  child

  {{age}}


</template>

<script>
import {ref} from 'vue'

export default {

  props: {
    age: Number
  },

  setup(props, context){ //第二个参数context
    let {attrs} = context //从context中解构得到attrs对象

    console.log(attrs.name) //获取到传递的name
    console.log(attrs.gernder) //获取到传递的gernder

  }


}

</script>

```



