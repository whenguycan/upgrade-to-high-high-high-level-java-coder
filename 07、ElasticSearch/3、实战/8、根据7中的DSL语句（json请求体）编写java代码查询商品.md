## 将DSL语句转成java代码

> 整体思路如下



#### 1、dsl语句中是query的查询,我们需要把它转成我们代码的查询

dsl语句

```json
{
  "query":{
    ......
  }
}
```

java代码

```java
public void search() throws IOException {
  SearchRequest searchRequest = new SearchRequest(); //初始化query查询
  searchRequest.indices("test");//需要查询的索引

  

  SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行query查询
}
```



#### 2、dsl语句中query后面是bool，即多条件查询，我们需要把它转成我们代码的查询

dsl语句

```json
{
  "query":{
    "bool":{
      ......
    }
  }
}
```

java代码

```java
public void search() throws IOException {
  SearchRequest searchRequest = new SearchRequest(); //初始化query查询
  searchRequest.indices("test");//需要查询的索引

  SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
  BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
  searchSourceBuilder.query(boolQueryBuilder);//执行条件查询

  searchRequest.source(searchSourceBuilder);
  SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
}
```



#### 3、dsl语句中bool后面是must、filter，我们需要把它转成我们代码的查询

- 转换must到java代码

  - dsl语句

    ```json
    {
      "query":{
        "bool":{
          "must":[
            {
              "match": {
                "goodsTitle": "苹果14"
              }
            }
          ],
          ......
          ......
        }
      }
    }
    ```

  - java代码

    ```java
    public void search() throws IOException {
      SearchRequest searchRequest = new SearchRequest(); //初始化query查询
      searchRequest.indices("test");//需要查询的索引
    
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
      
      
      boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
      searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
      
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
    }
    ```

- 转换filter到java

  - dsl语句

    ```json
    {
      "query":{
        "bool":{
          ....
          "filter":[
            {
              "term": {
                "goodsCateId": 1
              }
            },
            {
              "terms": {
                "goodsBrandId": [
                  "1",
                  "2"
                ]
              }
            },
            {
              "nested": {
                "path": "goodsAttr",
                "query": {
                  "bool": {
                    "must": [
                      {
                        "term": {
                          "goodsAttr.attrId": {
                            "value": 1
                          }
                        }
                      },
                      {
                        "terms": {
                          "goodsAttr.attrValue": [
                            "x",
                            "y"
                          ]
                        }
                      }
                    ]
                  }
                }
              }
            },
            {
              "range": {
                "goodsPrice": {
                  "lte": 10000,
                  "gte": 100
                }
              }
            }
          ]
        }
      }
    }
    ```

  - java代码

    - 转换filter下的第一个元素：term

      - dsl语句

        ```json
        {
          "query":{
            "bool":{
              ....
              "filter":[
                {
                  "term": {
                    "goodsCateId": 1
                  }
                },
          			.....
          			.....
              ]
            }
          }
        }
        ```

      - java代码

        ```java
        public void search() throws IOException {
          SearchRequest searchRequest = new SearchRequest(); //初始化query查询
          searchRequest.indices("test");//需要查询的索引
        
          SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
          BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
        
          
          
          boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
          
        
          
          
          boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
        
          
          
        
        
          searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
        
          
          searchRequest.source(searchSourceBuilder);
          SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
        }
        ```

      

    - 转换filter下的第二个元素：terms

      - dsl语句

        ```json
        {
          "query":{
            "bool":{
              ....
              "filter":[
                
                {
                  "terms": {
                    "goodsBrandId": [
                      "1",
                      "2"
                    ]
                  }
                },
        				......
        				......
              ]
            }
          }
        }
        ```

      - java代码

        ```java
        public void search() throws IOException {
          SearchRequest searchRequest = new SearchRequest(); //初始化query查询
          searchRequest.indices("test");//需要查询的索引
        
          SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
          BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
        
          
          
          boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
          
          
        
          boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
        
          
          
          
          //构建terms的数值的数组
          Long[] l = {1L,2L};
          boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
        
        
          
          
          
        
          searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
        
          
          searchRequest.source(searchSourceBuilder);
          SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
        }
        ```

        

    - 转换filter下的第三个元素：nested

      - dsl语句

        ```json
        {
          "query":{
            "bool":{
              ....
              "filter":[
                
                {
                  "nested": {
                    "path": "goodsAttr",
                    "query": {
                      "bool": {
                        "must": [
                          {
                            "term": {
                              "goodsAttr.attrId": {
                                "value": 1
                              }
                            }
                          },
                          {
                            "terms": {
                              "goodsAttr.attrValue": [
                                "x",
                                "y"
                              ]
                            }
                          }
                        ]
                      }
                    }
                  }
                }
        
              ]
            }
          }
        }
        ```

      - java代码

        ```java
        public void search() throws IOException {
                SearchRequest searchRequest = new SearchRequest(); //初始化query查询
                searchRequest.indices("test");//需要查询的索引
        
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
        
        
        
                boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
        
        
        
                boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
        
        
        
        
                //构建terms的数值的数组
                Long[] l = {1L,2L};
                boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
        
        
                //构建nested内的bool查询
                BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
                nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
                String[] s = {"x", "y"};
                nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
                boolQueryBuilder.filter(nestedQueryBuilder);
        
        
        
        
                searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
          
          
          			searchRequest.source(searchSourceBuilder);
                SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
            }
        ```

      

    - 转换filter下的第四个元素：range

      - dsl语句

        ```json
        {
          "query":{
            "bool":{
              ....
              "filter":[
                ......
                {
                  "range": {
                    "goodsPrice": {
                      "lte": 10000,
                      "gte": 100
                    }
                  }
                }
              ]
            }
          }
        }
        ```

      - java代码

        ```java
        public void search() throws IOException {
          SearchRequest searchRequest = new SearchRequest(); //初始化query查询
          searchRequest.indices("test");//需要查询的索引
        
          SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
          BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
        
          
          
          boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
          
          
        
          boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
        
          
          
          
          //构建terms的数值的数组
          Long[] l = {1L,2L};
          boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
        
          
          
          
          RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
          rangeQueryBuilder.gte("100");
          rangeQueryBuilder.lte("10000");
          boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
        
          
          
        
          searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
          
          
        
          searchRequest.source(searchSourceBuilder);
          SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
        }
        ```

        

      

      

