## aggs关键字





#### 1、使用范例

```json
{
    "query":{
        "match_all":{}  //查询所有数据
    },

    "aggs":{  //对查询结果进行聚合分析
        "brand_agg":{ //给当前聚合起一个名字
          
            "聚合条件1":{ 
              "field":"指定字段",
              "size":10
            },
            "聚合条件2":{
              "field":"指定字段",
              "size":10
            }
        },
      	
      	"聚合名称2":{
          "聚合条件":{
            
            "field":"指定字段",
              "size":10
            
            
            
          }
        }
      
    }
}
```





#### 2、范例详述

使用aggs对查询结果进行聚合，聚合的结果会存放到`aggregations`中，如下为例

```json
{
    "took": 5,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        ......  //这儿是命中的结果，这儿忽略掉
    },
    "aggregations": { //聚合的结果在这儿
        "brand_agg": { //聚合的时候取的名字
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 3,
            "buckets": [
                {
                    "key": 1,  //聚合得到goodsBrandId值为1
                    "doc_count": 1  //对应的数量为1
                },
                {
                    "key": 2, //聚合得到goodsBrandId值为2
                    "doc_count": 1  //对应的数量为1
                },
                {
                    "key": 3,  //聚合得到goodsBrandId值为3
                    "doc_count": 1  //对应的数量为1
                }
            ]
        }
    }
}
```

