## range关键字



#### 1、使用范例

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    条件1
                    
                },
                {
                    "range":{
                        "goodsId":{
                            "gt": 3
                        }
                    }
                }
            ]
            
        }
    }
    

}
```





#### 2、范例详述

使用range来获取范围值，range指定的字段的映射关系，最好不用使用`text`