## _source关键字



#### 1、使用范例

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "match":{
                        "goodsTitle":"苹果"
                    }
                        
                    
                }
            ]
            
        }
    },
    
    "highlight":{
        "fields": {
        "goodsTitle": {}
        },
        "pre_tags": "<strong>",
        "post_tags": "</strong>"
    },
    "_source":["goodsTitle", "goodsId"],
    "from":2,
    "size":2
    
    

}
```



#### 2、范例详述

使用_source关键字，可以过滤掉我们不需要的字段。

