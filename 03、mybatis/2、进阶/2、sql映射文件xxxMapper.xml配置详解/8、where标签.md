## where标签

如果使用if标签，进行了如下配置

```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  WHERE
  <if test="state != null">
    state = #{state}
  </if>
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

那么当传入的查询条件state为null时，那么sql语句是如下的样子

```sql
SELECT * FROM BLOG WHERE
```

这样明显是报错的，如果传入的查询条件title不为null时，那么sql语句时如下的样子

```sql
SELECT * FROM BLOG WHERE AND title like 'someTitle'
```



此时就需要使用where标签

```xml
<select id="findActiveBlogLike" resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

*where* 标签只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 标签也会将它们去除。