## 集成svg图标



#### 为什么要使用svg图标

svg图标相对于img图片，页面加载的性能非常高。且svg图标比img图片小很多，放在项目中几乎不占用资源。





#### 如何集成svg图标



- 安装svg插件

  ```shell
  npm install vite-plugin-svg-icons
  ```



- 入口文件main.ts中配置下svg插件需要的配置

  添加如下代码

  ```typescript
  import 'virtual:svg-icons-register'
  ```

  

- 修改vite.config.ts文件，添加如下内容

  ```typescript
  import {createSvgIconsPlugin} from "vite-plugin-svg-icons"; //引入svg插件暴露出来的createSvgIconsPlugin方法
  
  plugins: [
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')], //设定svg图标存放的目录为src/assets/icons
      symbolId: 'icon-[dir]-[name]',
    })
  
  ],
  ```



#### 集成完毕后，项目中如何使用svg图标



使用方式一：直接使用

- 打开阿里图标库网站，地址：https://www.iconfont.cn/

- 随便找个图标，点击下载具体的某个图标，然后找到`复制svg代码`功能

- 到本地项目中src/assets中新建icons目录，然后新建一个.svg文件，把上面复制的svg代码放进去

- 在需要的组件中使用，如下代码

  ```vue
  <!--使用svg图标，必须在外层用svg包裹，在内部一定要使用use标签-->
  <svg>
  
    <!-- 具体使用哪个svg图标，使用xlink:href指定图标的路径 -->
    <use xlink:href="#icon-xxx"></use> <!-- xlink:href内部使用svg图标，一定是#icon开头，然后后面跟着svg图标的名称->
  
  </svg>
  ```

  svg图标的大小可以自己调控，在svg标签内调整就行

  ```vue
  <svg style="width:10px;....">
  
  </svg>
  ```



使用方式二：封装成组件使用

- 分装好的组件代码

  ```vue
  <template>
      <!--使用scg图标，必须在外层用svg包裹，在内部一定要使用use标签-->
      <svg style="width: 30px;">
  
          <!-- 具体使用哪个svg图标，使用xlink:href指定图标的路径 -->
          <use :xlink:href="'#icon-' + pppp.name"></use> <!-- xlink:href内部使用svg图标，一定是#icon开头，然后后面跟着svg图标的明后才能-->
  
      </svg>
  </template>
  
  <script setup lang="ts">
  
  import { defineProps } from 'vue';
  
  
   //这儿接收父组件调用的时候传入的参数
  const pppp = defineProps({
  
      name: {
          type: String,
      }
  
  })
  
  </script>
  
  ```

- 使用封装好的组件

  ```vue
  <script setup lang="ts">
      import svgIcon from '@/components/SvgIcon/index.vue' //引入组件
  
  </script>
  
  <template>
  
    <svgIcon name="xxx"></svgIcon>  <!-- 使用组件并传参 -->
  </template>
  
  <style scoped>
  
  
  
  </style>
  
  ```
  
  

使用方式三：定义方式二的组件为全局组件，只要在入口文件中引入一次，不需要再在各个需要使用的组件中重复import

- 只要在入口文件引入该组件，并注册该组件就行

  ```typescript
  import { createApp } from 'vue'
  import App from '@/App.vue'
  import 'virtual:svg-icons-register'
  import svgIcon from '@/components/SvgIcon/index.vue' //引入我们写的组件
  
  const app = createApp(App)
  
  app.component("svgIcon", svgIcon) //注册全局组件，注意双引号内的就是组件的名称，那么后面只能使用<svgIcon ....></svgIcon>
  
  app.mount('#app')
  
  ```

  