#### 3、分页、高亮、排序

dsl语句

```json
{
  "query":{
    ......
  },
  "sort": [
    {
      "goodsPrice": {
        "order": "desc"
      }
    }
  ],
    "from": 0,
    "size": 10,
    "highlight": {
      "fields": {
        "goodsTitle": {}
      },
      "pre_tags": "<strong>",
      "post_tags": "</strong>"
    },
}
```

java代码

略，这块很简单





#### 4、查询结果聚合

dsl语句

```json
{
    "query":{
      ......
    },
    "aggs": {
      "brand_agg":{
          "terms":{
            "field":"goodsBrandId",
            "size":10
          },
          "aggs":{
            "brand_name_agg":{
              "terms":{
                "field":"goodsBrandName",
                "size":10
              }
            }
          }
        },
      
      "attr_agg": {
        "nested": {
          "path": "goodsAttr"
        },
        "aggs": {
          "attrId_agg": {
            "terms": {
              "field": "goodsAttr.attrId",
              "size": 10
            },
            "aggs": {
              "attr_name_agg": {
                "terms": {
                  "field": "goodsAttr.attrName",
                  "size": 100
                }
              },
              "attr_value_agg": {
                "terms": {
                  "field": "goodsAttr.attrValue"
                }
              }
            }
          }
        }
      }
    }
  
}
```

