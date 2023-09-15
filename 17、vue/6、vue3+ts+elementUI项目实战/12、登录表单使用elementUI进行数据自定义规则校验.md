## 对表单数据进行自定义规则校验

> elementUI官方参考：https://element-plus.org/zh-CN/component/form.html#%E8%87%AA%E5%AE%9A%E4%B9%89%E6%A0%A1%E9%AA%8C%E8%A7%84%E5%88%99



相比普通校验（使用elementUI自带的校验规则进行校验）有很多无法满足我们对数据的校验的地方，比如密码需要大小写、包含字母数字等，用elementUI自动的规则无法实现校验，那么就需要我们自定义一些规则。



用法跟普通校验一致，只是在定义校验的规则的时候，需要我们自己去定义规则，不能使用elementUI自带的规则了。





#### 自定义规则的流程

- 修改校验规则

  ```typescript
  const checkLoginForm = {
      username: [
          {trigger:"blur", validator: validateUsername} //不再使用elementUI的校验规则，而是使用validator指定我们自己的校验函数为validateUsername
      ],
      password: [
  
  
      ]
  }
  ```

- 具体的校验规则

  ```typescript
  const validateUsername = (rule:any, value:any, callback: any)=>{
      // value为表单元素的值。
      // callback为校验通过的回调，如果符合校验规则则需要放行，如果校验不通过则需要给出错误信息
  
      if(value == "123"){
          callback();//放行
      }else{
          callback(new Error("错误信息"))//这儿的new Error("xxxx")就是相当于elementUI中自带校验规则的message
      }
  }
  ```

  