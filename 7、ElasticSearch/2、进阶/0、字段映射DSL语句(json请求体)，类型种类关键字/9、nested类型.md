## nested类型



#### 1、使用范例

```json
{
  "mappings": {
    "properties": {
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





#### 2、范例详述

指定`goodsAttr`是一个嵌套属性，它有子属性的，在`properties`中再定义子属性的属性类型。

注意：<font color="red">nested类型的数据，在查询和聚合的时候都需要单独处理！</font>后面会具体使用。



