## vite的概念及使用vite初始化vue项目



#### 1、什么是vite

vite是vue3.0开始推荐的最新的构建工具，即使用vite可以快速创建一个vue3.0的项目。



#### 2、使用vite初始化vue项目

- 使用如下命令，启动vite

  ```shell
  npm create vite@latest
  ```

  这一步会自动让我们安装`create-vite`，然后需要给项目起个名字，然后需要选择要初始化的项目类型是vue、react等，然后再选择是vue还是vue-ts

- 安装好项目后，终端会有提示

  ```shell
    cd 项目目录
    npm install
    npm run dev
  ```

  因为初始化项目之后，项目的相关依赖都没有被拉进来，也没有node_modules目录，所以才需要运行上面的`npm install`



