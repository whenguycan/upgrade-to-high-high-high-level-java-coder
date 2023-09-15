## ENTRYPOINT关键字

> 与CMD使用一致！
>
> ENTRYPOINT在Dockerfile中只能出现一次，有多个，只有最后一个会有效。其作用是在启动容器的时候提供一个默认的命令项。
>
> ENTRYPOINT指定的启动命令，如果用户执行docker run --entrypoint xxxx 进行覆盖！



ENTRYPOINT的使用格式

- ENTRYPOINT ["command", "param1", "param2"]
- ENTRYPOINT command param1 param2



```dockerfile
FROM nginx:latest
....
....
....
ENTRYPOINT ["java", "-jar", "xxxx.jar"]
```

