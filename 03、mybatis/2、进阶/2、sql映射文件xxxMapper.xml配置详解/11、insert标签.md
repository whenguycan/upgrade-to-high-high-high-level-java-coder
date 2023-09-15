## insert标签

代表插入数据

```xml
<insert id="insertTe"  useGeneratedKeys="true" keyProperty="id">
  insert into te (`user_name`, `age`, `addr`) values (#{userName}, #{age}, #{addr})
</insert>
```

`useGeneratedKeys`代表自动生成key值，`keyProperty`是key值生成到传入对象的哪一个属性中。

经过`useGeneratedKeys`和`keyProperty`的设置就可以获取到插入数据的自增id了！