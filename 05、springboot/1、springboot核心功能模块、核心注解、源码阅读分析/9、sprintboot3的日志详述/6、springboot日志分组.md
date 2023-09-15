## springboot日志分组



使用范例

```yaml
logging:
  group: #日志进行分组，自己起个名字叫abc
    abc: com.example.demolog.controller, com.example.demolog.service, com.example.demolog.abc #分组包含哪些包
  level:
    abc: info #指定分组的日志级别
```

