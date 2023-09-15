## resultMap标签

在很多情况下，数据库字段与我们select标签中指定的resultType中的属性名称会有不一致的情况，比如数据库中的字段为user_name，而select标签中指定的resultType中的属性名称为userName，那么会导致读取出来的user_name无法包装成resultType中的userName。

我们可以使用resultMap标签，在select标签中指定的resultMap，然后会select的数据，先经过resultMap包装，在返回resultType指定的数据。

```xml
<mapper namespace="com.example.demo11.mappers.TeMapper">

    <resultMap id="allcol" type="com.example.demo11.entity.TeEntity"> <!-- type指定包装之后返回的类型 -->
        <id column="id" property="id"></id> <!-- id标签为主键字段映射 -->
        <result column="user_name" property="userName"></result>  <!-- result标签为非主键字段映射， column为数据库字段  property是类属性字段 -->
      
      	<association......>......</association>
      	<collection.......>......</collection>
    </resultMap>

    <!-- select标签是指定下面的sql语句时select语句 -->
    <select id="selectAll" resultMap="allcol"> <!-- resultMap 指定把数据转到具体的resultMap去进行包装。 -->
        select id, user_name, age, addr from te
    </select>
</mapper>
```



注意：

当我们查询的数据，有一部分内容在另外一张表，我们就需要使用关联查询。

当te（学生）表，每个学生都有一条考试成绩记录存放在score（成绩）表中，我们在查询学生列表的时候，就需要把学生本次考试成绩关联查询出来，就可以使用`association`标签。`association`仅能用在一对一关系上！



当te（学生）表，每个学生都有多条考试成绩记录存放在score（成绩）表中，我们在查询学生列表的时候，就需要把学生所有考试成绩关联查询出来，就可以使用`collection`标签。`collection`仅能用在一对多关系上！



具体使用看实战部分！

