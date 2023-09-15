## 导航守卫

当用户点击菜单按钮(路由)之后，会经过导航守卫的拦截，从而达到权限判断、是否登录等条件的判断，如果不符合规定的，我们就可以进行拦截、跳转等操作。

官方参考：https://router.vuejs.org/zh/guide/advanced/navigation-guards.html#%E5%85%A8%E5%B1%80%E5%89%8D%E7%BD%AE%E5%AE%88%E5%8D%AB



全局导航守卫就是每个路由都会执行的导航守卫（拦截器）



#### 1、全局前置导航守卫

```js
import Index from '../views/Index'   //引入上面新建的2个文件（component）
import Login from '../views/Login'
import Error from '../views/Error.vue'
import Footer from "../views/Footer.vue";
import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/index/:id([0-9]+)',
            name: 'index2',
            props: {
                default: true,
                Footer: {
                    name: 'tangwei'
                }
            },
            components: {
                default: Index,
                Footer: Footer
            }
        },
        {
            path: '/login/:id',
            props: (route)=>({
                search: route.query.search
            }),
            name: 'login',
            component: Login
        },
        {
            path: '/:all*',
            name: "Error",
            component: Error
        }
    ]
})

//看这里，这里就是写了一个全局前置守卫，所有的请求都会先经过这儿拦截!!!!!!!!!!!!!!!!!!!!
router.beforeEach((to, from,next)=>{
// to是指要跳转到哪里去
// from是指从哪里进来的
//next是一个函数，不执行next函数是无法往下跳转的
  next()
})

export default router
```

我们在上面写了很多路由



#### 例子：利用全局导航守卫，模拟登录失败跳转到登录页面去

```js
import Index from '../views/Index'   //引入上面新建的2个文件（component）
import Login from '../views/Login'
import Error from '../views/Error.vue'
import Footer from "../views/Footer.vue";
import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/index',
            name: 'index2',
            props: {
                default: true,
                Footer: {
                    name: 'tangwei'
                }
            },
            components: {
                default: Index,
                Footer: Footer
            }
        },
        {
            path: '/login',
            props: (route)=>({
                search: route.query.search
            }),
            name: 'login',
            component: Login
        },
        {
            path: '/:all*',
            name: "Error",
            component: Error
        }
    ]
})

//看这里，这里就是写了一个全局前置守卫，所有的请求都会先经过这儿拦截!!!!!!!!!!!!!!!!!!!!
var isLogined = true
router.beforeEach((to, from,next)=>{
// to是指要跳转到哪里去
// from是指从哪里进来的
//next是一个函数，不执行next函数是无法往下跳转的
  
    if(isLogined){ //如果登录成功了
        if (to.name === 'login'){ //登录成功在进入登录界面，跳转回/index
            next('/index');//控制跳转到/index
        }else{
            next()//如果不是继续渲染页面
        }

    }else{//如果没有登录成功
        if (to.name === 'login'){//如果已经在登录页面了，不用跳转直接渲染页面
            next();
        }
        else{//没有登录成功，就控制跳转到/login页面
            next({
                name: 'login'
            })
        }

    }

})

export default router


```

