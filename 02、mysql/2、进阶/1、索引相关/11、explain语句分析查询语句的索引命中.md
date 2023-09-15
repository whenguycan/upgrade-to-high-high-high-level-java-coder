## explain的使用

在我们进行sql优化的时候，我们不知道，我们的sql到底会不会走我们创建的索引，所以，我们需要使用explain先查看一下sql语句的执行计划。

explain之后的执行计划，id大的先执行，id为Null（在使用union的时候会出现，代表连接2个结果）





当我们执行一个explain语句的时候，以下面的为例

```sql
mysql> explain select * from user_info where id = 2\G
*************************** 1. row ***************************
           id: 1
  select_type: SIMPLE
        table: user_info
   partitions: NULL
         type: const
possible_keys: PRIMARY
          key: PRIMARY
      key_len: 8
          ref: const
         rows: 1
     filtered: 100.00
        Extra: NULL
1 row in set, 1 warning (0.00 sec)
```

各列的含义如下:

- id: SELECT 查询的标识符. 每个 SELECT 都会自动分配一个唯一的标识符.
- select_type: SELECT 查询的类型.
- table: 查询的是哪个表
- partitions: 匹配的分区
- type: join 类型
- possible_keys: 此次查询中可能选用的索引
- key: 此次查询中确切使用到的索引.
- ref: 哪个字段或常数与 key 一起被使用
- rows: 显示此查询一共扫描了多少行. 这个是一个估计值.
- filtered: 表示此查询条件所过滤的数据的百分比
- extra: 额外的信息



详述select_type

`select_type` 表示了查询的类型, 它的常用取值有:

- SIMPLE, 表示此查询不包含 UNION 查询 或 子查询。

- PRIMARY, 查询中包含任何复杂的子部分，最外层查询被标记为PRIMARY。

  例子：select * from xxx where id in (select distinct id from dha)，那么select * from xxx where id in...这段查询会被标记为PRIMARY。

- DERIVED,在from列表中包含了子查询，这个子查询会被标记为derived。子查询的结果会放到一张临时表里。

  例子：select * from (select * from dept)

- UNION, 表示此查询是 UNION 的第二个或随后的查询

  例子：(select * from dept where id =10) union (select * from dept where id = 11)，那么select * from dept where id = 11就会被标注为UNION

- DEPENDENT UNION, 子查询中的UNION操作，从UNION 第二个及之后的所有SELECT语句的SELECT TYPE为 DEPENDENT UNION。

  例子：select * from sys_user where user_id  in (
  		(select user_id from sys_user where user_id=1)
  		union 
  		(select user_id from sys_user where user_id=2)
  	)

- UNION RESULT, 两个UNION合并的结果，其中的extra使用 using temporary是没法优化的。

- SUBQUERY, 子查询中的第一个 SELECT查询

- DEPENDENT SUBQUERY: 子查询中的第一个 SELECT查询, 子查询依赖于外层SELECT查询的结果。<font color="red">看到这玩意儿一定要小心。</font>

  例子：select * from xxx where id in (select distinct id from dha) and name=xxx 这样的sql语句，mysql在执行的时候，先执行select * from xxx where name=xxx 这条sql语句，查询出需要的数据，如果查出来100万条数据，就会把这100万条数据放到一个结果集中，结果集暂时取名t1，结果集 t1 中的每一条记录，都将与子查询 SQL 组成新的查询语句：select distinct id from dha where id=%t1.id%。等于说，子查询要执行100万次。<font color="red">可以看出执行结果是，子查询依赖于外部查询的结构。</font>



详述type[air rec]

`type`表示了查询使用的索引类型，它的常用取值有：

- ALL，全表扫描。证明没有使用索引，而是扫描了整张表来获取数据。
- index，表示扫描了所有的索引
- range，索引范围扫描，常见于<、<=、>、>=、between、in等操作符，且范围查询的字段有索引存在。
- ref，索引等值查询，但是需要返回匹配的多条数据。对索引有如下要求，非唯一性索引或组合索引（满足极左原则）
- eq_ref，在连表查询（join）时候，使用primary key或者unique key作为关联条件的等值查询。
- const，表中只有一行匹配的记录。一般用在主键索引或者唯一键索引上的等值查询。（如果是多组合索引，则需要等值全匹配）



详述extra[iw ft jb iu]

`extra`表示包含不适合在其他列中显示但十分重要的额外信息，它的常用取值有：

- Using filesort，在使用order by进行排序时，没有使用索引进行排序，就会出现这个。<font color="red">一旦出现这个，可能优化不了！</font>

- Using temporary，mysql在对查询结果分组、排序时，排序、分组字段即使加了索引也会出现这个。<font color="red">一旦出现这个，一定需要优化！</font>

- Using index，表示相应的select操作中使用了覆盖索引(covering index)，避免访问了表的数据行，效率不错！如果同时出现using where，表明索引被用来执行where条件的过滤操作；如果没有同时出现using where，表明索引用来读取数据而非执行查找动作。

- Using index condition,表示没有使用索引覆盖

- Using where，没有根Using index一起出现，表示使用了where....条件对结果进行了过滤，不管是在索引过程中使用还是在索引数据出来之后都是这个结果。

- Using join buffer，在连表查询的时候使用了buffer块，两个表的关联字段没有索引。

- impossible where，where条件有错，根本没有作用。

- select tables optimized away，在没有group by子句的情况下，基于索引优化MIN/MAX操作或者对于MYSQL存储引擎优化COUNT（*）操作，不必等到执行阶段再进行计算，查询执行计划生成的阶段即完成优化。

- distinct，优化distinct操作，在找到第一匹配的元组后即停止找同样的值的动作

- using union，使用了or查询，且where条件的字段都是单独的索引，表示从处理结果获取并集。即便后面的字段是组合索引，也是这个结果。

- using itersect，使用了and查询，且几个字段是单独的索引，表示从处理结果获取交集。

  