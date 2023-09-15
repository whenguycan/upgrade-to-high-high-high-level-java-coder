## computed关键字的使用



如果在代码中，data中没有指定的属性，我需要给系统增加一个属性，就需要使用`computed`关键字，大致使用如下：

```javascript
<script>
    // 声明一个选项对象
    const App = {
        //初始数据
        data: function (){
            return {
                showOrHide: false,
                age: 1,
                height: 183
            }
        },
        methods: {
            func: function (){
                this.age += 1
            }
        },
        computed: {
          	//xxx就是一个新增的属性
            xxx: function (){
                return this.age + this.height //一定需要return
            }
        }
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")
    vm.counter = 200
</script>
```

然后再html中这样写：

```html
<div id="app">
    v-on: <span @click="func">点击事件</span>
    Computed：<span>{{xxx}}</span>
</div>
```



