## 使用postman手动编写json并发送请求测试



发送GET请求到 `/索引名称/_search`，请求体为：

```json
{
    "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "goodsTitle": "苹果14"
                    }
                }
            ],
            "filter": [
                {
                    "term": {
                        "goodsCateId": 1
                    }
                },
                {
                    "terms": {
                        "goodsBrandId": [
                            "1",
                            "2"
                        ]
                    }
                },
                {
                    "term": {
                        "goodsHasstock": true
                    }
                },
                {
                    "nested": {
                        "path": "goodsAttr",
                        "query": {
                            "bool": {
                                "must": [
                                    {
                                        "term": {
                                            "goodsAttr.attrId": {
                                                "value": 1
                                            }
                                        }
                                    },
                                    {
                                        "terms": {
                                            "goodsAttr.attrValue": [
                                                "x",
                                                "y"
                                            ]
                                        }
                                    }
                                ]
                            }
                        }
                    }
                },
                {
                    "range": {
                        "goodsPrice": {
                            "lte": 10000,
                            "gte": 100
                        }
                    }
                }
            ]
        }
    },
    "sort": [
        {
            "goodsPrice": {
                "order": "desc"
            }
        }
    ],
    "from": 0,
    "size": 10,
    "highlight": {
        "fields": {
            "goodsTitle": {}
        },
        "pre_tags": "<strong>",
        "post_tags": "</strong>"
    },
    "aggs": {
      	"brand_agg":{
          "terms":{
            "field":"goodsBrandId",
            "size":10
          },
          "aggs":{
            "brand_name_agg":{
              "terms":{
                "field":"goodsBrandName",
                "size":10
              }
            }
          }
        },
        "attr_agg": {
            "nested": {
                "path": "goodsAttr"
            },
            "aggs": {
                "attrId_agg": {
                    "terms": {
                        "field": "goodsAttr.attrId",
                        "size": 10
                    },
                    "aggs": {
                        "attr_name_agg": {
                            "terms": {
                                "field": "goodsAttr.attrName",
                                "size": 100
                            }
                        },
                        "attr_value_agg": {
                            "terms": {
                                "field": "goodsAttr.attrValue"
                            }
                        }
                    }
                }
            }
        }
    }
}
```

