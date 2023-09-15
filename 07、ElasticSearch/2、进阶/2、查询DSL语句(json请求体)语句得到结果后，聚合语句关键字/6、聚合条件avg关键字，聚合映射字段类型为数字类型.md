## avg关键字



#### 1、使用范例

```json
{
    "query":{
        "match_all":{}
    },

    "aggs":{
        "value_agg":{
            "avg":{  //求平均值
                "field":"goodsSaleCount"
            }
        }
    }
}
```





#### 2、范例详述

