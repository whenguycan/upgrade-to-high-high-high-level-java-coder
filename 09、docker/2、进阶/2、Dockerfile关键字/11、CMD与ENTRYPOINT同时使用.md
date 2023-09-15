## CMD与ENTRYPOINT同时使用



当CMD与ENTRYPOINT都使用如下格式且同时出现在dockerfile中，

CMD ["command", "param1", "param2"]

ENTRYPOINT ["command", "param1", "param2"]



那么真实的容器启动命令是把`CMD`的命令做为参数拼接到`ENTRYPOINT`后面，组成整个启动命令。

