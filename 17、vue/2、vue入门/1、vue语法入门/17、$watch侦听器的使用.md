## $watch的使用

整体的功能跟watch是一样的，不过就是名字不同而已。



- 普通监听

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>

<body>

<div id="app">
   
    <input type="tex" v-model="info.age" />

</div>

<script>

    // 声明一个选项对象
    const App = {

        data: function(){
            return {
                info: {
                    age: 20,
                    height: 30
                }
            }
        },
        created: function(){
            
            //返回一个取消监听的函数
            const unwatch = this.$watch("info.age", (newval,  olgval)=>{//第一个参数是要监听的属性，第二个参数是一个箭头函数
                console.log(newval + "+" + olgval)
            })

            //经过一系列操作后，执行取消监听的函数
            unwatch()
        },
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")

</script>

</body>
</html>

```



- 深度监听

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>

<body>

<div id="app">
   
    <input type="tex" v-model="info.age" />

</div>

<script>

    // 声明一个选项对象
    const App = {

        data: function(){
            return {
                info: {
                    age: 20,
                    height: 30
                }
            }
        },
        created: function(){
            
            //返回一个取消监听的函数
            const unwatch = this.$watch("info", (obj)=>{//第一个参数是要监听的属性，第二个参数是一个箭头函数
                console.log(obj)
            },{ //这儿是第三个参数，深度监听
                deep: true
            })

            //经过一系列操作后，执行取消监听的函数
            // unwatch()
        },
    }

    // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
    const vm = Vue.createApp(App).mount("#app")

</script>

</body>
</html>

```

