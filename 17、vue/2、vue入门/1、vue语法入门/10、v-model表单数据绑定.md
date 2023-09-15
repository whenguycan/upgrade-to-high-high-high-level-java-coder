## v-model表单数据绑定

```html
<!-- v-model -->
<input type="text" v-model="val">
<input type="radio" name="chk" v-model="stat" value="eat">
<input type="radio" name="" v-model="stat" value="ddd">

<!-- 
v-model只能用在用户能够输入或修改的表单元素上，如：input、textarea、select、checkbox等
v-model是控制表单元素内的值，所以可以直接写成v-model="xxxx"等价于 v-moedl:value="xxxx"

v-model修饰在checkbox、radio内是控制有没有被选中，即checked属性

v-model如果使用在上述标签中，用户修改标签内的值，会同时修改Vue中用户定义的数据值，

而v-bind绑定的值你在页面上修改，是不会修改自定义属性的值的
-->
{{stat}} <!-- 如果上面的stat是在radio中，那么在Vue中定义stat为[]或者''，stat都是接收的radio的value值
如果上面的stat是在checkbox中，Vue中如果定义stat为一个数组即[]，stat都是接收的所有选中的checkbox的value值，如果在Vue中定义stat为一个空字符串或者是true/false，那么就只能接收checkbox的状态
-->



 <input type="text" v-model.lazy="txt"/> <!--  在“change”事件时而非“input”事件时更新txt的数据 -->
{{txt}}

<input type="text" v-model.trim="txt2"/> <!--  自动过滤用户输入的首尾空白字符保存到txt中 -->
{{txt2}}
```

