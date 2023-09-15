## 登录表单使用elementUI进行数据校验

> element官方参考：https://element-plus.org/zh-CN/component/form.html#%E8%A1%A8%E5%8D%95%E6%A0%A1%E9%AA%8C



#### 第一步：修改我们之前组件中的表单，改成elementUI提供的表单标签

```vue
<template>

		<!-- 改造的内容在这儿 -->
    <el-form>
				
        <el-form-item>
            用户名: <el-input type="text" name="username" v-model="username"/>
        </el-form-item>

        <el-form-item>
            密码： <el-input type="text" name="password" v-model="password"/>
        </el-form-item>

        <el-form-item>

            <el-button type="primary"  value="登录" @click="loginFunc" >登录 </el-button>

        </el-form-item>

    </el-form>
		<!-- 改造的内容在这儿 -->



</template>

<script setup lang="ts">

import login from '@/apis/user/login.ts'

import mainStore  from '@/store/index.ts'

import { useRouter } from 'vue-router';


import { ElNotification } from 'element-plus'


let $router = useRouter();

const mainStoreInstance = mainStore()

let username = "czadmin";
let password = "Czdx@123";

let loginFunc = () => { 



    
    console.log(username);
    console.log(password);

    let param = {
        userName: username,
        password
    }

    // try{

        mainStoreInstance.doLogin(param).then(()=>{
            $router.push("/home")
        }).catch((data)=>{
            ElNotification({
                type: "error",
                message: data.message
            })
        }) //异步调用pinia中的方法
        
        
    // }catch(error){ //捕获上面方法的异常

    //     console.log(error.message)

    // }
    

}

</script>
```





#### 第二步：编写表单校验

- 在el-form标签中，添加 :model="xxxx"，意思是将表单数据统一收集到xxxx对象中，这个xxxx对象是需要被reactive定义的

  ```vue
  <script setup lang="ts">
  
    const xxxx = reactive({
      
      username: "",
      password: ""
      
    })
  
  </script>
  ```

- 在el-form标签中，添加 :rules="yyyy"，意思就是将表单收集到xxxx对象中的数据使用 :rules 定义的规则进行校验，rules具体的规则需要自己开发

  ```vue
  <script setup lang="ts">
  
    const xxxx = reactive({
      
      username: "",
      password: ""
      
    })
    
    const yyyy = {
      
       username: [
          {required: true, message: "用户名必填", trigger: 'blur'}, //required为必填，message是触发错误的提示，trigger是指什么时候触发校验规则
          {required: true, min: 6, max: 10, message: "用户名只能是6-10个字符", trigger: 'blur'}
      ],
      
      password: [ //针对password属性的校验规则
        
        
      ]
      
    }
  
  </script>
  ```

- 到el-form-item中，添加 prop="username/password"，prop中的值就是你当前输入的数据最终会使用rules中的哪个属性的规则去进行校验

​		



#### 第三步：表单校验通过了才能发axios请求

- 给el-form标签添加 ref="loginFormRef"

- 使用ref获取到这个el-form标签

  ```vue
  const loginFormRef = ref();
  ```

- 保证表单项的校验都通过了再发送请求

  ```typescript
  console.log(loginFormRef.value);//打印根据ref标签获取到的对应的表情属性,其中有validate方法，返回一个Promise对象，根据Promise对象的结果去判断需不需要继续发axios请求
  
  
  //所以最终会这样用
  loginFormRef.value.validate().then(()=>{
    
    
    //继续发送axios请求......
    
    
  })
  ```
  
  

