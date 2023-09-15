## CMD关键字

> CMD在Dockerfile中只能出现一次，有多个，只有最后一个会有效。其作用是在启动容器的时候提供一个默认的命令项。
>
> 如果用户执行docker run ......imageid command xxx xxx 的时候提供了命令项，就会覆盖掉这个命令。没提供就会使用构建时的命令。



CMD关键字的使用格式：

- CMD ["command","param1","param2"]
- CMD command param1 param2 



```dockerfile
FROM nginx:latest
....
....
....
CMD ["java", "-jar", "xxxx.jar"]
```

