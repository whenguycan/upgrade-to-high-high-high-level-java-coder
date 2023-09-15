## 初始化eslint



#### 1、初始化eslint

运行如下命令即可初始化

```shell
npx eslint --init # 注：npx 表示从当前路径下查找命令，即 ./node_modules/.bin/eslint --init
```

初始化的过程中，会交互式的询问一些问题，以生成.eslintrc.*配置文件

```shell
? How would you like to use ESLint? …
❯ To check syntax only        #仅检查语法
  To check syntax and find problems  #检查语法以及发现错误
  To check syntax, find problems, and enforce code style  #检查语法、发现问题、强制代码风格
```

我们一般选择`To check syntax and find problems`

然后又跳出问题

```shell
? What type of modules does your project use? …
❯ JavaScript modules (import/export)   
  CommonJS (require/exports)
  None of these
```

我们一般选择`JavaScript modules (import/export) `

然后又跳出问题

```shell
? Which framework does your project use? …
❯ React
  Vue.js
  None of these
```

然后又跳出问题

```shell
? Does your project use TypeScript? 
› No 
  Yes
```

然后又跳出问题

```shell
? Where does your code run? …  (Press <space> to select, <a> to toggle all, <i> to invert selection)
✔ Browser  #项目运行在浏览器端
✔ Node     #项目运行在Node端
```

我们肯定选择`Browser`

然后又跳出问题

```shell
? What format do you want your config file to be in? …  #生成什么样的配置文件
❯ JavaScript # js
  YAML       # yaml
  JSON       # json
```

我们肯定选择`JavaScript`

然后提示我们还需要安装一些检测vue语法、ts的语法的插件，我们直接选择安装就行。



初始化完毕之后，项目中会出现`.eslintrc.cjs`的配置文件。



#### 2、.eslintrc.cjs配置文件详解

```js
module.exports = { //对外暴露的配置对象
  	//eslint的工作环境
    "env": {
        "browser": true, //在浏览器端工作
        "es2021": true   //校验的es语法的版本
    },
  	//eslint校验规则的继承
    "extends": [
        "eslint:recommended", //eslint的校验规则默认是全部关闭的，但是配置了这个项目，就会打开推荐的校验规则 比如函数不能重名等
        "plugin:@typescript-eslint/recommended", //开启ts的语法规则校验
        "plugin:vue/vue3-essential" //开启vue3的语法规则校验
    ],
  	//为一些特定的文件，指定校验处理器
    "overrides": [
        {
            "env": {
                "node": true
            },
            "files": [
                ".eslintrc.{js,cjs}"
            ],
          	
            "parserOptions": {
                "sourceType": "script"
            }
        }
    ],
  	
    "parserOptions": {
        "ecmaVersion": "latest", //指定校验的语法是ecma的最新版本的语法
        "parser": "@typescript-eslint/parser", //使用ts的解析器去解析项目中的代码
        "sourceType": "module"
    },
  	//eslint工作需要的额外插件
    "plugins": [
        "@typescript-eslint",
        "vue"
    ],
  
  	//校验规则的配置，需要自己去参考eslint的官网，然后自己写校验规则
    "rules": {
    }
}

```

