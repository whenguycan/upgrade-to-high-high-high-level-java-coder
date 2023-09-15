## springboot集成jwt使用secret的方式生成和解析token



#### 1、引入jwt依赖

```xml
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.3.0</version>
</dependency>
```



#### 2、使用secret的方式生成token

```java
try {
    Algorithm algorithm = Algorithm.HMAC256("secret");//这儿填写的字符串是参与加密的字符串，本地需要保留好
    String token = JWT.create()
            .withIssuer("tangwei")
            .withExpiresAt(new Date(System.currentTimeMillis() +  60 * 1000)) //一分钟过期
            .withClaim("username", "tangwei")//往payload中写入数据
            .sign(algorithm);

    System.out.println(token);
} catch (JWTVerificationException exception){
        //Invalid signature/claims
}
```



#### 3、对使用secret方式生成的token解析

```java
try {
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0YW5nd2VpIiwiZXhwIjoxNjE4MzkxNzYzLCJqdGkiOiJsYW5nIiwidXNlcm5hbWUiOiJ0YW5nd2VpIn0.QL0HFWckRYqnVJihIAjzwRvpM3KToJmfsw5sIaFzV1Q";
    Algorithm algorithm = Algorithm.HMAC256("secret");
    JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("tangwei") //这有这个用户的token才会解析token内容
            .build(); //Reusable verifier instance
    DecodedJWT jwt = verifier.verify(token);

    System.out.println(jwt.getClaim("username").asString());//解析到payload中的数据

}catch (JWTVerificationException exception){
    System.out.println(exception.getMessage());
}

```

