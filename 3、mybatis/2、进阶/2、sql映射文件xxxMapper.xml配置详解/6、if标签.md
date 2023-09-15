## if标签

当查询数据的条件不固定，可能有可能没有，那么我们就需要在sql映射文件中判断条件存不存在，如果存在才添加到sql语句上，如果不存在就不要添加到sql语句上，就需要使用if标签。



```xml
<select id="findActiveBlogLike" resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

if标签中的test，是判断的条件。条件成立则会把if标签中的条件加到select的where后面。