## actions的使用

与mutations的使用一致，只是mutations只能是同步的执行，而actions可以是异步的执行。



使用示例

```js
import {createStore} from "vuex";

export default createStore({
    state: {
        counter: 2098 //初始化一个store的数据
    },
    mutations: {
        add(state){ //编写对state中数据的更改方法，其中的参数是 就是上面的state
            state.counter ++
        }
    },
  	//actions里面的方法
    actions: {
        addact(state){ //编写对state中数据的更改方法，其中的参数是 就是上面的state
            state.commit('add')
        }
    },
    modules: {

    }
})

```

vue中调用actions中的方法

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
    addcount: function (){
      this.$store.dispatch('addact') //!!!!!!!!!看这里，addact对应store的actions中的方法！！！！！！！！！！！！！！
    },
  }





}

</script>
```





actions也有辅助函数**mapActions**，使用略过！

