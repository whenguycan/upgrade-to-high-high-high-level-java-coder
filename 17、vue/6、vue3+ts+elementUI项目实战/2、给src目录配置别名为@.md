## 给src目录配置别名为@



#### 为什么要给src目录配置名称？

因为等我们的项目大了，文件与文件之间的层级多了之后，一个文件要引用另一个文件，就需要使用"./"、".../"、"../../../"这样的代码可读性非常差且不好管理，所以我们想都从src目录开始查找文件，那么给src起个别名，在开发的时候直接使用别名，就会非常省事



#### 如何设置别名

- 修改`vite.config.ts`文件

  ```typescript
  import { defineConfig } from 'vite'
  import vue from '@vitejs/plugin-vue'
  
  //引入path，path是nodejs提供的一个模块，可以获取到绝对路径和相对路径
  import path from 'path'
  
  // https://vitejs.dev/config/
  export default defineConfig({
    plugins: [vue()],
  
    //这儿是配置别名
    resolve: {
      alias: {
        "@": path.resolve("./src") //相对路径别名配置，使用@替代src目录
      }
    }
  })
  ```

  

- 修改tsconfig.json

  在`compilerOptions`中添加配置

  ```json
  "baseUrl": "./",
  "paths": {
    "@/*": ["src/*"] //在编译的时候@/*，改成src/*
  }
  ```



- 测试使用

  只要去main.js中，引入App.vue的地方，把原本"./App.vue"改成'@/App.vue'看看报不报错就行了！

