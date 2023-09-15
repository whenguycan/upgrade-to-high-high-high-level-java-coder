## springboot使用web开发场景的三种方式



| 使用方式 |                         具体使用方法                         |             实现效果             |
| :------: | :----------------------------------------------------------: | :------------------------------: |
|  全自动  |                    直接编写controller逻辑                    |     全部使用自动配置默认效果     |
| 手自一体 | @Configuration + 实现WebMvcConfigurer接口 + 配置WebMvcRegistrations + 不要@EnableWebMvc | 自动配置+手动设置部分mvc底层组件 |
|  全手动  | @Configuration + 实现WebMvcConfigurer接口 + 要@EnableWebMvc  | 禁用自动配置效果，变为全手动配置 |

我们一般是使用”手自一体“的模式