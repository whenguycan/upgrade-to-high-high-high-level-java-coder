## location匹配规则



#### 1、location 介绍

location指令的功能是用来匹配不同的url请求，进而对请求做不同的处理和响应，我们输入的网址叫做`请求URI`，nginx用请求URI与`location中配置的URI`做匹配。



#### 2、location的匹配符号详解

- `=`

  精确匹配，内容要同表达式完全一致才匹配成功

  ```shell
  location = /abc/ {
    .....
   }
          
  # 只匹配http://abc.com/abc
  #http://abc.com/abc [匹配成功]
  #http://abc.com/abc/index [匹配失败]
  ```

  为什么`http://abc.com/abc `会匹配成功？因为，当用户发出请求后，服务器默认会去找/abc文件，如果没有找到，则会把abc当成目录，然后重定向到`http://abc.com/abc/`地址，然后就会被匹配上！

  示例如下：

  ![avatar](./images/v210a2c9d40w.webp)

- `~`

  执行正则匹配，区分大小写。

  ```shell
  location ~ /Abc/ {
    .....
  }
  #http://abc.com/Abc/ [匹配成功]
  #http://abc.com/abc/ [匹配失败]
  ```

  

- `~*`

  执行正则匹配，忽略大小写

  ```shell
  location ~* /Abc/ {
    .....
  }
  # 则会忽略 uri 部分的大小写
  #http://abc.com/Abc/ [匹配成功]
  #http://abc.com/abc/ [匹配成功]
  ```

  

- `^~`

  以xxx开头的匹配上了，后面就不再匹配

  ```shell
  location ^~ /index/ {
    .....
  }
  #以 /index/ 开头的请求，都会匹配上
  #http://abc.com/index/index.page  [匹配成功]
  #http://abc.com/error/error.page [匹配失败]
  ```

- 不加任何规则

  默认是大小写敏感，前缀匹配，相当于加了“~”与“^~”

  ```shell
  location /index/ {
    ......
  }
  #http://abc.com/index  [匹配成功]
  #http://abc.com/index/index.page  [匹配成功]
  #http://abc.com/test/index  [匹配失败]
  #http://abc.com/Index  [匹配失败]
  ```

- `/`

  任何没有匹配成功的，都会匹配这里处理

  ```shell
  location  /  {
  
  }
  ```

  



#### 3、location匹配顺序

`=` > `^~` > `~ | ~*` > `不加任何规则匹配` > `/





#### 4、location练习

```shell
location = /  {
#规则A
}

location = /login {
#规则B
}

location ^~ /static/ {
#规则C
}

location ~ \.(gif|jpg|png|js|css)$ {
#规则D
}

location ~* \.png$ {
#规则E
}

location / {
#规则H
}
```

测试：

```shell
访问根目录/， 比如http://localhost/ 将匹配规则A

访问 http://localhost/login 将匹配规则B，

http://localhost/register 则匹配规则H

访问 http://localhost/static/a.html 将匹配规则C

访问 http://localhost/b.jpg 将匹配规则D和规则E，但是规则D顺序优先，规则E不起作用， 而 http://localhost/static/c.png 则优先匹配到 规则C

访问 http://localhost/a.PNG 则匹配规则E， 而不会匹配规则D，因为规则E不区分大小写。

访问 http://localhost/qll/id/1111 则最终匹配到规则H，因为以上规则都不匹配。
```

