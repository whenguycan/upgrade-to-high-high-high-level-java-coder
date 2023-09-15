## mirror标签

> mirror相当于一个拦截器，它会拦截maven在settings.xml文件中的<profile>标签中对repository的相关请求，把请求里的repository地址，重定向到mirror里配置的地址。如果repository的id和mirror的mirrorOf的值匹配，则该mirror替代该repository，如果该repository找不到对应的mirror，则使用其本身。

如果配置了多个mirror，匹配到一个mirror后，后续的配置就没有作用了。



#### 使用范例

```xml
<mirror>
  <id>nexus.yyc.cn</id> <!-- 这个id不能随便写，需要与要使用的server标签的id保持一致 -->
  <!--profile中的repository.id-->
  <mirrorOf>central</mirrorOf>
  <url>https://nexus.yyc.cn/repository/maven-public/</url>
</mirror>
```



#### 范例详解

- 标签详解

  1. <mirrorOf>标签，有如下几种值

     “*”：表示拦截所有 <repositories>下配置的远程下载请求。

     “external:*”：表示拦截所有本地没有且 <repositories>未配置的请求，换句话说，就是前面都查过，但是查不到的情况下，还会继续从<mirror>中定义的url下载。

     “repo,repo1”：表示拦截指定 <repositories>名称的远程地址请求，从<mirror>中定义的url下载。

     “*,!repo1”：表示拦截除了 <repositories>名称为repo1的远程地址请求，其他均从<mirror>中定义的url下载。