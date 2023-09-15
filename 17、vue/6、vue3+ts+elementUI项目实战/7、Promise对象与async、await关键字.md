## Promise与async、await关键字



#### Promise的作用

Promise对象是一个异步编程的解决方案,可以将异步操作以同步的流程表达出来, 避免了层层嵌套的回调函数(俗称’回调地狱’)



- promise的三个状态
  1. pending: 等待 (进行中) promise一创建出来，就是pending进行中
  2. fulfilled: 成功 (已完成), 调用 resolve, 就会将状态从pending改成fulfilled, 且将来就会执行.then
  3. rejected: 失败 (已拒绝), 调用 reject, 就会将状态从pending改成rejected, 且将来就会执行.catch

- promise提供的方法
  1. then方法
     - then方法必须返回一个promise对象
     - then方法中有两个参数，都是函数,第一个函数是成功处理函数，第二个函数是失败处理函数,但是一般不用第二个
     - then中的函数如果 没有书写return或者promise等，那么本身返回一个promise的fulfilled状态的对象,（promiseResult：undefined）
     - then中的函数，如果返回一个promise对象，then的返回值就是这个promise对象，promise对象的状态和内部返回的promise状态一致
     - then中的函数，返回值不是一个promise对象，则then还是返回成功状态的promise对象,但是PR值就是这个返回的值（promiseResult：return的值）
     - 如果then中的函数出错了（有异常了），则then返回一个失败的promise对象
  2. catch方法
     - catch是处理失败promise的方法,catch返回值的规则和then一样

#### async的作用

修饰一个函数，表示该函数会返回一个promise对象。

在函数内，

- 如果return了一个值，则返回是成功的promise对象，值是return的内容
- 如果return了一个promise对象，则返回值就是这个promise对象
- 如果 抛出错误 或者 是里边等待了一个失败的promise对象，则async的返回值是失败的promise对象，promise对象的值就是 reject的参数 或 错误





#### await的作用

在async修饰的函数内部使用，表示等待一个promise对象。

- 如果等待的promise对象返回成功状态，则继续向下运行，最后async函数返回一个成功的promise对象
- 如果等待的promise对象返回的是失败状态，则停止运行，async函数返回这个失败的promise对象