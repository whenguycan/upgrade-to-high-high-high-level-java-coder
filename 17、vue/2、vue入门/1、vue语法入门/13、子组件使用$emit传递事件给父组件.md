## 子组件传递事件给父组件



看如下代码中的注释，标注的步骤去操作：

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <style>
        .red {
            color: red;
        }
    </style>
</head>
<body>

<div id="app">
    <!-- 在调用组件的时候将数据传递过去 -->
    <zujianname v-bind:va="{name: 'tangwei'}" v-on:click-event="parentFn"></zujianname> <!-- 第2步，在子组件调用的时候绑定上子组件中emits定义的事件--> 
</div>

<script>
    // 声明一个选项对象
    const App = {
        //定义一个全局组件
        components: {
            zujianname: {
                data: function (){
                    return {
                        counter: 100
                    }
                },
                methods:{
                    func: function (){
                        return this.counter++
                    },
                    childClick: function(){
                        this.$emit("click-event", "这儿可以传参")  //第4步，在子组件中触发自定义的点击事件
                    }
                },

                props: { //这儿接收了来自调用的参数传递，注意一定要用引号，写成'xxxx'
                    va: {
                        type: Object,
                        validator(value){ //value是接收传递的参数，然后进行校验
                            if(value.name == "tangwei"){
                                return true;
                            }
                            return false;
                        }
                    }
                },

                emits: ["click-event"],   //第1步，在子组件中定义一个事件

                // 模板代码
                template: `
                  <h1>{{va.name}}{{counter}}</h1> 
                  <br />
                  <button @click="childClick">点击</button>   <!-- 第3步，在子组件的模板中写自己的点击事件 -->
                `
            }
        },
        methods: {
            parentFn:function(value){ //第5步，父组件触发点击事件，value是接收子组件的传参的！
                console.log("xxxxxxxx");
                console.log(value);
            }
        }
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App)
    vm.mount("#app")
</script>

</body>
</html>

```

