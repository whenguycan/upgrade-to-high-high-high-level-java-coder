## watch侦听器的使用

侦听的是data中自定义的数据的变化。侦听哪个data中哪个参数，内部的方法名就需要跟data中的参数名一致。



- 简单使用

  

  - 普通属性

    ```html
    <!DOCTYPE html>
    <html lang="en">
    
    <head>
        <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    </head>
    
    <body>
    
    <div id="app">
       
        <input type="tex" v-model="age" />
    
    </div>
    
    <script>
    
        // 声明一个选项对象
        const App = {
            data: function(){
                return {
                    age: 10
                }
            },
    
            watch: {
                age: function(newage, oldage){ //每一次变化都会调用一次侦听器，newval是变化之后的值，oldval是变化之前的值
                    console.log("监听到age的变化")
                }
            }
        }
    
        // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
        const vm = Vue.createApp(App).mount("#app")
    
    </script>
    
    </body>
    </html>
    
    
    ```

    

  - computed属性

    在监听一个data中的一个对象属性有没有变化的时候使用，我们之前是使用computed将对象属性放到一个计算属性中，然后去监听计算属性的
    
    ```html
    <!DOCTYPE html>
    <html lang="en">
    
    <head>
        <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    </head>
    
    <body>
    
    <div id="app">
       
        <input type="tex" v-model="info.age" />
    
    </div>
    
    <script>
    
        // 声明一个选项对象
        const App = {
    
            data: function(){
                return {
                    info: {
                        age: 20,
                        height: 30
                    }
                }
            },
            
            computed: {
    
                age: function(){
                    return this.info.age;
                }
    
            },
    
            watch: {
                age: function(newage, oldage){ //每一次变化都会调用一次侦听器，newval是变化之后的值，oldval是变化之前的值
                    console.log("监听到age的变化")
                }
            }
        }
    
        // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
        const vm = Vue.createApp(App).mount("#app")
    
    </script>
    
    </body>
    </html>
    
    ```
    
    

- watch深度监听

  在监听一个对象属性有没有变化的时候使用，我们之前是使用computed将对象属性放到一个计算属性中，然后去监听计算属性的，如果使用深度监听就不用再去构造一个computed属性了！

  ```html
  <!DOCTYPE html>
  <html lang="en">
  
  <head>
      <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  </head>
  
  <body>
  
  <div id="app">
     
      <input type="tex" v-model="info.age" />
  
  </div>
  
  <script>
  
      // 声明一个选项对象
      const App = {
  
          data: function(){
              return {
                  info: {
                      age: 20,
                      height: 30
                  }
              }
          },
          watch: {
              info: {
                  handler: function(obj){//这儿的obj是指向被监听的对象的，即上面的info
                      console.log(obj)
                  },
                  deep: true, //深度监听的开关
                  immediate: true //是否立即执行上面的handler指向的方法。
              }
            
            	//如果监听多个，写成如下的样子
            	info: [
            		
            			{
            				监听的第一个
          				},
        
        					{
        						监听的第二个
      						}
            	
            	]
          }
      }
  
      // 全局API对象.创建应用对象返回(选项对象).挂载方法(DOM节点)
      const vm = Vue.createApp(App).mount("#app")
  
  </script>
  
  </body>
  </html>
  
  ```

  