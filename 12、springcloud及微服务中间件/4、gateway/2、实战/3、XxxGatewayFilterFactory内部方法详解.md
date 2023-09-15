## XxxGatewayFilterFactory内部方法详解

> 实现自定义的Filter必看



#### 一、XxxGatewayFilterFactory类中必须有一个静态内部类（Config类）

> 类中存放Filter所需配置，一般就是一些属性和get/set方法

#### 二、XxxGatewayFilterFactory类必须继承抽象类AbstractGatewayFilterFactory

> AbstractGatewayFilterFactory的泛型为XxxGatewayFilterFactory类内部的Config类

#### 三、重写shortcutFieldOrder方法

> 这方法的作用在于，从配置文件中解析数据，并按格式设置到Config类内部的属性上。



举例说明，shortcutFieldOrder方法 与Config内部类的关系：

yaml配置：

```yaml
spring:
  application:
    name: gateway
  cloud:
    gateway:
      routes:
        - id: auth
          uri: lb://auth
          predicates:
            - Path=/api/auth/**
          filters:
            - RewritePath=/api/auth(?<segment>/?.*), $\{segment}
```



Config类：

> config类 至少也需要一个属性，千万不能为空class，否则会报错的！

```java
public static class Config {

		private String regexp;

		private String replacement;

		public String getRegexp() {
		}

		public Config setRegexp(String regexp) {
			
		}

		public String getReplacement() {
		}

		public Config setReplacement(String replacement) {
			
		}

	}
```

shortcutFieldOrder方法

```java
@Override
public List<String> shortcutFieldOrder() {
  return Arrays.asList("regexp", "replacement");//对应Config类中的regexp属性、replacement属性。会分别把yaml配置文件中/api/auth(?<segment>/?.*)读取到放到regexp属性、$\{segment}读取到replacement属性。
}
```



#### 四、实现apply方法，方法内部创建GatewayFilter匿名内部类

> GatewayFilter匿名内部类中的filter方法，就是执行过滤的，过滤之后，如果修改了request，需要返回chain.filter(exchange.mutate().request(request).build())，如果没有修改request，需要返回chain.filter(exchange) 好让过滤器链往下执行

例子:

```java
@Override
	public GatewayFilter apply(Config config) {
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange,
					GatewayFilterChain chain) {
				ServerHttpRequest req = exchange.getRequest();
				addOriginalRequestUrl(exchange, req.getURI());
				String path = req.getURI().getRawPath();
				String newPath = path.replaceAll(config.regexp, replacement);

				ServerHttpRequest request = req.mutate().path(newPath).build();

				exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());

				return chain.filter(exchange.mutate().request(request).build());
			}
		};
	}
```



#### 五、如果是自定义GatewayFilterFactory，需要将XxxGatewayFilterFactory注册到Bean中





#### 六、完整示例

```java
@Component
public class CheckTokenToGatewayFilterFactory extends AbstractGatewayFilterFactory<CheckTokenToGatewayFilterFactory.Config> {

    public CheckTokenToGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                System.out.println("地址为：" + exchange.getRequest().getURI().toString());
                return chain.filter(exchange);
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("need");
    }

    public static class Config{
        private String need;

        public String getNeed() {
            return need;
        }

        public void setNeed(String need) {
            this.need = need;
        }
    }
}
```



