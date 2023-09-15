## 不使用vuex存储共享数据的例子



```vue
<template>

  <div>
    <button @click="addCounter">{{counter}}</button>
  </div>

</template>

<script>

export default {

  data: function (){
    return {
      counter: 0
    }
  },

  methods: {
    addCounter: function (){
      this.counter++
    }
  }



}

</script>


<style>



</style>

```

当在此页面上点击按钮，计数器会完成+1 的操作，但是如果切换到了/login页面，再回来，发现计数器被重置了，数据是没有保留的！



那如果需要存储数据，怎么办？

- 在项目中创建一个store目录，然后再store中新建一个index.js，内容如下

  ```js
  const store = {
      state: {
          count: 0
      },
      addone(){
          this.state.count ++
      }
  }
  
  export default store
  
  ```

- 在需要的页面先导入上面的文件

  ```vue
  import store from '../store/index'
  ```

- 在需要使用的地方使用就行

  ```vue
  <template>
  
    <div>
      <button @click="addCounter">{{counter}}</button>
    </div>
  
  </template>
  
  <script>
  import store from '../../store/index'
  export default {
  
  
  
    data: function (){
      return {
        counter: store.state.count //获取到store中的值，赋给页面上的变量
      }
    },
  
    methods: {
      addCounter: function (){ 
        store.addone()//触发点击事件，调用store的方法
        this.counter = store.state.count //将最新的store的值赋给页面上的变量
      }
    }
  
  
  
  }
  
  </script>
  
  ```

  注意，这样使用不能刷新页面，刷新页面store的值也没有了，所以只能做成用<router-link>的点击跳转。