## 统一处理state中的数据

> vuex官方叫 state派生



```js
import {createStore} from "vuex";

export default createStore({
    state: {
        counter: 2098 //初始化一个store的数据
    },
    //state的派生!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!看这儿！！！！！！！！！！！！
    getters: {
        plusone: function (state){ //起一个随机名称的方法
            return "[" + state.counter + "]"
        }
    },
    mutations: {
        add(state){ //编写对state中数据的更改方法，其中的参数是 就是上面的state
            state.counter ++
        }
    },
    actions: {

    },
    modules: {

    }
})

```

然后再组件中调用

```vue
{{$route.getters.plusone}}
```





getters也有辅助函数mapGetters，跟state一样使用！！这儿不讲了！