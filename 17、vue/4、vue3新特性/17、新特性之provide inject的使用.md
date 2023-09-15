## provide inject的使用

上面第13课，我们创建了一个Index.vue组件，还创建了一个Child.vue组件，然后使用props进行了Index组件与Child组件之间的值传递。那么此时有面临一个问题，Index组件、Child组件、与App.vue组件，形成了一个组件链。App组件是爷爷组件、Index组件为父组件、Child组件为孙组件，那么如果有一个数据是需要从爷爷组件(App组件)往下传递的，那么我们就需要使用props一层层写下来去接收，如果父组件不止Index组件这一个，那么是不是需要写很多props？provide.inject就是为了解决这个问题！





#### 仅仅暴露数据

在爷爷组件中，使用provide暴露数据

```vue
<script>
import {provide, readonly} from "vue";

export default {

  setup(){
    const title = "xxxx"
    
    provide('title', readonly(title)) //爷爷组件中暴露数据，且在子组件中是只读的，无权修改数据
  }

}

</script>
```

只要在爷爷节点把数据做成响应式的，通过provide传递出去的也是响应式的数据。



在父、孙子、重孙子、曾孙子，只要想用的地方

```vue
<template>

  child

  {{title}}
</template>

<script>
import {ref, inject} from 'vue'

export default {



  setup(props, context){
    const title = inject('title') //从爷爷组件中获取数据

    return {
      title
    }
  }


}

</script>

```





#### 暴露数据的同时也暴露一个方法

在爷爷组件中，使用provide暴露数据和方法

```vue
<script>

import HelloWorld from './components/HelloWorld.vue'
import {ref, provide, readonly} from "vue";
export default {



  setup(){
    const title = ref("xxxxx")
    provide('title', readonly(title)) //确保在子组件中是只读的，只能通过爷爷组件暴露的方法才能修改

    const changeTitle = ()=>{
      title.value = "yyyy"
    }

    provide("changeTitle", changeTitle) //把方法也传递出去
    
  }
}
</script>
```

在父、孙子、重孙子、曾孙子，只要想用的地方

```vue
<template>

  child

  <button @click="changeTitle">点击</button> <!-- 点击触发爷爷组件中传递过来的方法 -->
  {{title}}
</template>

<script>
import {ref, inject} from 'vue'

export default {



  setup(props, context){
    const title = inject('title')

    const changeTitle = inject("changeTitle")//获取到爷爷组件中的方法

    return {
      title,
      changeTitle
    }
  }


}

</script>

```

