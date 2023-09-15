## plugin关键字

> 管理docker插件的工具



#### 1、安装一个插件

```shell
docker plugin install [OPTIONS] PLUGIN [KEY=VALUE...]
```

`OPTIONS`可选的参数

1. --alias： 指定插件的本地名称
2. --disable：安装成功后，不要启用该插件，默认是false
3. --grant-all-permissions：是否授予运行插件所需的所有权限，默认值false

例子：

```shell
docker plugin install registry.docker.com/grafana/loki-docker-driver:main --alias loki --grant-all-permissions
```



#### 2、列出所有的plugin

```shell
docker plugin ls
```



#### 3、禁用一个plugin

```shell
docker plugin disable 插件id
```



#### 4、启用一个plugin

```shell
docker plugin enable 插件id
```

