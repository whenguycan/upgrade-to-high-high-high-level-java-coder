## props的使用

子组件接收到的props的数据是只读的。



子组件中使用props接收来自父组件的传值

```vue
<template>

  child

  {{age}}


</template>

<script>
import {ref} from 'vue'

export default {

  props: {
    age: Number //使用props接收来自父组件的传值
  },

  setup(){



  }


}

</script>

```

在子组件的setup(){....}中，setup()中可以获取到父组件传递过来的props，即可以这样写setup(props){......}，代码如下

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

  setup(props){
    console.log(props.age)


  }


}

</script>

```







父组件中传值

```vue
<template>
  index

  <Child :age="10"></Child> <!-- 使用子组件,，并往子组件传值, 使用:age是传递动态数据， 不带“:”传递静态数据-->

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

