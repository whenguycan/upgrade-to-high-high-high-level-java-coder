## 集成antd UI包



#### 引入antd UI包

> 官方参考：https://antdv.com/docs/vue/getting-started-cn

在项目根目录，运行 cnpm install ant-design-vue@next 或者 yarn add ant-design-vue@next

验证是否安装成功，只要去项目的package.json中查看，dependencies部分有没有ant-design-vue及版本就行。



#### 使用antd UI包

- 修改main.js，添加如下内容

  ```javascript
  import Antd from 'ant-design-vue'
  import 'ant-design-vue/dist/antd.css'
  
  createApp(App).use(Antd).......
  ```

- 测试是否正确使用

  去antd UI的官网随便找个按钮，把按钮代码复制到之前的index.vue页面中，看看效果，就知道是否正确集成

- 后续使用，请查看antd ui的官方文档。