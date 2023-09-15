## ref与$refs的使用

当我们在写代码的过程中，可能需要去操作某一个DOM元素，在原本的js中我们可以通过document.getElementById的方式获取，那么在vue中我们如何快速获取呢？

可以在写具体的标签中使用ref去注册标签，然后在vue中使用$refs获取到所有注册的标签。



- 在同一个组件中操作

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>

<body>

<div id="app">
    <div ref="div">123</div> <!-- 注册div标签到vue中 -->
</div>

<script>

    // 声明一个选项对象
    const App = {
       
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")
    console.log(vm.$refs.div)  //这样就能获取到对应的DOM标签了！

</script>

</body>
</html>

```



- 子组件注册的ref，如何在父组件中获取？

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>

<body>

<div id="app">
   
    <div ref="div">123</div> <!-- 使用ref注册div标签到vue中 -->
    <myzujian ref="child"></myzujian> <!-- 这个也要使用ref注册到vue中 -->

</div>

<script>

    // 声明一个选项对象
    const App = {
        components: {
            myzujian: {
                template: `
                    <div ref="zizizi">zizujian</div>
                `
            }
        }
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")
    console.log(vm.$refs.child.$refs.zizizi)  //先获取到child的标签，然后再获取到子组件的$refs属性

</script>

</body>
</html>

```

