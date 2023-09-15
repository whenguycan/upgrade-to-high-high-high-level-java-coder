## 登录成功token写入pinia，登录失败给出错误提示



因为axios的get、post等方法都是异步执行的方法会返回Promise对象，所以actions中的方法只有是异步的才能把axios的get、post方法执行的返回值的promise对象进行传递



#### 使用如下：

vue中调用pinia中的actions中的一个方法，并获取到方法返回值

```vue
<template>

用户名: <input type="text" name="username" v-model="username"/>
<br />

密码： <input type="text" name="password" v-model="password"/>

<br />

<input type="button"  value="登录" @click="loginFunc">

</template>

<script setup lang="ts">

import login from '@/apis/user/login.ts'

import mainStore  from '@/store/index.ts'

import { useRouter } from 'vue-router';

let $router = useRouter();

const mainStoreInstance = mainStore()

let username = "czadmin";
let password = "Czdx@123";

let loginFunc = () => { //发起异步调用

    
    console.log(username);
    console.log(password);

    let param = {
        userName: username,
        password
    }

    mainStoreInstance.doLogin(param).then(()=>{//异步调用pinia中的方法
      console.log("success")
      
      $router.push("/home")//调用编程式导航，进行登录成功跳转
      
    }).catch( (data)=>{
      console.log(data.message)
      
      //调用elementUI的错误提示页面，给出错误提示
    }) 
}

</script>
```



pinia中的存储代码如下：

````typescript
import { defineStore } from "pinia";

import login from '@/apis/user/login.ts'
import {loginUserData} from '@/apis/user/type.ts';

const mainStore = defineStore("main", { //定义一个store容器，main是容器的名字

    state: ()=>{ 
        return {
            token: ""
        }
    },

    actions: { //用于定义对于存储数据修改的methods方法
        async doLogin(param: loginUserData){  // 被调用的方法，这儿一定要是被async修饰的异步方法，才能返回Promise对象
            await login(param).then((response)=>{ //login方法是封装好的axios请求方法
                const { status, data } = response;
                if(status === 200){
                    if(data.code === 200){
                      	this.token = data.msg
                        localStorage.setItem("TOKEN", data.msg);
                    }else{
                        return Promise.reject(new Error(data.msg)); //返回一个reject的Promise
                    }
                }else{
                  return Promise.reject(new Error(data.msg));
                }
            })
        }
    }
    

});

export default mainStore; //导出容器给组件使用
````

