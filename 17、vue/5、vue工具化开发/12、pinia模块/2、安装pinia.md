## 安装pinia



#### 1、下载pinia依赖

```shell
npm install pinia
```



#### 2、在项目入口文件引入并使用pinia

```typescript
//引入依赖
import { createPinia } from 'pinia'

//创建pinia实例
const pinia = createPinia()

const app = createApp(App)
//挂载pinia
app.use(pinia)
app.mount('#app')
```



