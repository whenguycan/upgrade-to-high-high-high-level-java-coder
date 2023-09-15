## depends_on关键字



#### 1、使用范例

```yaml
version: '2'
services:
  user:
    build: .
    depends_on:
      - db
      - redis
  redis:
    image: redis
  db:
    image: postgre

```



#### 2、范例详述

depends_on后面是写 “服务的名称”。

如果我们的应用依赖redis和mysql服务，在没有启动数据库和redis容器的时候去启动应用容器，这时候应用容器因为找不到数据库而退出(与非容器启动项目一样)。为了避免此种情况，得找到解决方案：加入一个标签，就是depends_on。该标签解决了容器依赖，启动先后顺序的问题。