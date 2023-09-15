## 集成axios成功后，在TS内我们如何设置请求和响应的数据类型



#### 1、集成axios

- 引入axios包

  > 官方参考：http://www.axios-js.com/zh-cn/docs/vue-axios.html
  >
  > 

​		在项目根目录，运行 npm install axios

​		验证是否安装成功，只要去项目的package.json中查看，dependencies部分有没有axios及版本就行。



- 配置axios，添加请求拦截器

  在src目录下新建一个request文件夹，在里面新建index.js文件，内容如下：

  ```typescript
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

- 配置跨域代理

  找到vite.config.ts中，找到defineConfig添加如下代码

  ```typescript
   server: {
     open:true,//是否打开
       cors: true,
         proxy:{//代理信息（转发信息）
           "/apis":{//暗号 一致会被拦截，不一致就不会被拦截
             		target:'http://localhost:9999',//真实地址 表示要代理到哪里去（后端端口号）
               	ws: false,        //如果要代理 websockets，配置这个参数
                secure: false,  // 如果是https接口，需要配置这个参数
                changeOrigin:true,//地址替换
                rewrite: (path) => path.replace(/^\/apis/, ""),//消除作案痕迹 将apis替换为空
  
           }
         }
  
   }
  ```

  





- 封装各个网络请求

  在src目录下新建一个apis文件夹，这里面放入今后所有的请求文件，例如新建一个请求用户登录的接口login.ts，代码如下：

  ```javascript
  import httpRequest from "@/request/index"
  
  export const login = (param: any) => httpRequest.post<any, any>("/login", param)
  
  ```

  我们发现，在调用login方法的时候，参数param的类型为any，post方法的第二个参数是返回数据的类型也是any，如果都写any那没有任何意义，所以我们需要对照接口的数据进行修改！

  - 假设我们请求login方法的参数如下结构

    ```json
    {
      "username":"tangwei",
      "password":"123456"
      
    }
    ```

    所以我们就需要新建一个type.ts文件，内容如下：

    ```typescript
    export interface loginUserData{
        username: string,
        password: string
    }
    ```

    然后就修改login.ts中的代码，修改成如下的样子

    ```typescript
    import httpRequest from "@/request/index"
    import {loginUserData} from '@/apis/user/type.ts'; //引入我们上面定义的接口，做为入参的参数类型
    
    //param的any改成loginUserData
    export const login = (param: loginUserData) => httpRequest.post<any, any>("/login", param)
    ```

  - 假设我们login接口的返回数据结构如下

    ```json
    {
      "token": "sdfsdfsdfsdfsdfsg-sdfsdfgdsg-dfghbdfbh",
      "roles": ["xxxx", "yyyy", "zzzzz"],
      "info": {
        	
        "age": 10,
        "height": "1,.83"
      }
    }
    ```

    所以我们就需要在type.ts文件中，写内容如下：

    ```typescript
    interface userInfo{
        age: number,
        height: string
    }
    
    export interface loginResponseData{
        token: string,
        roles: string[],
        info: userInfo
    }
    ```

    然后就修改login.ts中的代码，修改成如下的样子

    ```typescript
    import httpRequest from "@/request/index"
    import {loginUserData, loginResponseData} from '@/apis/user/type.ts';//引入我们上面定义的接口loginResponseData，做为入参的参数类型
    
    //post中第二个参数从any改为loginResponseData
    export const login = (param: loginUserData) => httpRequest.post<any, loginResponseData>("/login", param)
    
    ```

    

  

