## 在DOM节点上使用ref

在DOM节点上使用ref，可以修改这个节点的信息，也可以获取到节点暴露出来的数据和方法。



使用范例

- 操作普通标签

  ```vue
  <template>
  
    child
  
    <h3 ref="root">xxxxx</h3> <!-- ref="root" root就是来自setup中返回的 -->
  </template>
  
  <script>
  import {ref, onMounted} from 'vue'
  
  export default {
  
    setup(){
  
      const root = ref(null) //获取到上面<h3 ref="root">xxxxx</h3>的DOM节点
  
      onMounted(()=>{
        console.log(root.value.innerHTML) //root.value获取到DOM节点对象，然后才可以调用.innerHTML等属性
      })
  
      return { //一定要返回
        root
      }
    }
  
  
  }
  
  </script>
  
  ```

- 操作组件标签

  ```vue
  <template>
    index
  
    <Child ref="child"></Child> <!-- 使用子组件 -->
  
  </template>
  
  <script>
  
  import Child from './Child.vue' //导入子组件
  import {onMounted, ref} from 'vue'
  export default {
    components: {Child}, //声明子组件
  
    //入口
    setup() {
  
      const child = ref(null) //这儿的child一定要跟子组件的标签名一样，否则拿不到
       onMounted(()=>{
         console.log(child.value) //这儿就能获取到整个子组件的代理对象
         //通过child.value.xxx 可以拿到子组件暴露的数据
       })
  
      return {
        child
      }
    }
  }
  </script>
  
  
  
  ```
  
  



