## hash操作



#### 1、设置一个永不过期的hash数据

对应redis-cli的hset

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.put("info", "name", "tangwei");
```





#### 2、设置一个永不过期的hash数据，设置之前判断数据是否存在，不存在才能设置成功

对应redis-cli的hsetnx

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.putIfAbsent("info2", "name2", "tangwei");
```





#### 3、批量设置永不过期的hash数据

对应redis-cli的hmset

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();

HashMap<String, String> map = new HashMap<String, String>();
map.put("role", "ce");
map.put("name", "xxx");

opsHash.putAll("info3", map);
```





#### 4、获取一个hash数据

对应redis-cli的hget

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();

opsHash.get("info3", "name");
```



#### 5、获取一个hash数据中的多个key对应的值

对应redis-cli的hmget

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();

LinkedList<String> li = new LinkedList<>();
li.add("name");
li.add("role");

System.out.println(opsHash.multiGet("info3", li));
```



#### 6、获取一个hash中的所有field

对应redis-cli的hkeys

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.keys("info");
```



#### 7、获取一个hash中的所有value

对应redis-cli的hval

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.values("info");
```





#### 8、对hash中的某一个field进行+1、-1、+n操作



#### 9、获取一个hash数据中的field的数量

对应redis-cli的hlen

```java
代码中先获取到hash中所有的keys，然后根据keys的数量得到即可
```





#### 10、判断hash中是否存在一个field

对应redis-cli的hexists

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.hasKey("info", "role");
```



#### 11、删除一个hash中的一个field

对应redis-cli的hdel

```java
HashOperations<String, String, Object> opsHash = redisTemplate.opsForHash();
opsHash.delete("info", "role2");
```











