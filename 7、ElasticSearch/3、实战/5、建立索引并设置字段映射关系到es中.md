## 建立索引并设置字段映射关系

发送PUT请求到`/索引名称`，建立字段映射关系如下：

```json
{
  "mappings": {
    "properties": {
      "goodsId": {
        "type": "long"
      },
      "goodsTitle":{
        "type":"text",
        "analyzer":"ik_smart"
      },
      "goodsSubtitle":{
        "type":"text",
        "analyzer":"ik_smart"
      },
      "goodsPrice":{
        "type":"keyword"
      },
      "goodsImage":{
        "type":"keyword",
        "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        "doc_values": false   #true为支持聚合操作，false为不支持聚合操作
      },
      "goodsSaleCount":{
        "type":"long"
      },
      "goodsHasstock":{
        "type":"boolean"
      },
      "goodsCommentCount":{
        "type":"long"
      },
      "goodsBrandId":{
        "type":"long"
      },
      "goodsBrandName":{
        "type":"keyword",
        "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        "doc_values": true   #true为支持聚合操作，false为不支持聚合操作
      },
      "goodsBrandImg":{
        "type":"keyword",
        "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        "doc_values": false   #true为支持聚合操作，false为不支持聚合操作
      },
      "goodsCateId":{
        "type":"long"
      },
      "goodsCateName":{
        "type":"keyword",
        "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        "doc_values": true   #true为支持聚合操作，false为不支持聚合操作
      },
      "goodsAttr":{
      	"type":"nested", #表示当前的goods_attr是一个嵌套属性，它有子属性。
        "properties":{
          "attrId":{
            "type":"long"
          },
          "attrName":{
            "type":"keyword",
            "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        		"doc_values": true   #true为支持聚合操作，false为不支持聚合操作
          },
          "attrValue":{
            "type":"keyword"
          }
        }
      }
    }
  }
}
```

