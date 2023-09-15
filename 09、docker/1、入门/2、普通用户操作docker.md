## 普通用户操作docker



在docker安装完成之后，会新增一个docker的用户组。普通用户没加入到docker用户组之前是没有权限操作docker的。如果没有权限操作docker会出现如下错误

```shell
Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/containers/json?all=1": dial unix /var/run/docker.sock: connect: permission denied
```



如何解决？只需要将用户加入到docker的用户组即可！

```shell
usermod -aG docker username
```

