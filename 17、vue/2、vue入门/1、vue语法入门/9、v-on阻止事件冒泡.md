## v-on阻止事件冒泡



```html
<div id="app">
  <div v-on:click="func()">
    <span v-on:click="func()">点击事件</span> 
  </div>
</div>
```

上面的代码，当点击`点击事件`的时候，会触发2次func()方法，因为事件冒泡了，那么应该如何阻止呢？只需要修改代码即可

```html
<div id="app">
  <div v-on:click="func()">
    <span v-on:click.stop="func()">点击事件</span> 
  </div>
</div>
```

那么再次点击就不会冒泡了！

