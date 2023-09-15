## eslint的使用

初始化eslint并且认识了生成的配置文件后，我们需要真正使用eslint



#### 在vue项目中需要再安装一个eslint使用的插件

```shell
npm install eslint-plugin-vue 
```

其中：

- eslint-plugin-vue的作用，检查vue语法的eslint的插件



安装好这些插件后，eslint的配置文件需要我们写入一些配置规则

```json
module.exports = {
    "env": {
        "browser": true,
        "es2021": true
    },
    "extends": [
        "eslint:recommended",
        "plugin:@typescript-eslint/recommended",
        "plugin:vue/vue3-essential"
    ],
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
        "ecmaVersion": "latest",
        "parser": "@typescript-eslint/parser",
        "sourceType": "module"
    },
    "plugins": [
        "@typescript-eslint",
        "vue"
    ],
    "rules": {

        // off == 0 关闭规则
        // warn == 1 表示是一个警告规则
        // error == 2 表示是一个错误

        "no-var": "error", //要求使用const或者let，而不是用var定义变量
        "no-multiple-empty-lines": ['warn', {max: 1}],//不允许多个空行，最多只能一个空行，否则扔警告


    }
}

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

在main.js中写入`var a = 100`，因为我们在规则中设置了不能用var定义变量，然后运行如下命令进行检测

```shell
npm run lint
```

然后会抛出如下错误：

```shell
/Users/tangwei/vueProjects/vite-vue-start/vite-01/src/main.js
  8:1  error    Unexpected var, use let or const instead   no-var
  8:5  warning  'name' is assigned a value but never used  @typescript-eslint/no-unused-vars

✖ 2 problems (1 error, 1 warning)
  1 error and 0 warnings potentially fixable with the `--fix` option.


```





