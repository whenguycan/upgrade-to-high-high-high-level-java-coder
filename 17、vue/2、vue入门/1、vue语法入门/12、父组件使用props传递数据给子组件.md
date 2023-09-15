## 组件中的数据传递

> 注意： 
>
> 1. 父组件传递到子组件的值，子组件是没有权限进行修改的！
> 2. 要使用props中的属性，可以通过this去调用。
>
> 



#### 1、数据的传递

- 传递动态数据

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
    <!-- 在调用组件的时候将数据传递过去 content就是父组件中的属性 -->
    <zujianname v-bind:va="content"></zujianname> <!-- 调用一下这个组件 -->
</div>

<script>
    // 声明一个选项对象
    const App = {
        data: function (){
            return {
                content: "父组件"  //父组件定义的数据
            }
        },
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
                    }
                },

                props: ['va'], //这儿接收了来自组件调用的参数传递，就是父组件content对应的值数据，注意一定要用引号，写成'xxxx'

                // 模板代码
                template: `
                  <h1 @click="func()">{{va}}{{counter}}</h1> <!--这儿调用{{va}} -->
                `
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





- 传递静态数据

  - 传递普通数据

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
        <!-- 在调用组件的时候将数据传递过去,注意传递进去的知识一个静态熟悉性，值为200 -->
        <zujianname v-bind:va="200"></zujianname> <!-- 调用一下这个组件 -->
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
                        }
                    },
    
                    props: { //这儿接收了来自调用的参数传递，规定需要接收参数的参数类型
                        va: Number
                    },
    
                    // 模板代码
                    template: `
                      <h1 @click="func()">{{va}}{{counter}}</h1> <!--这儿调用{{va}} -->
                    `
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

  - 传递对象数据

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
        <!-- 在调用组件的时候将数据传递过去，注意这儿传递的是一个对象 -->
        <zujianname v-bind:va="{name: 'tangwei'}"></zujianname> <!-- 调用一下这个组件 -->
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
                        }
                    },
    
                    props: { //这儿接收了来自调用的参数传递，规定传递数据是一个对象类型
                        va: Object
                    },
    
                    // 模板代码
                    template: `
                      <h1 @click="func()">{{va.name}}{{counter}}</h1> <!--这儿调用{{va}} -->
                    `
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





#### 2、传递之后数据校验

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
    <zujianname v-bind:va="{name: 'tangwei'}"></zujianname> <!-- 调用一下这个组件 -->
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
                    }
                },

                props: { //这儿接收了来自调用的参数传递
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

                // 模板代码
                template: `
                  <h1 @click="func()">{{va.name}}{{counter}}</h1> <!--这儿调用{{va}} -->
                `
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



