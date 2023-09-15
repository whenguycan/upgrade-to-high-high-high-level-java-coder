## 详解export关键字

看HelloWorld.vue中用了`export`关键字，我们就需要学习下。



#### 1、export的作用

写在.js文件中或者.vue文件的<script></script>标签内，用于导出一个变量、对象、函数、组件，可以被其它的.js文件或者.vue文件的<script></script>标签内import导入使用。



#### 2、导出变量

- 导出一个变量

  到vue项目中，新建一个test目录，随后新建一个test.js，写入

  ```javascript
  export var age = 33
  ```

  在任意的.js文件或者.vue文件的<script></script>标签中，导入变量使用

  ```javascript
  import {age} from '@/test/test'
  console.log(age)
  ```

  注意：import的时候，<font color="red">一定要写成{age}，别问我为啥！我也不知道</font>，而且import {age}，这儿的age必须export时的变量名一致！

  

- 导出多个变量

  到vue项目中，新建一个test目录，随后新建一个test.js，写入

  ```javascript
  export var age = 33
  export var name = "tangwei"
  ```

  上面这样挨个export是可以的，或者也可以使用批量导出

  ```javascript
  var age = 33
  var name = "tangwei"
  
  export {name, age}
  ```

  在任意的.js文件或者.vue文件的<script></script>标签中，导入变量使用

  ```javascript
  import {age, name} from '@/test/test'
  console.log(age)
  console.log(name)
  ```



#### 3、导出函数

- 导出一个函数

  到vue项目中，新建一个test目录，随后新建一个test.js，写入

  ```javascript
  export function a(i, j){
      return i + j
  }
  ```

  在任意的.js文件或者.vue文件的<script></script>标签中，导入函数使用

  ```javascript
  import {a} from '@/test/test'
  console.log(a(1,2))
  ```

  注意：import的时候，<font color="red">一定要写成{a}，别问我为啥！我也不知道</font>，而且import {a}，这儿的a必须export时的函数名一致！

  

- 导出多个函数

  到vue项目中，新建一个test目录，随后新建一个test.js，写入

  ```javascript
  export function a(i, j){
      return i + j
  }
  
  export function b(i, j){
      return i + j
  }
  
  ```

  上面这样挨个export是可以的，或者也可以使用批量导出

  ```javascript
  function a(i, j){
      return i + j
  }
  
  function b(i, j){
      return i + j
  }
  
  export {a, b}
  ```

  在任意的.js文件或者.vue文件的<script></script>标签中，导入函数使用

  ```javascript
  import {a, b} from '@/test/test'
  console.log(a(1,2))
  console.log(b(3,4))
  ```

  

- 导出匿名函数

  到vue项目中，新建一个test目录，随后新建一个test.js，写入

  ```javascript
  export default function (i, j){
      return i + j;
  }
  ```

  `export default`意思是在导出的时候，因为是匿名函数，没有具体的名称，所以带个默认的导出名叫default.

  注意：<font color="red">这儿default，是语法规定，不能改！</font>

  

  在任意的.js文件或者.vue文件的<script></script>标签中，导入函数使用

  ```javascript
  import func from "@/test/test"
  console.log(func(2,3))
  ```

  因为导出的匿名函数没有名称，所以这儿可以自定义一个名称去接，然后调用！





#### 4、导出对象

到vue项目中，新建一个test目录，随后新建一个test.js，写入

```javascript
export default {
    a: 'hello',
    b: 'world'
}
```

因为对象没有名称，所以我们使用`export default`给他带个默认的`default`名称导出！

在任意的.js文件或者.vue文件的<script></script>标签中，导入对象使用

```javascript
import oo from "@/test/test"
console.log(oo.a, oo.b)
```

因为导出的对象没有名称，所以这儿可以自定义一个名称去接，然后调用！



#### 5、导出组件

>  其实导出的是一个特殊的组件对象

到vue项目中，新建一个test目录，随后新建一个test.vue，写入

```vue
<template>
  <div>
    test
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>

```

到需要使用test.vue组件的文件中，使用

```javascript
<template>
  <test-vue></test-vue>  <!-- 使用组件 -->
</template>

<script>

import testVue from "@/test/test.vue" //import组件

export default {

  name: 'App',
  components: {
    testVue     //放到当前页面可用组件中
  }
}
</script>
```



































