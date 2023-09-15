## DDL语句



#### 1，DDL语句操作数据库

我们先来学习DDL来操作数据库。而操作数据库主要就是对数据库的增删查操作。

##### 1.1  查询

查询所有的数据库

```sql
SHOW DATABASES;
```

运行上面语句效果如下：

<img src="../../images/image-20210721221107014.png" alt="image-20210721221107014" style="zoom:80%;" />

上述查询到的是的这些数据库是mysql安装好自带的数据库，我们以后不要操作这些数据库。

##### 1.2  创建数据库

* **创建数据库**：

```sql
CREATE DATABASE 数据库名称;
```

运行语句效果如下：

<img src="../../images/image-20210721223450755.png" alt="image-20210721223450755" style="zoom:80%;" />

而在创建数据库的时候，我并不知道db1数据库有没有创建，直接再次创建名为db1的数据库就会出现错误。

<img src="../../images/image-20210721223745490.png" alt="image-20210721223745490" style="zoom:80%;" />

为了避免上面的错误，在创建数据库的时候先做判断，如果不存在再创建。

* **创建数据库(判断，如果不存在则创建)**

```sql
CREATE DATABASE IF NOT EXISTS 数据库名称;
```

运行语句效果如下：

<img src="../../images/image-20210721224056476.png" alt="image-20210721224056476" style="zoom:80%;" />

从上面的效果可以看到虽然db1数据库已经存在，再创建db1也没有报错，而创建db2数据库则创建成功。

##### 1.3  删除数据库

* **删除数据库**

```sql
DROP DATABASE 数据库名称;
```

* **删除数据库(判断，如果存在则删除)**

```sql
DROP DATABASE IF EXISTS 数据库名称;
```

运行语句效果如下：

<img src="../../images/image-20210721224435251.png" alt="image-20210721224435251" style="zoom:80%;" />

##### 1.4  使用数据库

数据库创建好了，要在数据库中创建表，得先明确在哪儿个数据库中操作，此时就需要使用数据库。

* **使用数据库**

```sql
USE 数据库名称;
```

* **查看当前使用的数据库**

```sql
SELECT DATABASE();
```

运行语句效果如下：

<img src="../../images/image-20210721224720841.png" alt="image-20210721224720841" style="zoom:80%;" />





#### 2，DDL:操作表

操作表也就是对表进行增删改查操作。

##### 2.1  查询表

* **查询当前数据库下所有表名称**

```sql
SHOW TABLES;
```

我们创建的数据库中没有任何表，因此我们进入mysql自带的mysql数据库，执行上述语句查看

![image-20210721230202814](../../images/image-20210721230202814.png)

* **查询表结构**

```sql
DESC 表名称;
```

查看mysql数据库中func表的结构，运行语句如下：

<img src="../../images/image-20210721230332428.png" alt="image-20210721230332428" style="zoom:80%;" />

##### 2.2  创建表

* **创建表**

```sql
CREATE TABLE 表名 (
	字段名1  数据类型1,
	字段名2  数据类型2,
	…
	字段名n  数据类型n
);

```

> 注意：最后一行末尾，不能加逗号

知道了创建表的语句，那么我们创建创建如下结构的表

<img src="../../images/image-20210721230824097.png" alt="image-20210721230824097" style="zoom:80%;" />

```sql
create table tb_user (
	id int,
    username varchar(20),
    password varchar(32)
);
```

运行语句如下：

<img src="../../images/image-20210721231142326.png" alt="image-20210721231142326" style="zoom:80%;" />

##### 2.3  数据类型

MySQL 支持多种类型，可以分为三类：

* 数值

  ```sql
  tinyint : 小整数型，占一个字节
  int	： 大整数类型，占四个字节
  	eg ： age int
  double ： 浮点类型
  	使用格式： 字段名 double(总长度,小数点后保留的位数)
  	eg ： score double(5,2)   
  ```

* 日期

  ```sql
  date ： 日期值。只包含年月日
  	eg ：birthday date ： 
  datetime ： 混合日期和时间值。包含年月日时分秒
  ```

* 字符串

  ```sql
  char ： 定长字符串。
  	优点：存储性能高
  	缺点：浪费空间
  	eg ： name char(10)  如果存储的数据字符个数不足10个，也会占10个的空间
  varchar ： 变长字符串。
  	优点：节约空间
  	缺点：存储性能底
  	eg ： name varchar(10) 如果存储的数据字符个数不足10个，那就数据字符个数是几就占几个的空间	
  ```

> 注意：其他类型参考资料中的《MySQL数据类型].xlsx》

**案例：**

```
需求：设计一张学生表，请注重数据类型、长度的合理性
	1. 编号
	2. 姓名，姓名最长不超过10个汉字
	3. 性别，因为取值只有两种可能，因此最多一个汉字
	4. 生日，取值为年月日
	5. 入学成绩，小数点后保留两位
	6. 邮件地址，最大长度不超过 64
	7. 家庭联系电话，不一定是手机号码，可能会出现 - 等字符
	8. 学生状态（用数字表示，正常、休学、毕业...）
```

语句设计如下：

```sql
create table student (
	id int,
    name varchar(10),
    gender char(1),
    birthday date,
    score double(5,2),
    email varchar(15),
    tel varchar(15),
    status tinyint
);
```

##### 2.4  删除表

* **删除表**

```sql
DROP TABLE 表名;
```

* **删除表时判断表是否存在**

```sql
DROP TABLE IF EXISTS 表名;
```

运行语句效果如下：

<img src="../../images/image-20210721235108267.png" alt="image-20210721235108267" style="zoom:80%;" />

##### 2.5  修改表

* **修改表名**

```sql
ALTER TABLE 表名 RENAME TO 新的表名;

-- 将表名student修改为stu
alter table student rename to stu;
```

* **添加一列**

```sql
ALTER TABLE 表名 ADD 列名 数据类型;

-- 给stu表添加一列address，该字段类型是varchar(50)
alter table stu add address varchar(50);
```

* **修改数据类型**

```sql
ALTER TABLE 表名 MODIFY 列名 新数据类型;

-- 将stu表中的address字段的类型改为 char(50)
alter table stu modify address char(50);
```

* **修改列名和数据类型**

```sql
ALTER TABLE 表名 CHANGE 列名 新列名 新数据类型;

-- 将stu表中的address字段名改为 addr，类型改为varchar(50)
alter table stu change address addr varchar(50);
```

* **删除列**

```sql
ALTER TABLE 表名 DROP 列名;

-- 将stu表中的addr字段 删除
alter table stu drop addr;
```

