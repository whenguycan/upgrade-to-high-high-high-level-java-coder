## springboot集成es



#### 1、springboot与es的版本对应关系

参考：https://docs.spring.io/spring-data/elasticsearch/docs/5.0.x/reference/html/

注意，上面的url对应的版本是可选的，查看具体的版本需要去对应版本查看。





#### 2、引入springboot依赖

- 在项目的pom.xml文件中，指定es依赖包的仓库地址：

  ```xml
  <repositories>    
      <repository>
          <id>es-snapshots</id>
          <name>elasticsearch snapshot repo</name>
          <url>https://snapshots.elastic.co/maven/</url>
      </repository>
  </repositories>
  ```

- 引入依赖

  ```xml
  <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-high-level-client</artifactId>
      <version>7.12.1</version>
  </dependency>
  
  
  //下面的两个依赖，需要具体去看elasticsearch-rest-high-level-client中是否有引入，没有引入则需要手动引入
  <dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-client</artifactId>
      <version>7.12.1</version>
  </dependency>
  
  <dependency>
      <groupId>org.elasticsearch</groupId>
      <artifactId>elasticsearch</artifactId>
      <version>7.12.1</version>
  </dependency>
  
  ```



#### 3、新增一个es的config类

> 用于初始化es的连接

```java
@Configuration
public class MyEsConfig {

    @Bean
    public RestHighLevelClient getEsClient(){
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("172.16.4.134", 9200, "http"));
        return new RestHighLevelClient(restClientBuilder);
    }

}

```





#### 4、在需要的地方使用RestHighLevelClient

```java
@Autowired
public  RestHighLevelClient esmClient;

```

