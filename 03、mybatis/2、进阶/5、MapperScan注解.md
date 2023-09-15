## MapperScan注解

是由ibatis中提供的注解，可以在启动类中添加此注解，作用是指定要扫描的sql映射文件代理接口类的路径，如果使用了MapperScan，那么代理接口类不用都添加@Mapper注解。



#### 具体用法

1. @MapperScan("com.demo.mapper")：扫描指定包中的接口
2. @MapperScan("com.demo.*.mapper")：一个 * 代表一级包；比如可以扫到com.demo.aaa.mapper,不能扫到com.demo.aaa.bbb.mapper
3. @MapperScan("com.demo.**.mapper")：两个 * 代表任意个包；比如可以扫到com.demo.aaa.mapper,也可以扫到com.demo.aaa.bbb.mapper
4. @MapperScan ({ "com.kfit.demo" , "com.kfit.user" }) 扫描多个包
5. @MapperScan ({ "com.kfit.*.mapper" , "org.kfit.*.mapper" }) 扫描多个多级目录