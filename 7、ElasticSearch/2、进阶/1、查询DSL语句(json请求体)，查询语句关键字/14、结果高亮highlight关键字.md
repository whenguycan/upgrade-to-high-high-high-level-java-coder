## highlight关键字



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
    },
    
    "highlight":{
        "fields": {
        	"goodsTitle": {}
        },
        "pre_tags": "<strong>",
        "post_tags": "</strong>"
    }
    
    

}
```







#### 2、范例说明

使用`highlight`对结果进行处理

1. `highlight`中fields指定的字段，需要参与到条件查询中，搜索写了highlight了也白写

2. 对`highlight`处理之后的es会单独存放到一个`highlight`字段中，如下

   ```json
   "hits": [
               {
                   "_index": "products",
                   "_type": "_doc",
                   "_id": "jwmfF4YBQUGKOFiHOu2R",
                   "_score": 1.5099853,
                   "_source": {
                       "goodsAttr": [
                           {
                               "attrId": 1,
                               "attrName": "机身内存",
                               "attrValue": "56G"
                           },
                           {
                               "attrId": 2,
                               "attrName": "运行内存",
                               "attrValue": "2G"
                           },
                           {
                               "attrId": 3,
                               "attrName": "CPU型号",
                               "attrValue": "垃圾"
                           }
                       ],
                       "goodsBrandId": 6,
                       "goodsBrandImg": "",
                       "goodsBrandName": "",
                       "goodsCateId": 6,
                       "goodsCateName": "",
                       "goodsCommentCount": 6,
                       "goodsHasstock": true,
                       "goodsId": 6,
                       "goodsImage": "",
                       "goodsPrice": 6,
                       "goodsSaleCount": 6,
                       "goodsSubtitle": "手机",
                       "goodsTitle": "Apple iPhone 14 苹果14 苹果 14"
                   },
                   "highlight": {
                       "goodsTitle": [
                           "Apple iPhone 14 <strong>苹果</strong>14 <strong>苹果</strong> 14"
                       ]
                   }
               },
               {
                   "_index": "products",
                   "_type": "_doc",
                   "_id": "jQmfF4YBQUGKOFiHOu2R",
                   "_score": 1.449183,
                   "_source": {
                       "goodsAttr": [
                           {
                               "attrId": 1,
                               "attrName": "机身内存",
                               "attrValue": "256G"
                           },
                           {
                               "attrId": 2,
                               "attrName": "运行内存",
                               "attrValue": "6G"
                           },
                           {
                               "attrId": 3,
                               "attrName": "CPU型号",
                               "attrValue": "A15"
                           }
                       ],
                       "goodsBrandId": 4,
                       "goodsBrandImg": "",
                       "goodsBrandName": "",
                       "goodsCateId": 4,
                       "goodsCateName": "",
                       "goodsCommentCount": 4,
                       "goodsHasstock": true,
                       "goodsId": 4,
                       "goodsImage": "",
                       "goodsPrice": 4,
                       "goodsSaleCount": 4,
                       "goodsSubtitle": "手机",
                       "goodsTitle": "Apple iPhone 苹果 14"
                   },
                   "highlight": {
                       "goodsTitle": [
                           "Apple iPhone <strong>苹果</strong> 14"
                       ]
                   }
               }
           ]
   ```

   