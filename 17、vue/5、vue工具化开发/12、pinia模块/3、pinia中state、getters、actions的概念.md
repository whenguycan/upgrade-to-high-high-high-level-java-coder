## pinia中state、getters、actions的概念



#### 1、state用于存储全局的数据

```typescript
{
  
  state: ()=>{
    
    return {
      token: "" //一定要return这个定义好的token属性
    }
  }
  
}
```





#### 2、actions是存放修改state中数据的方法

> 类似原生vue属性的methods

```typescript
{
  state: ()=>{
    
    return {
      token: "" //一定要return这个定义好的token属性
    }
  },
    
  actions: {
    
    userLogin(data){ //定义一个方法，可以去修改state中的属性
      
      this.token="token"
      
      
    }
    
  }
  
}
```



#### 3、getters是存放修改state中数据到的方法

> 类似原生vue属性的computed，多次调用只会执行一次

```typescript
{
  state: ()=>{
    
    return {
      token: "" //一定要return这个定义好的token属性
    }
  },
    
  getters: {
    
    userLogin(state){ //定义一个方法，可以去修改state中的属性，参数state是整个state
      
      return state.token = "xxxx"
      
      
    }
    
  }
  
}
```