- 转换第一个聚合即brand_agg到java代码

  - 转换brand_agg的主聚合到java代码

    ```java
    public void search() throws IOException {
      SearchRequest searchRequest = new SearchRequest(); //初始化query查询
      searchRequest.indices("test");//需要查询的索引
    
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
    
    
      boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
      boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
    
    
    
    
      //构建terms的数值的数组
      Long[] l = {1L,2L};
      boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
    
    
      //构建nested内的bool查询
      BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
      nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
      String[] s = {"x", "y"};
      nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
      NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
      boolQueryBuilder.filter(nestedQueryBuilder);
    
    
    
    
    
      RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
      rangeQueryBuilder.gte("100");
      rangeQueryBuilder.lte("10000");
      boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
    
    
    
    
    
      searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
    
      //brand_agg的主聚合
      TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("brand_agg");
      termsAggregationBuilder.field("goodsBrandId");
      termsAggregationBuilder.size(10);
      searchSourceBuilder.aggregation(termsAggregationBuilder);//构建查询结果聚合
    
    
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
    }
    ```

    

  - brand_agg还有子聚合，也需要转换到Java代码

    ```java
    public void search() throws IOException {
      SearchRequest searchRequest = new SearchRequest(); //初始化query查询
      searchRequest.indices("test");//需要查询的索引
    
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
    
    
      boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
      boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
    
    
    
    
      //构建terms的数值的数组
      Long[] l = {1L,2L};
      boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
    
    
      //构建nested内的bool查询
      BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
      nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
      String[] s = {"x", "y"};
      nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
      NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
      boolQueryBuilder.filter(nestedQueryBuilder);
    
    
    
    
    
      RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
      rangeQueryBuilder.gte("100");
      rangeQueryBuilder.lte("10000");
      boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
    
    
    
    
    
      searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
    
      //聚合brand_agg
      TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("brand_agg");
      termsAggregationBuilder.field("goodsBrandId");
      termsAggregationBuilder.size(10);
      //构建子聚合
      TermsAggregationBuilder brandNameaAgg = AggregationBuilders.terms("brand_name_agg");
      brandNameaAgg.field("goodsBrandName");
      brandNameaAgg.size(10);
      termsAggregationBuilder.subAggregation(brandNameaAgg);//将子聚合设置到主聚合中，有多少个子聚合就调用多少次subAggregation设置进去
    
      
      
      searchSourceBuilder.aggregation(termsAggregationBuilder);//构建查询结果聚合，有多少个主聚合就调用多少次aggregation设置进去
    
    
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
    }
    ```

