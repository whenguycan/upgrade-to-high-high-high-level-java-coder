## 使用axios发起网络请求

> 帮助我们发送一个ajax请求出去的工具！



#### 引入axios包

> 官方参考：http://www.axios-js.com/zh-cn/docs/vue-axios.html

在项目根目录，运行 npm install axios

验证是否安装成功，只要去项目的package.json中查看，dependencies部分有没有axios及版本就行。





#### 配置axios，添加请求拦截器

在src目录下新建一个request文件夹，在里面新建index.js文件，内容如下：

```javascript
import axios from 'axios'

// 创建一个 axios 实例
const service = axios.create({
    baseURL: '/api', // 所有的请求地址前缀部分
    timeout: 60000, // 请求超时时间毫秒
    withCredentials: true, // 异步请求携带cookie
    headers: {
        // 设置后端需要的传参类型
        // 'Content-Type': 'application/json',
        // 'token': 'token',
        // 'X-Requested-With': 'XMLHttpRequest',
    },
})

// 添加请求拦截器
service.interceptors.request.use(
    function (config) {
      
      	//也可以在这儿设置请求头
        config.headers["Content-Type"]=  "xxxxx"
      
        // 在发送请求之前做些什么
        return config
    },
    function (error) {
        // 对请求错误做些什么
        console.log(error)
        return Promise.reject(error)
    }
)

// 添加响应拦截器
service.interceptors.response.use(
    function (response) {
        console.log(response)
        // 2xx 范围内的状态码都会触发该函数。
        // 对响应数据做点什么
        // dataAxios 是 axios 返回数据中的 data
        const dataAxios = response.data
        // 这个状态码是和后端约定的
        // const code = dataAxios.reset
        return dataAxios
      
      //注意 这儿最好直接返回response就行
    },
    function (error) {
        // 超出 2xx 范围的状态码都会触发该函数。
        // 对响应错误做点什么
        console.log(error)
      
      	let message = "";
        let status = error.response.status
        switch(status){
            case 404:
                message = "请求的内容不存在";
                break;
            case 500:
                message = "服务器开小差了！";
                break;
            case 403:
                message = "没有访问权限";
                break;
            default:
                message = "网络错误请稍后重试";
                break;
        }
      
        return Promise.reject(error)
    }
)

export default service


```



#### 使用axios发送网络请求

在src目录下新建一个apis文件夹，这里面放入今后所有的请求文件，例如新建一个请求用户登录的接口login.js，代码如下：

```javascript
import httpRequest from "@/request/index"

export const login = (param) => {
    return httpRequest({
        url: '/login',
        method: 'post',
        data: param
    })
}

//获取也可以这样写

export const login = (param)=>httpRequest.post(url, param);

```

在具体的页面调用，发起网络请求

```javascript
import {login} from '../apis/login'


login(要发送的数据，一般已经用reactive包装好了，可以直接放在这儿，例子 {xxxx:xxx, yyyy:yy}).then((response)=>{
    const { status, data } = response.data;
    if(status === "success"){
      console.log(data)
    }else{
      console.log("error")
    }
})
```



### 但是这儿有个问题，目前发起的网络请求都统一请求到了vue的服务上面，这么解决？

#### 配置axios代理跨域，将请求转发到后端服务器

在项目根目录下，新建一个vue.config.js文件，内容如下：

```javascript
module.exports = {
    devServer: {
        proxy: {//创建一个代理
            '/api': {//所有以/api的请求都会被这儿代理一下
                //1.必须添加http前缀，没添加我运行不了
                //2.localhost、127.0.0.1、公网地址三者都可以使用
                //3.结尾加不加/都ok
                target: 'http://localhost:8085', //代理转发的具体地址
                // 允许跨域
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': '' //URL的重写规则
                }
            }
        }
    }
}

```



#### java后端代码

```java
@RestController
public class IndexController {

    @PostMapping("/login")
    public HashMap login(@RequestBody HashMap parasm){

        System.out.println(parasm);

        HashMap<String, Object> response = new HashMap();
        response.put("status", 200);
        response.put("msg", "登录成功");
        return response;
    }
}
```



