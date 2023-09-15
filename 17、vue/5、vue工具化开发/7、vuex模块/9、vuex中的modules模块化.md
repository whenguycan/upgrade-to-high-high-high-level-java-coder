## modules模块化

就是利用模块化，可以将state、actions、mutations放到各自的模块里去，不会引起冲突

```js
const moduleA = { //当然，这部分也可以放到另外的文件中，然后在这儿进行导入
  namespace: true,
  state: () => ({ ... }),
  mutations: { ... },
  actions: { ... },
  getters: { ... }
}

const moduleB = {
  namespace: true,
  state: () => ({ ... }),
  mutations: { ... },
  actions: { ... }
}

const store = createStore({
  modules: {
    a: moduleA,
    b: moduleB
  }
})
```



在vue中调用的时候

- 方案1

```vue
{{$store.state.模块名称.xxx}} //调用state


this.$store.dispatch("模块名称/方法名称")//调用actions中的方法


this.$store.commit("模块名称/方法名称")//调用mutations中的方法


this.$store.gettters["模块名称/方法名称"]//调用getters
```

