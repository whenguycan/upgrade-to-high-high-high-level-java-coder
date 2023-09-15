## 在映射文件中sql条件如果出现>、>=、<、<= 的处理

因为映射文件本身是.xml文件，如果出现>、>=、<、<=文件是会报错的！

```xml
<mapper namespace="com.example.demo11.mappers.TeMapper">

    <resultMap id="allcol" type="com.example.demo11.entity.TeEntity">
        <id column="id" property="id"></id>
        <result column="user_name" property="userName"></result>
    </resultMap>

    <!-- select标签是指定下面的sql语句时select语句 -->
    <select id="selectAll" resultMap="allcol"> <!-- id是下面的sql语句的唯一标识 resultType为结果集数据类型，会自动把数据库搜索出来的数据包装成指定的resultType类型 -->
        select id, user_name, age, addr from te
    </select>

    <select id="selectOneById" resultMap="allcol">
        select * from te where id < #{id}
    </select>
</mapper>
```

`select * from te where id < #{id}` 这儿是会报错的！

怎么解决>、>=、<、<= 这类字符呢？

有两个方法

1. 方法1：使用转义符

   - \> 使用 `&gt;`
   - \>= 使用 `&gt;=`
   - < 使用`&lt;`
   - <= 使用 `&lt;=`

2. 使用 `<![CDATA[    .....  ]]> `来包裹 >、>=、<、<= 这类字符

   ```xml
   <select id="selectOneById" resultMap="allcol">
     select * from te where id
     <![CDATA[
               > 
           ]]>
     #{id}
   </select>
   ```

   

   

