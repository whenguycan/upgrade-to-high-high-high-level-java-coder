## element-plus的语言切换

官方参考：https://element-plus.gitee.io/zh-CN/guide/i18n.html



注意一点：在引入语言包的js文件后，项目打包可能会失败，如下

```shell

src/main.ts:3:18 - error TS7016: Could not find a declaration file for module 'element-plus/dist/locale/zh-cn.mjs'. '/Users/tangwei/vueProjects/vite-vue-start/vite-02/node_modules/element-plus/dist/locale/zh-cn.mjs' implicitly has an 'any' type.
  If the 'element-plus' package actually exposes this module, try adding a new declaration (.d.ts) file containing `declare module 'element-plus/dist/locale/zh-cn.mjs';`

3 import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
                   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

```

原因在于，所有的语言包的js文件都不能通过ts的语法检查，我们直接用粗暴的方式解决掉，

```js
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore  
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'  //在引入语言包js的时候，加上一个@ts-ignore注解，不要去检查语言包js文件的语法。
import 'element-plus/dist/index.css'
import App from './App.vue'

const app = createApp(App)
app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')


```

