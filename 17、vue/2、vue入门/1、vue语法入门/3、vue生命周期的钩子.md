## 页面渲染之前的数据初始化操作

所谓的钩子，就是在Vue渲染页面的整个生命周期中，通过Vue暴露出来了很多个接口函数供调用，以便在开发的时候可以使用。

比如：页面渲染前钩子`created`等等。



`beforeMount`是渲染载入页面之前调用

`mounted`是渲染载入页面之后调用

`beforeUpdate`此时页面中的数据是旧的，但是data中的数据是最新的，且页面并未和最新的数据同步

`updated`此时页面的数据已经和data中的数据同步了

`beforeDestroy`在执行这个钩子之前，实例中的data、method、computed等都是可用状态，并未真正执行销毁

`destroyed`此时组件已经完全被销毁，data、methods、computed等已经不可用了

`activated`用于重复激活一个组件的时候触发

`deactivated` 实例没有被激活时

`errorCaptured` 当后台组件中的错误被父组件捕获后触发。



官网参考：https://cn.vuejs.org/guide/essentials/lifecycle.html#lifecycle-diagram



```html
<!DOCTYPE html>
<html lang="en">

<head>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>

<body>

<div id="app">
   
    {{counter}}

</div>

<script>

    // 声明一个选项对象
    const App = {
        //初始数据
        data: function (){
            return {
                counter: 0, //这儿就是需要绑定的数据
            }
        },
        //页面渲染之前的初始化操作
        created: function(){
            this.counter = 1000
          
          //这儿的this就是下面的vm。
        },
      
      	//页面被渲染后执行
      	mounted: function(){
          
          
        }
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")

</script>

</body>
</html>

```

