## update标签

代表更新数据

```xml
<update id="updateAuthorIfNecessary">
  update Author
    set 
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
  where id=#{id}
</update>
```

