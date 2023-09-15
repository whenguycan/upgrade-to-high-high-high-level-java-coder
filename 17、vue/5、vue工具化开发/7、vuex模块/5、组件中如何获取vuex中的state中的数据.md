## 组件中如何获取vuex中的state中的数据



#### 1、可以使用使用如下的方法

```vue
{{$route.state.xxxxx}}
```





#### 2、使用vuex提供的`mapState`辅助函数

```vue
<template>

  <div>
    <input type="text" v-model="ccc" />
        {{ccc}}
  </div>

</template>

<script>
import {mapState} from "vuex";

export default {


  //新增一个计算属性，名称为ccc，数据来自store中的state中的counter
  computed: mapState({
    ccc: state => state.counter
  })
  
}

</script>
```

但是上面这样做了，发现修改输入框中的数据，store中的数据不会改变，怎么办？

所以需要修改成如下的样子：

```vue
<template>

  <div>
    <input type="text" v-bind:value="ccc" @input="addcount" />
    {{ccc}}
  </div>

</template>

<script>
import {mapState} from "vuex"; //引入vuex中的mapState

export default {


  //新增一个计算属性，名称为ccc，数据来自store中的state中的counter
  computed: mapState({
    ccc: state => state.counter //这行可以简写，ccc: counter，counter直接可以拿到state中的数据，也可以写成如下 
    // ccc(state){
    //   return state.counter
  	//}
    
    
    
    
    //这儿还可以继续写 xxx: yyy来添加对应的属性
  }),

  methods: {
    addcount: function (){
      this.$store.commit('add') //add对应store的mutations中的方法
    }
  }

}

</script>

```



注意：

如果在computed中使用了mapState，那么普通属性要和mapState共用computed，怎么办？

```vue
<template>

  <div>
    <input type="text" v-bind:value="ccc" @input="addcount" />
    {{ccc}}
  </div>

</template>

<script>
import {mapState} from "vuex";

export default {


  computed: {
    //在computed中跟别的属性一起使用
    age: function (){
      return 10
    },
    ...mapState({ //看这儿！！！！！！！！！！！！！！！！！！！！，需要在mapState前加 “...”，千万记住
       ccc: state => state.counter 
    }),
  },


  methods: {
    addcount: function (){
      this.$store.commit('add') //add对应store的mutations中的方法
    }
  }





}

</script>
```



