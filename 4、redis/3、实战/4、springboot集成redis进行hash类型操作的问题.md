## Hash操作的问题



#### 问题描述

有没有发现，我们在springboot集成redis之后，代码操作hash类型的数据，都只能设置一个永不过期的hash数据，那如果我的业务中需要设置hash类型数据的过期时间，怎么办？可以先设置hash数据，然后再设置key的过期时间？没错！可以这样操作，但是仔细想想是不是有问题，先设置hash数据再设置key的过期时间，是2次网络IO，网络这东西是不靠谱的！其中一次发生异常了，就会出问题，那怎么办呢？



#### 问题解决

就是如何确保 `先设置hash后设置hash的过期时间`的原子性！我们需要使用`lua`脚本！



```java
String key = "user-info";

HashMap<String, String> hashMap =  new HashMap<>();
hashMap.put("username", "tangwei");
hashMap.put("age", "30");
hashMap.put("role", "manager");
hashMap.put("token", "xxxxx");

Long expire = 120L;

//要执行的lua脚本
String script = "local fieldIndex=1;" +
  "local valueIndex=2;" +
  "local key=KEYS[1];" +
  "local fieldCount=KEYS[2];" +
  "local expired=KEYS[3];" +
  "for i=1,fieldCount,1 do " +
  "redis.pcall('HSET',key,ARGV[fieldIndex],ARGV[valueIndex]) " +
  "fieldIndex=fieldIndex+2 " +
  "valueIndex=valueIndex+2 " +
  "end;" +
  "redis.pcall('EXPIRE',key,expired);";


//设置lua脚本的脚本、以及返回值
DefaultRedisScript<Object> objectDefaultRedisScript = new DefaultRedisScript<>();
objectDefaultRedisScript.setScriptText(script);
objectDefaultRedisScript.setResultType(Object.class);

//设置lua脚本中ARGV需要的参数
ArrayList<String> args = new ArrayList<>();
for (Map.Entry<String, String> entry : hashMap.entrySet()){
  args.add(entry.getKey());
  args.add(entry.getValue());
}

//因为lua脚本没有return，所以这儿的res为null
Object res = stringRedisTemplate.execute(
  objectDefaultRedisScript, 
  Arrays.asList(key, String.valueOf(hashMap.size()), String.valueOf(expire)), 
  args.toArray()
);


```

