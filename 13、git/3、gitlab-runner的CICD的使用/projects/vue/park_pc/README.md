<!--
 * @Description: 
 * @Author: cuijing
 * @Date: 2023-02-20 13:34:03
 * @LastEditors: cuijing
 * @LastEditTime: 2023-02-20 13:41:37
 * @文件相对于项目的路径: \park_pc\README.md
-->
## 开发

```bash
# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npmmirror.com

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```