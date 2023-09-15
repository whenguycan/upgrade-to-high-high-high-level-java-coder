## Mapper代理开发



#### 编写Mapper代理

- 给TeMapper.xml的sql映射文件新增一个对应的TeMapper的接口

  >  注意点：

  > 1. 该Mapper接口文件最好与我们写的sql映射文件（xxxMapper.xml）同名，但是一般我们映射文件都是xxxMapper.xml，而接口文件一般都是XxxDao.java文件。<font color="red">如果两者的文件名不一致，在核心配置文件中的<mappers>标签中就不能使用<package>标签去扫包，只能使用<mapper>去挨个扫描具体文件了！</font>
  > 2. 该Mapper接口文件最好与我们写的sql映射文件（xxxMapper.xml）放在同一个目录下（只要保证打包之后在一个目录下就行！）。便于修改成包扫描的方式去配置加载sql映射文件。
  > 3. sql映射文件的namespace为该Mapper接口文件的全路径名称
  > 4. 在Mapper接口文件中的方法名一一对应sql映射文件中的id，并保证参数类型和返回值类型的一致

  - 在项目目录中新增package名为mappers

  - 在mappers目录中新增TeMapper.java的接口文件，内容如下

    ```java
    public interface TeMapper {
    
        List<TeEntity> selectAll(); //这儿的方法名必须与sql映射文件的id一致
    }
    
    ```

  - 修改sql映射文件TeMapper.xml

    - 在resources中新增目录

      > 注意：
      >
      > 1. 新增的目录与代理文件的路径一致
      > 2. 在resources中新增路径不要用`.`的方式分隔，要用`/`的方式分隔，虽然idea最后都会识别为`xxx.xxx.xxx`，但是在打成jar包的时候是不一样的

      把TeMapper.xml移动到新增的目录中

    - 修改代码

      ```xml
      <?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE mapper
              PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
              "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
      
      <!-- namespace参数，指定命名空间，一定要入代理Mapper接口文件全路径一致-->
      <mapper namespace="com.example.demo11.mappers.TeMapper">
          <!-- select标签是指定下面的sql语句时select语句 -->
          <select id="selectAll" resultType="com.example.demo11.entity.TeEntity"> <!-- id是下面的sql语句的唯一标识 resultType为结果集数据类型，会自动把数据库搜索出来的数据包装成指定的resultType类型 -->
              select * from te
          </select>
      </mapper>
      ```

​					

​					最终的目录结构如下图

​					<img src="../images/WechatIMG512.jpeg" alt="avatar" style="zoom: 50%;" />



- 修改`mybatis-config.xml`文件中加载sql映射文件的路径

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
  
                  <!-- 下面的配置是连接mysql的信息 -->
                  <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                  <property name="url" value="jdbc:mysql://localhost:3306/tw?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai"/>
                  <property name="username" value="root"/>
                  <property name="password" value="tangwei123456"/>
  
              </dataSource>
          </environment>
      </environments>
      <mappers>
          <!-- 这儿是需要加载哪些sql的映射文件 -->
          <mapper resource="./com/example/demo11/mappers/TeMapper.xml"/>
      </mappers>
  </configuration>
  ```

  因为使用了接口代理的方式，所以所有的sql映射文件都会放在统一的目录中，`mybatis-config.xml`文件中就可以使用扫描映射文件所在包路径的方式加载所有的sql映射文件

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <environments default="development">
          <environment id="development">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
  
                  <!-- 下面的配置是连接mysql的信息 -->
                  <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                  <property name="url" value="jdbc:mysql://localhost:3306/tw?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=Asia/Shanghai"/>
                  <property name="username" value="root"/>
                  <property name="password" value="tangwei123456"/>
  
              </dataSource>
          </environment>
      </environments>
      <mappers>
          <!-- 这儿是需要加载哪些sql的映射文件 -->
  <!--        <mapper resource="./com/example/demo11/mappers/TeMapper.xml"/>-->
          <package name="com.example.demo11.mappers"/>
      </mappers>
  </configuration>
  ```

  

- 修改test类中的调用方式

  ```java
   //1. 创建sqlSessionFactory对象
  String resource = "mybatis-config.xml";
  InputStream inputStream = Resources.getResourceAsStream(resource);
  SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  
  //2. 获取sqlSession对象，用它来执行sql
  SqlSession sqlSession = sqlSessionFactory.openSession();
  
  //3. 执行sql语句
  // 这儿的 返回值是在TeMapper.xml中的resultType中定义好的
  // 这儿的 test是TeMapper.xml文件中定义的namespace
  // 这儿的 selectAll是在TeMapper.xml文件中的id="selectAll"定义的唯一标识
  // List<TeEntity> teEntityList = sqlSession.selectList("test.selectAll");
  
  TeMapper teMapper = sqlSession.getMapper(TeMapper.class); //获取到代理接口类
  List<TeEntity> teEntityList = teMapper.selectAll(); //指定代理接口类中的方法
  
  System.out.println(teEntityList);
  
  //4. 断开连接释放资源
  sqlSession.close();
  ```

  

  

