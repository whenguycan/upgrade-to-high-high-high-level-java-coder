## 安装prettier



使用如下命令即可完成安装

```shell
npm install prettier eslint-plugin-prettier eslint-config-prettier
```

其中：

- eslint-config-prettier的作用，让所有跟prettier规则冲突的eslint rules失效，并使用prettier进行代码检查
- eslint-plugin-prettier的作用，使prettier的规则优先级更高，eslint的优先级更低

