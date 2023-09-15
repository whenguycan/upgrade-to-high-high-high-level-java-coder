## <script setup>语法糖



我们之前写的代码里面都是如下的样子

```vue
<template>

</template>

<script>

  export default {
    
    
    setup(){
      
      
      //这儿写各种代码
      
      
    }
    
  }

</script>
```

如果使用<script setup>后，上面的代码可以被简化成如下的样子

```vue
<script setup>

	//这儿写各种代码
  
</script>
```



使用<script setup>后，vue会自动帮我们把代码编译成上面的样子，使用<script setup>有什么好处呢？

1. 在其中的声明的函数、变量等不需要再写return，可以在模板中直接使用。

2. import的组件，可以在模板中使用，不用再次声明一次。

   ```vue
   <script setup>
   import MyComponent from './MyComponent.vue'
   </script>
   
   <template>
     <MyComponent />
   </template>
   ```

   

3. 自定义指令的使用

   必须以 `vxxxxDirective` 的形式来命名本地自定义指令，以使得它们可以直接在模板中使用

   ```vue
   <script setup>
   const vMyDirective = { //定义自定义指令，名称为vMyDirective
     beforeMount: (el) => {
       // 在元素上做些操作
     }
   }
   </script>
   <template>
     <h1 v-my-directive>This is a Heading</h1>
   </template>
   ```

   

   