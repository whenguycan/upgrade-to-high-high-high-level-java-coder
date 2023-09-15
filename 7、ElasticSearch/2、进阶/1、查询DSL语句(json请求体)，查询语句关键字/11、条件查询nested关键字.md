## nested关键字



#### 1、使用范例

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "nested":{
                        "path": "goodsAttr",
                        "query":{
                            "bool":{
                                "must":[
                                    {
                                        "terms":{
                                            "goodsAttr.attrId": [
                                                1,2
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



#### 2、范例详述

使用nested查询

1. nested查询，只能在字段映射type为nested的类型时使用，别的type类型不能使用
2. nested关键字下，必须跟着`path`指定字段映射的type为nested的字段名称
3. 必须跟着一个子查询，在子查询中对nested类型中的字段进行查询。