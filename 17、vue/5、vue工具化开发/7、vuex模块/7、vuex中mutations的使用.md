## mutations的使用

> 修改state中属性的方法



只要去看 4、使用vuex做一个全局计数器  这篇就懂了



补充一点：

mutations也是有辅助函数的mapMutations

在 4、使用vuex做一个全局计数器上进行修改，演示辅助函数mapMutations

```vue
<template>

  <div>
    <input type="text" v-bind:value="ccc" @input="addcount" />
    {{ccc}}
  </div>

</template>

<script>
import {mapState, mapMutations} from "vuex";

export default {


  //新增一个计算属性，名称为ccc，数据来自store中的state中的counter
  computed: {
    age: function (){
      return 10
    },
    ...mapState({
       ccc: state => state.counter //这行可以简写，ccc: counter，counter直接可以拿到state中的数据，也可以写成如下
      // ccc(state){
      //   return state.counter
      //}




      //这儿还可以继续写 xxx: yyy来添加对应的属性
    }),
  },


  methods: {
    //使用辅助函数的方式
    ...mapMutations({
      addcount: 'add' //addcount为本组件的方法名，'add'为store中的方法名
    })
  }





}

</script>
```

