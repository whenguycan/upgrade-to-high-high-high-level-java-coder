## 子聚合aggs关键字

子聚合是在上一次聚合数据的前提下再进行一次聚合！

#### 1、使用范例

```json
{
    "query":{
        "match_all":{}
    },

    "aggs":{
        "brand_agg":{
            "terms":{
                "field":"goodsBrandId",
                "size":3
            },
            "aggs":{//这儿定义一个子聚合，它是在上面terms聚合的结果之下再进行一次聚合，就是 根据goodsBrandId聚合得到的数据再根据goodsPrice再聚合一次。 
                "brand_name_agg":{
                    "terms":{
                        "field":"goodsBrandName",
                        "size":1
                    }
                },
              
                "这儿还可以再写自聚合":{ //这儿还可以再写自聚合
              			"terms":{
                      ......
                    }
            		}
            }
        }
    }
}
```





#### 2、范例详述

在一个aggs成功之后，<font color="red">在成功数据的基础上</font>再进行一次aggs，结果如下：

```json
{
    "took": 670,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        ....... //查询的结果
    },
    "aggregations": {
        "brand_agg": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 3,
            "buckets": [
                {
                    "key": 1,
                    "doc_count": 1,
                    "brand_name_agg": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "",
                                "doc_count": 1
                            }
                        ]
                    }
                },
                {
                    "key": 2,
                    "doc_count": 1,
                    "brand_name_agg": {  //自聚合的结果
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "",
                                "doc_count": 1
                            }
                        ]
                    }
                },
                {
                    "key": 3,
                    "doc_count": 1,
                    "brand_name_agg": {
                        "doc_count_error_upper_bound": 0,
                        "sum_other_doc_count": 0,
                        "buckets": [
                            {
                                "key": "",
                                "doc_count": 1
                            }
                        ]
                    }
                }
            ]
        }
    }
}
```

