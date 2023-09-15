## 根据id查询一条数据



#### 1、新增sql映射文件对应的代理接口文件



#### 2、在代理接口文件中写入，写一个查询方法

```java
public interface TeMapper {
    List<TeEntity> selectOneById(int id); //查询方法
}
```



#### 3、在sql映映射文件中，编写对应方法的具体sql

```xml
<mapper namespace="com.example.demo11.mappers.TeMapper">
    <resultMap id="allcol" type="com.example.demo11.entity.TeEntity">
        <id column="id" property="id"></id>
        <result column="user_name" property="userName"></result>
    </resultMap>

    <!-- select标签是指定下面的sql语句时select语句 -->
    <select id="selectOneById" resultMap="allcol"  parameterType="integer"> <!-- id是下面的sql语句的唯一标识 resultType为结果集数据类型，会自动把数据库搜索出来的数据包装成指定的resultType类型 -->
        select id, user_name, age, addr from te where id = #{id}
    </select>
</mapper>
```



#### 4、编写test方法执行测试

```java
//1. 创建sqlSessionFactory对象
String resource = "mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//2. 获取sqlSession对象，用它来执行sql
SqlSession sqlSession = sqlSessionFactory.openSession(true); //true代表自动提交

//3. 执行sql语句
// 这儿的 返回值是在TeMapper.xml中的resultType中定义好的
// 这儿的 test是TeMapper.xml文件中定义的namespace
// 这儿的 selectAll是在TeMapper.xml文件中的id="selectAll"定义的唯一标识
// List<TeEntity> teEntityList = sqlSession.selectList("test.selectAll");

TeMapper teMapper = sqlSession.getMapper(TeMapper.class); //获取到代理接口类
TeEntity teEntityList = teMapper.selectOneById(1); //指定代理接口类中的方法

System.out.println(teEntityList);

//4. 断开连接释放资源
sqlSession.close();
```

