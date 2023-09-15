## String操作



#### 1、设置一个永不过期的数据

对应 redis-cli中的set方法

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.set("username2", "唐炜");
```



#### 2、设置一个带过期时间的数据

对应redis-cli的setex方法

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.set("username2", "唐炜", 30, TimeUnit.SECONDS);
```



#### 3、设置数据时判断数据是否存在,不存在才设置成功，同时也带过期时间

对应redis-cli的setnx方法

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.setIfAbsent("user-age", "30", 30, TimeUnit.SECONDS); //返回一个boolean类型的结果
```



#### 4、批量设置多个数据

对应redis-cli的mset

- 不判断是否存在直接设置

  ```java
  ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
  
  HashMap<String, String> map = new HashMap<>();
  map.put("kangkang", "cc");
  map.put("ceshi", "ceshi");
  
  opsStr.multiSet(map);
  ```

  

- 判断是否存在再设置，map元素中只要有1个存在，整个map都不会被设置！

  ```java
  ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
  
  HashMap<String, String> map = new HashMap<>();
  map.put("kangkang", "cc");
  map.put("ceshi", "ceshi");
  
  opsStr.multiSetIfAbsent(map);
  ```



#### 5、对字符串值进行追加写入

对应redis-cli的append

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.append("username2", "康");
```



#### 6、对数据进行+1、+n、-n操作

对应redis-cli的incre、decre、increby、increbyfloat

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.increment("age"); // 对应incre的+1操作


ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.decrement("age"); // 对应decre的-1操作


ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.increment("age", 10L); // 对应increby的+n操作


ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
opsStr.increment("age", 0.5); // 对应increbyfloat的+n.xx操作
```





#### 7、删除操作

对应redis-cli的del

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
System.out.println(opsStr.getAndDelete("age"));
```



#### 8、获取数据

对应redis-cli的get、mget

```java
ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
System.out.println(opsStr.get("username2")); // 对应redis-cli的get操作



ValueOperations<String, String> opsStr = redisTemplate.opsForValue();
LinkedList<String> li = new LinkedList<>();
li.add("username2");
li.add("kangkang");
System.out.println(opsStr.multiGet(li));//对应redis-cli的mget

```





