## vuex的安装



在项目根目录，运行 npm install vuex@next

验证是否安装成功，只要去项目的package.json中查看，dependencies部分有没有vuex及版本就行。



在项目src目录创建store目录，然后再新建一个index.js，内容如下

```js
import {createStore} from "vuex";

export default createStore({
    state: {

    },
    mutations: {

    },
    actions: {

    },
    modules: {
        
    }
})

```



上面代码中`state`、`mutations`、`actions`、`modules`都是什么作用？

![avatar](../../images/vuex.png)



actions是通过想后端API获取数据，然后把获取到的数据交给mutations，然后mutations再去修改state中的数据或者状态，各个组件拿到state中的数据或状态去渲染页面。



