## 使用pinia存储数据



#### 在项目src目录中新建store目录，然后再建index.ts文件，写入如下内容

```typescript
import { defineStore } from "pinia";

const mainStore = defineStore("main", { //定义一个store容器，main是容器的名字，这个名字要保证全局唯一，返回的是一个函数

    state: ()=>{ //state用于存储数据，必须是箭头函数
        return {
            count: 100
        }
    },

    getters: { //用于定义对于存储数据修改的computed方法，多次调用只会执行一次
        changeCount(){
          this.count ++ 
        }
    },

    actions: { //用于定义对于存储数据修改的methods方法
			
      changeCount(){
        this.count ++ 
      }
    }

});

export default mainStore; //导出容器给组件使用
```



#### 在组件中使用

```typescript
import mainStore  from '@/store/index.ts'

const mainStoreInstance = mainStore()

console.log(mainStoreInstance.count); //获取到store中state中的数据

mainStoreInstance.changeCount(); //调用store中的actions中的方法
```

