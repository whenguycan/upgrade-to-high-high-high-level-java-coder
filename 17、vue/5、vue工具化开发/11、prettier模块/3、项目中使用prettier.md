## 使用prettier



#### 项目中新增一个prettier的配置文件，名称为 .prettierrc.json

```json
{

  "singleQuote": true, //强制要求字符串都是单引号
  "semi": false,       //是否强制在每行语句后加分号
  "bracketSpacing": true,
  "tabWidth": 4 //缩进占几个字符

}

```

这儿就是对代码做限制的配置



#### 项目中新增一个.prettierignore文件，表示prettier忽略的文件

```tex
./dist/*
./node_modules/*
```



#### 到package.json中新增运行脚本，让eslint可以运行

```json
{
  "name": "vite-01",
  "private": true,
  "version": "0.0.0",
  "type": "module",
  "scripts": {
    ......
    "lint": "eslint src", //运行这个命令 eslint会校验src目录下的所有文件
    "fix": "eslint src --fix" //运行这个命令 eslint会校验src目录下的所有文件
  },
  ......
}

```



然后运行指令进行测试

在main.js中写入

```js
import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from "./router/index.js";

createApp(App).use(router).mount('#app')

let ff = ()=>
{
  console.log("tangwei");//这儿写了分号
}

ff();

```



因为我们在规则中设置了每行不用带分号，然后运行如下命令进行检测

```shell
npm run lint
```

会发现，提示去掉分号