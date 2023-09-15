## select 查询语句

> 查询语句用的是最多的，也是最需要优化的。





#### select关键字

表示当前sql语句是一条查询语句，select 后面跟你需要查询的字段，例子如下：

1. select *：表示查询所有字段
1. select id, username：表示查询id、username两个字段
1. select id, username as name: 表示查询id、username两个字段，并给username取个别名叫name
1. select a.id, a.username, b.content：当连表查询的时候，因为是2张表联查，所以会给2张表分别起名为a、b，这时候要从a表中查id、username字段，从b表中查content字段，就要这么写。 



#### from关键字

表示从哪张表读取数据，例子如下：

1. select .... from userInfo：这儿的userInfo就是一张表
2. select .... from (select .... from xxxx)：这儿使用了子查询，将子查询的结果做为父查询的表数据来源



#### where关键字

表示对数据过滤的条件，例子如下：

1. select ... from ... where id > 10;
2. select ... from ... where name = "xxx";



#### or条件连接

- 使用范例

  select * from pd_school_student_topic_log where student_id = 1 or course_id = 6;

- 查询过程

  把select .... from.... where a = xxx or b = xxx拆分为，select .... from.... where a = xxx 和 select .... from.... where b = xxx分别执行，然后取两者结果的并集。

- 结论：

  所以 or 查询一定要对每个字段建立单独的索引，如果其中一个字段没有索引，那么就会走全表扫描得到结果。如果给所有的字段建立组合索引，是没有效果的。

  

#### and条件连接

- 使用范例

  select .... from.... where a = xxx and b = xxx

- 查询过程

  - 如果所有字段建立了组合索引

    select .... from.... where a = xxx and b = xxx，那么是一个整体的执行不会拆分开，先用a定位到具体的叶子节点，然后用b在叶子节点继续过滤数据，直至得到需要的数据。

  - 如果每个字段都有单独自己的索引

    把select .... from.... where a = xxx and b = xxx拆分为，select .... from.... where a = xxx 和 select .... from.... where b = xxx分别执行，会选择性的走其中一个索引。

  - 如果只有其中一个字段有索引，另一个字段没有索引

    select .... from.... where a = xxx and b = xxx，那么是一个整体的执行不会拆分开，先用a定位到具体的叶子节点，然后再用叶子结点获取到的id进行回表查询，查询到的结果再用后面没有索引的字段去过滤。

  - 两个字段都没有索引

    select .... from.... where a = xxx and b = xxx，那么是一个整体的执行不会拆分开，先用select .... from....进行全表扫描，再用a=xxx and b=xxx进行过滤。

- 结论：

  and查询需要根据字段建立索引的情况来确定sql具体的执行方案，



#### in条件连接

- 使用范例

  select ... from .... where userid in (1,2,3,4,5)

- 查询过程

  - 如果对userid建立了索引

    拿着需要找的数据集去索引上挨个遍历。

  - 如果userid没有索引

    遍历所有的数据，逐条判断userid是否在需要找的数据集中。

- 结论：

  in查询，建立索引和没有建立所以是有本质区别的。



#### group by分组关键字

- 使用范例

  select .... from .... where .... group by .... 

