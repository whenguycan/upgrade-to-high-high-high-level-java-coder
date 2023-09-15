## 循环遍历渲染



```html
<!-- v-for -->
<ul>
  <li v-for="(value, key) in attrT" v-bind:id="value">{{value}} -- {{key}}</li> <!-- attrT是定义在Vue中的一个属性数组attrR:[1,2,3,4,5] 或属性对象{"xxxx":"xxxx", "yyyy":"yyyy", "cccc":"cccc"} -->
</ul>
```



v-for不能与v-if一起使用！