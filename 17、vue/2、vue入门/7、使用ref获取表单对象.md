## 使用ref获取表单对象

>  任意标签都可以使用ref，给标签记上ref属性，可以理解为给标签节点起了个名字。然后，通过vue实例的$refs属性拿到这个dom元素

#### 新建一个表单

```html
<template>
  <div>
    <form v-bind="formstate" ref="myForm" name="xxx">
      用户名：<input type="text" name="username" v-model="formstate.username" @blur="kk()"/>
      <br/>
      密码：<input type="text" name="password" v-model="formstate.password"/>
      <br />
      <input type="button" value="提交" @click="submit()">
    </form>
  </div>
</template>
```





```javascript
<script>

import {reactive} from 'vue'

export default {


  data:function(){ //定义组件内需要的属性

    //建立一个双向绑定数据池，就可以在template和下面的js中使用了，注意form标签上需要绑定下
    const formstate = reactive({
      username: 'tangwei',
      password: '123456'
    });

    return {
      formstate
    }
  },
  methods:{
    kk:function (){
      console.log(this.formstate.username) //这儿可以直接获取到数据
    },
    submit: function (){
      console.log(this.$refs.myForm.name) //获取被ref标记的标签，然后就可以获取到信息
      console.log(this.formstate.username)
      console.log(this.formstate.password)
    }
  }
}
</script>
```

