## springboot过滤器器的使用



#### 使用注意点

1. 过滤器工作在请求到达servlet之前

   ![avatar](../../images/112233.webp)

2. 过滤器一定要实现springboot提供的Filter接口，并实现doFilter方法

3. 在springboot中过滤器也要注册成为一个bean，才能正常使用

4. 过滤器应该还没有到servlet，所以，过滤器如果要返回数据给用户，需要手动操作`ServletResponse`对象。



#### 使用案例

```java
@Component
public class CheckLienceFilter implements Filter {



    @Value("${czdx.slatcode}")
    private String slatcode;

    @Value("${czdx.lience.path}")
    private String liencePath;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletResponse.setCharacterEncoding("utf-8");

        String signstr = "";
        try{
            // 读取加密字串
            FileReader signFile = new FileReader(liencePath);
            signstr = signFile.readString();
        }catch (IORuntimeException exception){
            HashMap<String, String> responseData = new HashMap<>();
            responseData.put("status", "333");
            responseData.put("message", "授权码文件不存在，无法提供服务");
            String jsonStr = JSONUtil.toJsonStr(responseData);
            servletResponse.getWriter().println(jsonStr);
            return;
        }

        //读取私钥文件
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = null;
        try {
            String utf8StringPrik = FileUtil.readUtf8String("czdx/private.pem");
            byte[] privateKeyBytes = utf8StringPrik.getBytes();
            pkcs8EncodedKeySpec =
                    new PKCS8EncodedKeySpec(Base64.decode(privateKeyBytes));
        }catch (IORuntimeException exception){
            HashMap<String, String> responseData = new HashMap<>();
            responseData.put("status", "333");
            responseData.put("message", "私钥文件不存在，无法提供服务");
            String jsonStr = JSONUtil.toJsonStr(responseData);
            servletResponse.getWriter().println(jsonStr);
            return;
        }


        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] result = cipher.doFinal(Base64.decode(signstr));
            String res = new String(result);

            Map<String, Object> map = JSONUtil.parseObj(res);

            String machineCode = (String) map.get("machineCode");
            String uniqueCode = SecureUtil.sha1(SecureUtil.md5(ServerUtils.getCpuSerial() + slatcode));
            if(!machineCode.equals(uniqueCode)){
                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("status", "333");
                responseData.put("message", "尚未授权的服务器，系统无法提供服务");
                String jsonStr = JSONUtil.toJsonStr(responseData);
                servletResponse.getWriter().println(jsonStr);

                return;
            }

            String expireAt = (String) map.get("expireAt");
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expireDate = dateFormat.parse(expireAt);
            int r = now.compareTo(expireDate);
            if(r > 0 ){

                HashMap<String, String> responseData = new HashMap<>();
                responseData.put("status", "333");
                responseData.put("message", "授权码过期，系统无法提供服务");
                String jsonStr = JSONUtil.toJsonStr(responseData);
                servletResponse.getWriter().println(jsonStr);

                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
```

