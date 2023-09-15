## select标签

select标签代表是查询语句，内部只能放select语句

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!-- namespace参数，指定命名空间 -->
<mapper namespace="test">
    <!-- select标签是指定下面的sql语句时select语句 -->
    <select id="selectAll" [resultType="com.example.demo11.entity.TeEntity" resultMap="xxxx" parameterType="int/Integer/....." flushCache="false"]> <!-- id是下面的sql语句的唯一标识 resultType为结果集数据类型，会自动把数据库搜索出来的数据包装成指定的resultType类型 -->
        select * from te where id = #{id}/${id} 
    </select>
</mapper>
```

注意：

1. `id`属性当前select标签的唯一标识，需要与其Mapper代理文件中的方法一一对应

2. `resultType`指定sql语句的返回数据最终包装的格式，如果需要返回一个简单类型，比如int或者String，可以直接写成int或者string，如果需要返回一个实体对象就需要写成最终的包装类型！

3. `resultMap`如果指定resultMap标签，则返回的数据会通过resultMap进行包装

4. `parameterType`如果是有参查询，在Mapper代理接口文件中方法传入的具体的参数类型，即便有传参该，参数类型也可以省略不写。

4. `flushCache` 将其设置为 true 后，只要语句被调用，都会导致一级缓存和二级缓存被清空，默认值：false。一般不用管！

4. `useCache` 将其设置为 true 后，将会导致本条语句的结果被二级缓存缓存起来，默认值：对 select 元素为 true。一般也不用管！

4. `timeout` 设置执行sql的最大等待时间！超过时间就会抛出异常！

5. `#{xx}`与 `${xx}`在sql映射文件中都是表示占位符的意思，如果传入的参数为name，那么占位符可以写成#{name}或者${name}，如果传入的参数为age，那么占位符可以写成#{age}或者${age}。如果传入的参数是一个集合，那么占位符也可以写成#{xxx}或者${yyy}，前提是xxx和yyy在集合中有对应的get/set方法。但是#{}与${}是有区别的！

   `#{}`是你传入的参数，被认为是一个整体，不能单独执行

   `${}`是你传入的参数，不会被认为是一个整体，会被单独执行，会有sql注入的风险，所以这个不常用！
   
   

