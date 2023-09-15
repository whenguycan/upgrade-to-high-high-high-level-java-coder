## 以svg组件为例，使用自定义插件注册全局组件



为什么要使用自定义插件注册全局组件呢？因为，我们的项目到后期，会有很多全局组件，如果都在入口文件进行注册，那么入口文件将变的很难维护，所以，我们需要让入口文件尽可能简单，就在自定义插件中注册全局组件，入口文件就只要use下这个自定义插件就行了！



#### 编写自定义插件

以后所有的组件都在这儿注册

```typescript
import svgIcon from '@/components/SvgIcon/index.vue' //引入要注册的全局组件

//对外暴露插件对象
export default {

    //一定要叫install方法，当插件在入口文件被use的时候，就会触发这个install方法的执行
    install(app){ //这儿的app，就是入口文件use的时候传入的整个应用的对象

        app.component("svgIcon",svgIcon) //注册全局组件


    }

}
```



#### 入口文件中使用自定义插件

```typescript
import { createApp } from 'vue'
import App from '@/App.vue'
import 'virtual:svg-icons-register'
import globalComponent from '@/components/index.ts'; //引入自定义的插件

const app = createApp(App)

app.use(ElementPlus)

app.use(globalComponent) //使用这个自定义插件，就会触发自定义插件中的install方法

app.mount('#app')


```



#### 在需要使用的组件中直接使用

```vue
<svgIcon ......>


</svgIcon>
```