- 查询过程

  大多数教程上看到的都是介绍group by需要使用临时表，来收集数据，这样的说法是不对的！创建临时表的效率太低了！

  理论上来说整个sql语句的执行流程是 先执行 where .... 再 group by ... 再having...。但是实际情况下，是有所不同的！

  - 当 where .... 后的字段是主键字段，比如id，不论group by.... 后面的字段有没有索引，查询流程如下：

    1. 创建临时表，其中字段为 select....需要的字段
    2. 使用主键索引，根据where....条件到主键索引中找到select....需要的数据
    3. 遍历查询出来的数据，挨个将需要的字段放入到临时表中，如果有统计数据，比如count()，那么放入到表中的时候 + 1。
    4. 将数据返回给用户

    <font color="red">所以 这儿明确 创建了 创建了临时表、使用了where条件过滤。</font> 

    例子：explain select count(topic_id)as num,topic_id  from pd_school_student_topic_log where id > 6454 group by topic_id;

  - 当 where .... 后的字段不带索引，且group by....后的字段也不带普通索引，流程如下：

    1. 创建临时表，其中字段为 select....需要的字段
    2. 不使用索引遍历所有数据，根据where....条件找到select....需要的数据
    3. 遍历查询出来的数据，挨个将需要的字段放入到临时表中，如果有统计数据，比如count()，那么放入到表中的时候 + 1。
    4. 将数据返回给用户

    <font color="red">所以 这儿明确 创建了临时表、使用了where条件过滤。</font> 

    例子：explain select count(topic_id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id; 此时的student_id没有索引，topic_id也没有带索引。

    

  - 当 where .... 后的字段不带索引，但group by....后的字段带普通索引，流程如下：

    1. 根据group by ...后面的索引到该索引中查询需要的数据
    2. 数据查找到之后做归并操作
    3. 根据where...条件对数据进行过滤，如果过滤条件字段在该索引下不存在，则需要先回表找到该字段的数据，然后再进行过滤

    <font color="red">所以，这儿明确不用创建临时表，使用了where条件过滤</font>

    例子：explain select count(topic_id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id; 此时的student_id没有索引，但topic_id有带索引。

    

  - 当where .... 后的字段带普通索引，且group by....后的字段带普通索引，流程如下：

    1. 根据group by ...后面的索引到该索引中查询需要的数据
    2. 数据查找到之后做归并操作
    3. 根据where...条件对数据进行过滤，如果过滤条件字段在该索引下不存在，则需要先回表找到该字段的数据，然后再进行过滤

    <font color="red">所以，这儿明确不用创建临时表，使用了where条件过滤</font>

    例子：explain select count(topic_id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id; 此时的student_id有索引，但topic_id有带索引。

    

  - 当where .... 后的字段带普通索引，且group by....后的字段不带普通索引，流程如下：

    1. 创建临时表，其中字段为 select....需要的字段
    2. 根据where....条件中的索引，到对应索引中查询数据。<font color="green">此时会发生有趣的事情，明明该字段有索引，但是，explain出来却不走索引，是因为如果我们单独对where....条件的字段建立索引，那么需要查找的字段如果不在该索引中，就需要回表查询数据，当数据量大了，mysql会自动判断，效率如果不如全表扫描，那么就会走全表扫描，不走索引</font>
    3. 遍历查询出来的数据，挨个将需要的字段放入到临时表中，如果有统计数据，比如count()，那么放入到表中的时候 + 1。
    4. 将数据返回给用户

    <font color="red">所以 这儿明确 创建了临时表、使用了where条件过滤。</font> 

    例子：explain select count(id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id;此时的student_id有索引，但topic_id没有带索引。

  - 当where .... 后的字段跟group by....后的字段组成一个组合索引，group by...后面的字段在索引的前部，流程如下：

    1. 根据group by ...后面的索引到该索引中查询需要的数据
    2. 根据where...条件对数据进行过滤，此时的where...条件的字段跟group by的字段组合成了组合索引，那么就只要在索引查询阶段就可以将where....条件带入并进行数据过滤。
    3. 归并数据并返回给用户

    <font color="red">所以，这儿明确不用创建临时表，使用了索引覆盖，使用了where条件过滤</font>

    例子：explain select count(id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id; 此时的topic_id和student_id组成一个组合索引，类似 index(topic_id,student_id)

  - 当where .... 后的字段跟group by....后的字段组成一个组合索引，where .... 后面的字段再索引的前部

    1. 创建临时表，其中字段为 select....需要的字段
    2. 根据where....条件中的索引，到对应索引中查询数据。需要的topic_id也在当前索引中，使用了索引覆盖。
    3. 遍历查询出来的数据，挨个将需要的字段放入到临时表中，如果有统计数据，比如count()，那么放入到表中的时候 + 1。
    4. 将数据返回给用户

    <font color="red">所以，这儿明确不用创建临时表，使用了索引覆盖，使用了where条件过滤</font>

    例子：explain select count(id)as num,topic_id  from pd_school_student_topic_log where student_id > 270 group by topic_id; 此时的topic_id和student_id组成一个组合索引，类似 index(student_id,topic_id)

- 结论：

  要优化group by，就需要把group by的字段跟where条件的字段，组成一个组合索引，且group by的字段在前，where的字段在后。

  

#### having关键字

- 使用范例

  select .... from .... where .... group by .... having....

- 查询过程：

  having...优先级在where和group by之后

- 结论：

  having的目的是对数据归并之后的数据再进行过滤，执行优先级在where和group by之后。



#### order by排序关键字

- 使用范例

  select .... from .... [where ... group by ... having...] order by ...... asc/desc

- 查询过程

  order by .... esc/asc 优先级在所有关键字（除了limit）之后，是对筛选数据做排序工作

- 结论：

  1. order by配合group by一起使用，但凡group by使用了临时表，那么必会出现 Using filesort

  2. order by配合group by一起使用，group by没有使用了临时表，但是排序的字段不在索引中，那么必会出现 Using filesort

  3. <font color="red">order by的字段最好跟前面关键字用到的字段建立联合索引！因为联合索引，在确定了顶层数据的时候，可以确保下层的有序性！</font>

     

#### union与union all关键字

> 用来替代or语句的

- 使用范例

  union使用

  ```sql
  (select * from pd_school_student where id > 2710) union (select * from pd_school_student where id =2709)
  ```

  得到的结果如图

  ![avatar](../../images/WechatIMG453.jpeg)

  会把重复的去掉

  

  union all使用

  ```sql
  (select * from pd_school_student where id < 2710) union all (select * from pd_school_student where id =2709)
  ```

  得到的结果如图

  ![avatar](../../images/WechatIMG452.jpeg)

  不会去重

- 查询过程

  以下面sql为例

  ```sql
  (select * from pd_school_student where id < 2710) union (select * from pd_school_student where id =2709)
  ```

  先执行union后面的sql，在执行union前面的sql，再把结果合并！

- 结论

  一定是拆分开执行的。



#### join关键字

- left join

  - 使用范例

    ```sql
    select 
    
    stu.school_id,stu.grade,stu.class, stu.student_num, stu.name, log.add_time 
    
    from pd_school_student as stu 
    
    left join pd_school_student_topic_log as log on stu.id = log.student_id and log.student_id = 2708 
    
    where stu.id < 2709 
    ```

    

  - 查询效果

    ![avatar](../../images/WechatIMG450.jpeg)

    拿着左表的每一条数据，根据 on 关键字的条件到被关联表中查询。如果on条件成立了，如果被关联表中查到4条数据，那么最终的结果中该条数据变成了4条数据。如果on条件不成立，那么该条数据还是只有1条，不过被关联表中的字段显示为null。

  - 查询的mysql底层过程

    以下面的sql为例

    ```sql
    select 
    
    stu.school_id,stu.grade,stu.class, stu.student_num, stu.name, log.add_time 
    
    from pd_school_student as stu 
    
    left join pd_school_student_topic_log as log on stu.id = log.student_id and log.student_id = 2708 
    
    where stu.id < 2709 
    ```

    先分解sql，得到对pd_school_student操作的sql语句，大致如下

    ```sql
    select stu.id, stu.school_id,stu.grade,stu.class, stu.student_num, stu.name
    
    from pd_school_student as stu
    
    where stu.id < 2709 
    ```

    执行sql

    再然后再分拆sql，得到pd_school_student_topic_log操作的sql语句，大致如下

    ```sql
    select log.add_time 
    
    from pd_school_student_topic_log as log where 遍历pd_school_student表的id=log.student_id and log.student_id = 2708
    ```

    执行sql，注意 <font color="red">这儿的and适用与and关键字</font>

  - 结论：

    1. left join语句的sql，实际上会被拆分开执行

    2. 优化left join语句的sql，分开思考即可，重点是对 on 后面的条件，因为涉及到2张表，所以<font color="red">最好给on后面对应的字段都到各自的表中加上索引，唯一索引或关键字索引最好！</font>

       

- Inner join = join

  - 使用范例

    ```sql
    select 
    
    stu.school_id,stu.grade,stu.class, stu.student_num, stu.name, log.add_time 
    
    from pd_school_student as stu 
    
    inner join pd_school_student_topic_log as log on stu.id = log.student_id and log.student_id = 2708 
    
    where stu.id < 2709 
    ```

  - 查询效果

    ![avatar](../../images/WechatIMG451.jpeg)

    拿着左表的每一条数据，根据 on 关键字的条件到被关联表中查询。如果on条件成立了，如果被关联表中查到4条数据，那么最终的结果中该条数据变成了4条数据。如果on条件不成立，那么该条数据被丢弃。

  - 查询的mysql底层过程：

    参考left join

  - 结论：

    1. Inner join语句的sql，实际上会被拆分开执行
    2. 优化inner join语句的sql，分开思考即可，重点是对 on 后面的条件，因为涉及到2张表，所以<font color="red">最好给on后面对应的字段都到各自的表中加上索引，唯一所以或关键字索引最好！</font>

- right join

  略

#### limit关键字

- 使用范例

  1. select .... from .... [where ... group by ... having... order by ...... ] limit 1
  2. select .... from .... [where ... group by ... having... order by ......] limit 0,10

- 查询过程

  Limit ...... 优先级在所有关键字之后，是对筛选数据做分页显示工作

- 结论

  1. 查询分页使用

  2. limit 0,10 的意思是从第0条数据开始，拿10掉数据。

     

#### 子查询

- 使用范例

  1. select * from pd_school_student_topic_log where student_id in ( select id from pd_school_student where id < 2710) and right_answer = 'A';

- 查询过程

  以下面的sql为例

  ```sql
  select * from pd_school_student_topic_log where student_id in ( select id from pd_school_student where id < 2710) and right_answer = 'A';
  ```

  先将子查询拿到，然后执行，将子查询的结果参与到外面sql的执行，看下图的ref字段

  ![avatar](../../images/WechatIMG454.jpeg)

- 结论