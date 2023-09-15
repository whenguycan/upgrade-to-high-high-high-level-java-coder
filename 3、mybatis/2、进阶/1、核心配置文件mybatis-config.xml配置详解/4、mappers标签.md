## mappers标签

用于加载sql映射文件的配置



- 扫描具体的sql映射文件：

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
     
      <mappers>
          <!-- 这儿是需要加载哪些sql的映射文件 -->
          <mapper resource="./com/example/demo11/mappers/TeMapper.xml"/>
        
        	<mapper resource="./com/example/demo11/mappers/TeMapper2.xml"/>
      </mappers>
  </configuration>
  ```

- 扫描一个包中所有的sql映射文件

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "https://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
    <mappers>
      <package name="com.example.demo11.mappers"/>
    </mappers>
  </configuration>
  ```

  

