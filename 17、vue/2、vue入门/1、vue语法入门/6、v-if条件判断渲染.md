## 条件判断渲染

之前我们讲过v-if的使用



在很多情况下不会仅仅使用v-if，而是会使用如下的

```html
<div id="app">

    是否显示： <span v-if="a">a</span>
    <span v-else-if="b">b</span>
    <span v-else>c</span>
</div>
```

