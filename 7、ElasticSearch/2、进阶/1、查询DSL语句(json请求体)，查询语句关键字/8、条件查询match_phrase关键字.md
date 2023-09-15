## match_phrase关键字



#### 1、使用范例

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "match_phrase":{
                        "goodsTitle":"苹果手机"
                    }
                }
            ]
            
        }
    }

}
```





#### 2、范例详述

使用match_phrase查询

1. 如果查询的字段是text类型，会把查询条件先分词再用分好的词去查询（其实就是全文检索！）。分词结果必须在text字段分词中都包含，而且顺序必须相同，而且必须都是连续的。（搜索比较严格）