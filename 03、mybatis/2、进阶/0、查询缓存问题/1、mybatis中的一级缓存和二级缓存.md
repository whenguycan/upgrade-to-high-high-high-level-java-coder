## mybatis中的查询语句的一级缓存和二级缓存



在mybatis入门的时候，我们写好了mybatis的核心配置文件mybatis-config.xml和sql映射文件，随后我们创建SqlSession去加载核心配置文件去操作数据库！



考虑一个问题，在一个sqlSession中有一条查询sql语句需要被反复执行10次，是不是这10次查询操作都需要去连接到数据库去执行呢？



#### 1、一级缓存

- 一级缓存简介

  MyBatis在开启一个数据库会话时会创建一个新的SqlSession对象，SqlSession对象中会有一个Executor对象，Executor对象中持有一个PerpetualCache对象，PerpetualCache对象中使用HashMap本地缓存，这个HashMap就是一级缓存的载体！

  同一个sqlsession中执行相同的sql查询（相同的sql和参数），第一次会去查询数据库并写到缓存中，第二次从一级缓存中取

- 一级缓存何时清空

  同一个sqlSession中执行commit操作（自动提交下，执行插入、更新、删除都会自动commit的），close操作，则会清空SqlSession中的一级缓存，这样做的目的为了让缓存中存储的是最新的信息，避免脏读。



#### 2、二级缓存

- 二级缓存简介

  Mybatis在创建SqlSessionFactory就会创建二级缓存的载体，其结构为key-value的形式。

  在SqlSessionFactory中，所有的SqlSession共享二级缓存。当SqlSession执行关闭(close)后，才会把该sqlsession一级缓存中的数据添加到二级缓存中sqlSession对应的key为namespace的缓存中。

  查询数据的时候先查询二级缓存，二级缓存中无对应数据，再去查询一级缓存，一级缓存中也没有，最后去数据库查找。

- 二级缓存何时清空

  二级缓存有一个过期时间，默认是一小时，自二级缓存的数据创建的时候开始计时，如果有请求来读取数据，先判断缓存数据是否已经存在一小时及以上了，如果是清除掉这个缓存数据！懒删除的机制！

  

  

  