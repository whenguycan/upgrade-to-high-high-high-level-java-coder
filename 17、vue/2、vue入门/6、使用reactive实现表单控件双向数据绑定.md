## 使用双向数据绑定



#### 使用reactive定义双向数据绑定变量

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
      formstate,
    }
  },
  methods:{
    kk:function (){
      console.log(this.formstate.username) //这儿可以直接获取到数据
    },
    submit: function (){
      console.log(this.formstate.username)
      console.log(this.formstate.password)
    }
  }
}
</script>
```

#### 在template需要的地方使用

```html
<template>
  <div>
    <form v-bind="formstate">
      用户名：<input type="text" name="username" v-model="formstate.username" @blur="kk()"/>
      <br/>
      密码：<input type="text" name="password" v-model="formstate.password"/>
      <br />
      <input type="button" value="提交" @click="submit()">
    </form>
  </div>
</template>
```



