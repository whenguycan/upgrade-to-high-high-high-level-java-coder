## XxxRoutePredicateFactory内部方法详解

> 实现自定义的Predicate必看



#### 一、XxxRoutePredicateFactory类中必须有一个静态内部类（Config类）

>  类中存放Predicate所需配置，一般就是一些属性和get/set方法



#### 二、XxxRoutePredicateFactory类必须继承抽象类AbstractRoutePredicateFactory

> AbstractRoutePredicateFactory的泛型为XxxRoutePredicateFactory类内部的Config类



#### 三、重写shortcutType和shortcutFieldOrder方法

> 这两个方法的作用在于，从配置文件中解析数据，并按格式设置到Config类内部的属性上。



- shortcutType方法，有3个可选值可返回：
  1. ShortcutType.DEFAULT   将Map中的参数逐一映射到Config类的属性中
  2. ShortcutType.GATHER_LIST  将Map中的参数聚合成一个List, 赋值给shortcutFieldOrder方法第0个元素指定的参数名
  3. ShortcutType.GATHER_LIST_TAIL_FLAG 如果最后一个参数是boolean值，则将这个参数赋值给shortcutFieldOrder方法第1个元素指定的参数名, 其它值聚合成一个List赋值给shortcutFieldOrder方法第0个元素指定的参数名



举例说明，shortcutType、shortcutFieldOrder方法 与Config内部类的关系：

配置文件配置如下：

```yaml
spring:
  cloud:
    gateway:
      routes:
      		......
          predicates:
            - XXX=name1,name2,true
```



使用ShortcutType.DEFAULT:

```java
ShortcutType shortcutType() {
    return ShortcutType.DEFAULT;
}

default List<String> shortcutFieldOrder() {
    return [arg1, arg2, arg3]
}

class Config {
    // name1
    private String arg1;
    // name2
    private String arg2;
    // true
    private boolean arg3;
}
```





使用ShortcutType.GATHER_LIST:

```java
ShortcutType shortcutType() {
    return ShortcutType.GATHER_LIST;
}

default List<String> shortcutFieldOrder() {
    return [args]
}

class Config {
    // [name1,name2,true]
    private List<String> args;
}
```



使用ShortcutType.GATHER_LIST_TAIL_FLAG:

```java
ShortcutType shortcutType() {
    return ShortcutType.GATHER_LIST_TAIL_FLAG;
}

default List<String> shortcutFieldOrder() {
    return [args, flag]
}

class Config {
    // [name1,name2]
    private List<String> args;
    // true
    private boolean flag;
}
```





#### 四、实现apply方法，方法内部创建GatewayPredicate匿名内部类

> GatewayPredicate匿名内部类中的test方法，就是执行断言，成功返回true，失败返回false。<font color="red">返回true就会被这层断言拦截，不再进行后续的断言执行，返回false就进入下一层断言的判断。</font>

例子：

```java
@Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                URI uri = serverWebExchange.getRequest().getURI();
                return uri.getPath().startsWith(config.getCheckToken());//从config中取出来对比
            }


        };
    }
```





#### 五、如果是自定义RoutePredicateFactory，需要将XxxRoutePredicateFactory注册到Bean中



#### 六、完整示例

自定义断言代码

```java
@Component
public class CheckTokenRoutePredicateFactory extends AbstractRoutePredicateFactory<CheckTokenRoutePredicateFactory.Config> {

    public CheckTokenRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                URI uri = serverWebExchange.getRequest().getURI();
                return uri.getPath().startsWith(config.getCheckToken());
            }


        };
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.DEFAULT;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        ArrayList<String> list = new ArrayList<>();
        list.add("checkToken");
        return list;
    }

    public static class Config{
        public String checkToken;

        public String getCheckToken() {
            return checkToken;
        }

        public void setCheckToken(String checkToken) {
            this.checkToken = checkToken;
        }
    }
}

```

配置使用自定义断言

```yaml
spring:
  application:
    name: gateway
  cloud:
    nacos:
      discovery:
        password: nacos
        username: nacos
        server-addr: 172.16.4.144:8808
    gateway:
      routes:
        - id: User
          uri: lb://User
          predicates:
            - CheckToken=/user/**    #注意：这儿CheckToken来自CheckTokenRoutePredicateFactory类名中的CheckToken
          filters:
            - RewritePath=/user(?<segment>/?.*), $\{segment}
server:
  port: 80

debug: true
```



