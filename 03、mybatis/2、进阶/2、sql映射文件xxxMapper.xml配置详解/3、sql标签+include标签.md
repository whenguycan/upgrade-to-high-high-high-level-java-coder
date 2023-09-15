## sql标签 + include标签

当我们的sql语句，有很多重复的部分，那么我们可以把重复的部分提取出来，放到一个公共的标签中，然后需要使用就可以用include标签引入公共的部分就行了



以`select id, user_name, age, addr from te`为例，把`id, user_name, age, addr`提取出来，然后在需要的地方include进去使用

```xml
<!--
  namespace参数，指定命名空间
  -->
<mapper namespace="com.example.demo11.mappers.TeMapper">

    <sql id="select_public">
        id, user_name, age, addr
    </sql>

    <!-- select标签是指定下面的sql语句时select语句 -->
    <select id="selectAll" resultType="com.example.demo11.entity.TeEntity"> <!-- id是下面的sql语句的唯一标识 resultType为结果集数据类型，会自动把数据库搜索出来的数据包装成指定的resultType类型 -->
        select 
            <include refid="select_public"></include> <!-- include进来用！ -->
        from te
    </select>
</mapper>
```