- 转换第一个聚合即attr_agg到java代码

  - 因为它是一个nested的聚合，所以需要先构建一个nested的主聚合

    ```java
    public void search() throws IOException {
      SearchRequest searchRequest = new SearchRequest(); //初始化query查询
      searchRequest.indices("test");//需要查询的索引
    
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
    
    
      boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
      boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
    
    
    
    
      //构建terms的数值的数组
      Long[] l = {1L,2L};
      boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
    
    
      //构建nested内的bool查询
      BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
      nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
      String[] s = {"x", "y"};
      nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
      NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
      boolQueryBuilder.filter(nestedQueryBuilder);
    
    
    
    
    
      RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
      rangeQueryBuilder.gte("100");
      rangeQueryBuilder.lte("10000");
      boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
    
    
    
    
    
      searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
    
      //聚合brand_agg
      TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("brand_agg");
      termsAggregationBuilder.field("goodsBrandId");
      termsAggregationBuilder.size(10);
    
    
      //构建子聚合
      TermsAggregationBuilder brandNameaAgg = AggregationBuilders.terms("brand_name_agg");
      brandNameaAgg.field("goodsBrandName");
      brandNameaAgg.size(10);
      termsAggregationBuilder.subAggregation(brandNameaAgg);//将子聚合设置到主聚合中，有多少个子聚合就调用多少次subAggregation设置进去
    
      searchSourceBuilder.aggregation(termsAggregationBuilder);//构建查询结果聚合
    
    
      //构建nested类型的主聚合
      NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attr_agg", "goodsAttr");
      searchSourceBuilder.aggregation(nestedAggregationBuilder);
    
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
    }
    ```

  - nested的主聚合下有一个子聚合，也需要转换成Java代码

    ```java
    public void search() throws IOException {
      SearchRequest searchRequest = new SearchRequest(); //初始化query查询
      searchRequest.indices("test");//需要查询的索引
    
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
    
    
      boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
      boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
    
    
    
    
      //构建terms的数值的数组
      Long[] l = {1L,2L};
      boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
    
    
      //构建nested内的bool查询
      BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
      nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
      String[] s = {"x", "y"};
      nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
      NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
      boolQueryBuilder.filter(nestedQueryBuilder);
    
    
    
    
    
      RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
      rangeQueryBuilder.gte("100");
      rangeQueryBuilder.lte("10000");
      boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
    
    
    
    
    
      searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
    
      //聚合brand_agg
      TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("brand_agg");
      termsAggregationBuilder.field("goodsBrandId");
      termsAggregationBuilder.size(10);
    
    
      //构建子聚合
      TermsAggregationBuilder brandNameaAgg = AggregationBuilders.terms("brand_name_agg");
      brandNameaAgg.field("goodsBrandName");
      brandNameaAgg.size(10);
      termsAggregationBuilder.subAggregation(brandNameaAgg);//将子聚合设置到主聚合中，有多少个子聚合就调用多少次subAggregation设置进去
    
      searchSourceBuilder.aggregation(termsAggregationBuilder);//构建查询结果聚合
    
    
      //构建nested类型的主聚合
      NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attr_agg", "goodsAttr");
      TermsAggregationBuilder attridagg = AggregationBuilders.terms("attrId_agg").field("goodsAttr.attrId").size(10);//nested主聚合构建
      nestedAggregationBuilder.subAggregation(attridagg);//nested主聚合下的子聚合放进去了
      searchSourceBuilder.aggregation(nestedAggregationBuilder);
    
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
    }
    ```

  - 子聚合下还有子聚合，也需要转换成java代码

    ```java
    public void search() throws IOException {
            SearchRequest searchRequest = new SearchRequest(); //初始化query查询
            searchRequest.indices("products2");//需要查询的索引
    
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery(); //指定是bool类型的多条件查询
    
    
    
            boolQueryBuilder.must(QueryBuilders.matchQuery("goodsTitle", "苹果14")); //这儿就相当于给bool里面放了一个must的查询了！！！
    
    
    
            boolQueryBuilder.filter(QueryBuilders.termQuery("goodsCateId", 1)); //filter下的第一个元素：term
    
    
    
    
            //构建terms的数值的数组
            Long[] l = {1L,2L};
            boolQueryBuilder.filter(QueryBuilders.termsQuery("goodsBrandId", l));// filter下的第二个元素：terms
    
    
            //构建nested内的bool查询
            BoolQueryBuilder nesteBoolQueryBuilder = QueryBuilders.boolQuery();
            nesteBoolQueryBuilder.must(QueryBuilders.termQuery("goodsAttr.attrId", "1"));
            String[] s = {"x", "y"};
            nesteBoolQueryBuilder.must(QueryBuilders.termsQuery("goodsAttr.attrValue", s));
            NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("goodsAttr", nesteBoolQueryBuilder, ScoreMode.None);//filter下的第三个元素：nested 。构建nested的query，ScoreMode.None的意思是不参与评分。
            boolQueryBuilder.filter(nestedQueryBuilder);
    
    
    
    
    
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsPrice");//指定使用goodsPirce做为range查询的字段
            rangeQueryBuilder.gte("100");
            rangeQueryBuilder.lte("10000");
            boolQueryBuilder.filter(rangeQueryBuilder);//filter下的第四个元素： range
    
    
    
    
    
            searchSourceBuilder.query(boolQueryBuilder);//执行条件查询
    
    
            //聚合brand_agg
            TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("brand_agg");
            termsAggregationBuilder.field("goodsBrandId");
            termsAggregationBuilder.size(10);
    
    
            //构建子聚合
            TermsAggregationBuilder brandNameaAgg = AggregationBuilders.terms("brand_name_agg");
            brandNameaAgg.field("goodsBrandName");
            brandNameaAgg.size(10);
            termsAggregationBuilder.subAggregation(brandNameaAgg);//将子聚合设置到主聚合中，有多少个子聚合就调用多少次subAggregation设置进去
    
            searchSourceBuilder.aggregation(termsAggregationBuilder);//构建查询结果聚合
    
    
            //构建nested类型的主聚合
            NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attr_agg", "goodsAttr");
            TermsAggregationBuilder attridagg = AggregationBuilders.terms("attrId_agg").field("goodsAttr.attrId").size(10);//nested主聚合构建
            attridagg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("goodsAttr.attrName").size(10)); //子聚合下的子聚合构建并设置到子聚合中
            attridagg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("goodsAttr.attrValue").size(10)); //子聚合下的子聚合构建并设置到子聚合中
            nestedAggregationBuilder.subAggregation(attridagg);//nested主聚合下的子聚合放进去了
            searchSourceBuilder.aggregation(nestedAggregationBuilder);
    
            System.out.println(searchSourceBuilder.toString());
    
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT); //执行查询
        }
    ```

    

#### 5、从查询的响应中获取数据。

略
