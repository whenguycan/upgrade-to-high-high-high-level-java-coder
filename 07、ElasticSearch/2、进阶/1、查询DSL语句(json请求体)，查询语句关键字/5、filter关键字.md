## filter关键字



#### 1、使用范例

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "term":{
                        "goodsId":2
                    }
                }
            ],
            "filter":[
                {
                    条件1.....
                },
                {
                    条件2.....
                }
            ]
        }
    }

}
```





#### 2、范例详述

1. `filter`单独使用（没有配合`must`、`should`关键字）是对所有数据进行过滤，如果配合`must`、`should`则是对`must`、`should`匹配出来的数据进行过滤
2. `filter`中的所有条件是`and`的关系
3. 如果后续跟着是多个条件使用[ ]，如果后续是单个条件可以使用{ }
4. `filter`中所有的条件都是跟字段匹配相关的了，所以都是跟着`match`、`term`、`terms`、`nested`等。
5. `filter`只是对must、should的结果进行过滤，不能计算相关性得分。





#### 3、真实使用

```json
{
    "query":{
        "bool":{
            "must":[
                {
                    "term":{
                        "goodsId":2
                    }
                }
            ],
            "filter":[
                {
                    "term":{
                        "goodsCommentCount": 3
                    }
                },
                {
                    "term":{
                        "goodsBrandId":3
                    }
                }
            ]
        }
    }

}
```

