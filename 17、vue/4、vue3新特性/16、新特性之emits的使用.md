## emit的使用

将子组件的事件传递给父组件执行



子组件中代码如下：

```vue
<template>

  child


</template>

<script>
import {ref} from 'vue'

export default {

  //定义一个需要传递的事件名称
  emits: ['info'],


  setup(props, context){
    //从context中解构得到emit
    let {emit} = context

    //直接把需要传递的事件和参数传递给父组件，一般我们都是子组件中通过事件触发来传递，这儿只是演示如何使用
    emit('info', "需要传递的参数~")
  }


}

</script>

```



父组件中代码如下：

```vue
<template>
  index

  <Child @info="ccccc"></Child> <!-- 使用子组件，触发子组件中的info事件 -->

</template>

<script>

import Child from './Child.vue' //导入子组件

export default {
  components: {Child}, //声明子组件

  //入口
  setup() {

    //定义事件触发的方法
    const ccccc = (message)=>{ //message为子组件中传递过来的数据
      console.log("ccccc" + message)
    }

    return {
      ccccc
    }

  }
}
</script>



```



