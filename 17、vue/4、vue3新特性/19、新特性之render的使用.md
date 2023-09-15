## render的使用

> 只需要了解下，一般不用



在我们编写的组件中，html的代码，一般都写在<template></template>中的，而如果想不写在这个标签中的话，就需要使用render了！



使用范例

```vue
<script>
import {ref, onMounted, h} from 'vue'
import Child from '../views/Child.vue'
  
export default {
  
  components: {Child},

  setup(){
   
    return ()=>{
      return h('div', {}, [ //div为渲染出的标签为div，然后有下面的子标签
          h("h3", "xxxxx") //子标签为h3，h3标签的内容为xxxxx
        	h(Child, {version:"xxxx"}) //Child是子组件的名称，需要先import再声明才能用，{version:"xxxx"}是需要传递的props的数据
      ])
    }


  }


}

</script>

```

