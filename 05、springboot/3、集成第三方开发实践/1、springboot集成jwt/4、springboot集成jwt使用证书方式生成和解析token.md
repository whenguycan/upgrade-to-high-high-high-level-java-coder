## springboot集成jwt使用证书方式生成和解析token



#### 1、引入依赖

```xml
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.3.0</version>
</dependency>
```





#### 2、生成证书

1. 生成一对秘钥： 

   openssl genrsa -out mykey.pem 2048

2. 生成私钥：

   openssl pkcs8 -topk8 -inform PEM -outform PEM -in mykey.pem -out private_key.pem -nocrypt

3. 生成公钥：

   openssl rsa -in mykey.pem -pubout -outform DER -out public_key.der

将生成的公钥和私钥拷贝到项目的resources目录中。





#### 3、java生成和解析token

```java
public  PrivateKey getPemPrivateKey() throws Exception {    
  ClassPathResource classPathResource = new ClassPathResource("private_key.pem");
  File f = classPathResource.getFile();
  FileInputStream fis = new FileInputStream(f);
  DataInputStream dis = new DataInputStream(fis);
  byte[] keyBytes = new byte[(int) f.length()];
  dis.readFully(keyBytes);
  dis.close();

  String temp = new String(keyBytes);
  String privKeyPEM = temp.replace("-----BEGIN PRIVATE KEY-----\n", "");
  privKeyPEM = privKeyPEM.replace("-----END PRIVATE KEY-----", "");
  //System.out.println("Private key\n"+privKeyPEM);

  Base64 b64 = new Base64();
  byte [] decoded = b64.decode(privKeyPEM);

  PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
  KeyFactory kf = KeyFactory.getInstance("RSA");
  return kf.generatePrivate(spec);
}

public  PublicKey getPemPublicKey() throws Exception {
  ClassPathResource classPathResource = new ClassPathResource("public_key.der");
  File f = classPathResource.getFile();
  FileInputStream fis = new FileInputStream(f);
  DataInputStream dis = new DataInputStream(fis);
  byte[] keyBytes = new byte[(int) f.length()];
  dis.readFully(keyBytes);
  dis.close();

  X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
  KeyFactory kf = KeyFactory.getInstance("RSA");
  return kf.generatePublic(spec);
}

@GetMapping("/jwt")
public void testJwt() throws Exception {//这个方法是生成token

  RSAPublicKey publicKey = (RSAPublicKey)getPemPublicKey();//Get the key instance
  RSAPrivateKey privateKey = (RSAPrivateKey) getPemPrivateKey();//Get the key instance
  try {
    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    String token = JWT.create()
      .withIssuer("auth0")
      .withClaim("username", "tangwei")
      .sign(algorithm);
    System.out.println(token);
  } catch (JWTCreationException exception){
    System.out.println("Invalid Signing configuration / Couldn't convert Claims.");
    //
  }

}

@GetMapping("/checkJwt")
public void checkJwt(HttpServletRequest request) throws Exception {//这个方法是验证token和解析token数据
  String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsInVzZXJuYW1lIjoidGFuZ3dlaSJ9.hsB_33D_2hlbXBxRbWOZWEWcl3gL7BMC9tjFR73-1VnLhCHgYoJyHSoDRkRTWqulcCid8t_TuxQUfSALNWo37DxFto372BwNCjNYbhPI9uKSU0nW362WykfAmQ6-uTt8BRgDDyfw16tIgizFYVmq1ZBP0dvQHQxkZZDJou9g2LyUnCC_KGEhvf9YbIgUFstkLvvU42A2a_viaX_-Lduz0qi8h9aO8QlL-VgF0mW5hkJDDcNzXOW7ZmycbgxRUWh8ywU3AVUDMVxhrMypPjzJ9du-9qtt8a3-JMCz7JLG6ZI-3ZLcRHS1vJnZWP9cWzCrqcigQBNT7dFumP6hXccO8Q";
  RSAPublicKey publicKey = (RSAPublicKey)getPemPublicKey();//Get the key instance
  RSAPrivateKey privateKey = (RSAPrivateKey) getPemPrivateKey();//Get the key instance
  try {
    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    JWTVerifier verifier = JWT.require(algorithm)
      .withIssuer("auth0")
      .build(); //Reusable verifier instance
    DecodedJWT jwt = verifier.verify(token);
    System.out.println(jwt.getClaim("username").asString());
  } catch (JWTVerificationException exception){
    System.out.println("Invalid signature/claims");
  }
}
```





