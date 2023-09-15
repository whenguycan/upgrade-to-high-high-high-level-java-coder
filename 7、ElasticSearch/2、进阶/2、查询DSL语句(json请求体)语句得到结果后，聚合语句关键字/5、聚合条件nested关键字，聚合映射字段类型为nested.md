## nested关键字



#### 1、使用范例

```json
{
    "query":{
        "match_all":{}
    },

    "aggs":{
        "attr_agg":{ //给聚合起个名字
            "nested":{  //nested关键字，声明下面的操作是映射字段类型为nested
                "path":"goodsAttr" //被nested修饰的字段是哪个
            },
            "aggs":{ //一个子聚合，真正干活的聚合
                "attrId_agg":{ //聚合名称
                    "terms":{
                        "field":"goodsAttr.attrId",
                        "size":10
                    },
                  
                  	//还可以有子聚合
                  	"aggs":{
                        "attr_name_agg":{
                            "terms":{
                                "field": "goodsAttr.attrName",
                                "size":100
                            }
                        },
                        "attr_value_agg":{
                            "terms":{
                                "field":"goodsAttr.attrValue",
                                "size":100
                            }
                        }
                    }
                }
            }
        }
    }
}
```



#### 2、范例详述

聚合之后的结果，如下：

```json
{
    "took": 9,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 6,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            //忽略掉了结果......
        ]
    },
    "aggregations": {
        "attr_agg": {
            "doc_count": 18,
            "attrId_agg": {
                "doc_count_error_upper_bound": 0,
                "sum_other_doc_count": 0,
                "buckets": [
                    {
                        "key": 1,
                        "doc_count": 6,
                        "attr_name_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "机身内存",
                                    "doc_count": 6
                                }
                            ]
                        },
                        "attr_value_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "512G",
                                    "doc_count": 2
                                },
                                {
                                    "key": "128G",
                                    "doc_count": 1
                                },
                                {
                                    "key": "1T",
                                    "doc_count": 1
                                },
                                {
                                    "key": "256G",
                                    "doc_count": 1
                                },
                                {
                                    "key": "56G",
                                    "doc_count": 1
                                }
                            ]
                        }
                    },
                    {
                        "key": 2,
                        "doc_count": 6,
                        "attr_name_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "运行内存",
                                    "doc_count": 6
                                }
                            ]
                        },
                        "attr_value_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "6G",
                                    "doc_count": 2
                                },
                                {
                                    "key": "12G",
                                    "doc_count": 1
                                },
                                {
                                    "key": "2G",
                                    "doc_count": 1
                                },
                                {
                                    "key": "4G",
                                    "doc_count": 1
                                },
                                {
                                    "key": "8G",
                                    "doc_count": 1
                                }
                            ]
                        }
                    },
                    {
                        "key": 3,
                        "doc_count": 6,
                        "attr_name_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "CPU型号",
                                    "doc_count": 6
                                }
                            ]
                        },
                        "attr_value_agg": {
                            "doc_count_error_upper_bound": 0,
                            "sum_other_doc_count": 0,
                            "buckets": [
                                {
                                    "key": "A15",
                                    "doc_count": 2
                                },
                                {
                                    "key": "A16",
                                    "doc_count": 1
                                },
                                {
                                    "key": "APPLE A",
                                    "doc_count": 1
                                },
                                {
                                    "key": "垃圾",
                                    "doc_count": 1
                                },
                                {
                                    "key": "骁龙8",
                                    "doc_count": 1
                                }
                            ]
                        }
                    }
                ]
            }
        }
    }
}
```

