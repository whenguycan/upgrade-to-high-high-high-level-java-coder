## 路由独享守卫

就是进入到对应的路由中的守卫，即每个路由各自的拦截器。

```js
import Index from '../views/Index'   //引入上面新建的2个文件（component）
import Footer from "../views/Footer.vue";
import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/index/:id',
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
            },
            beforeEnter: (to, from, next)=>{  //看这儿！！！！！这儿就是写的路由独享的守卫！！！！！！
                console.log(to)
                console.log(from)
                next()
            }
        }
    ]
})

export default router


```

注意：路由独享守卫不会随着`params`、`query` 或 `hash` 改变时触发。例如，从 `/users/2` 进入到 `/users/3` 或者从 `/users/2#info` 进入到 `/users/2#projects`不会触发独享守卫。



