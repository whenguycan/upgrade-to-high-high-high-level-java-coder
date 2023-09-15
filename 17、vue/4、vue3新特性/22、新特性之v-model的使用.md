## v-model的使用

在vue2中，我们使用v-model，比如：

```vue
<input type="text" v-model="age" />
```

v-model的写法，其实等价于下面

```vue
<input type="text" :value="age" :input="changeValue" />
```

v-model实际上是给当前输入框，设置value值为age，绑定input事件为changeValue。





但是在vue3中的新特性中，可以用于父子组件中的传参



在父组件中代码如下：

```vue
<template>
  index {{money}}

  <br />

	<!-- v-model的写法等价于,如下：
			<Child :modelValue="money" @update:modelValue="changeMoney"></Child> 
			
				v-model做了2件事
				1. 给子组件传递了一个叫modelValue的参数，子组件中可以通过props接收modelValue接收
				2. 定义了一个自定义的事件update:modelValue，在子组件中可以通过emit触发update:modelValue事件
	-->
  <Child v-model="money"></Child>
</template>

<script setup>

import Child from './Child.vue'
import {ref} from "vue"; //导入子组件

let money = ref(10000)

const changeMoney = (mon)=>{ //使用v-model不会触发这个事件的执行
  console.log("xxxx")
  money.value = money.value + mon
}
</script>




```

也可以使用v-model指定传递给子组件中props的属性名和自定义事件的名称

```vue
<template>
  index {{money}}

  <br />

	<!-- 
				v-model:money做了2件事
				1. 给子组件传递了一个叫money的参数，子组件中可以通过props接收money接收
				2. 定义了一个自定义的事件update:money，在子组件中可以通过emit触发update:money事件
	-->
  <Child v-model:money="money"></Child>
</template>

<script setup>

import Child from './Child.vue'
import {ref} from "vue"; //导入子组件

let money = ref(10000)

const changeMoney = (mon)=>{ //使用v-model不会触发这个事件的执行
  console.log("xxxx")
  money.value = money.value + mon
}
</script>





```





在子组件中

```vue
<template>

  child: {{modelValue}}

  <button @click="changeMoney">点击</button>


</template>

<script setup>

const props = defineProps(["modelValue"])

const emits = defineEmits(["update:modelValue"])

const changeMoney = ()=>{
  emits('update:modelValue', props.modelValue+1000) //这儿是触发父组件中的自定义事件，第二个参数就是数据中的新值
}

</script>
```

