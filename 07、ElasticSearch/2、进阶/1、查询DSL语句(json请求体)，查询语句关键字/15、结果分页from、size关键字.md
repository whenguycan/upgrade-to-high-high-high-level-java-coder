## from、size关键字



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
    "from":2,
    "size":2
    
    

}
```





#### 2、范例详述

from表示从哪一个结果开始，一般开始的位置是0。

size表示一页显示几条数据。