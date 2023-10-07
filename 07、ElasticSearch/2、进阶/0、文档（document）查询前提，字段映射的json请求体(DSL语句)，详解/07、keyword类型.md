## keyword类型



#### 1、使用范例

```json
{
  "mappings": {
    "properties": {
      "goodsPrice":{
        "type":"keyword",
        "index": false,  #true为支持搜索即可以作为搜索条件 false为不支持搜索即不可以作为搜索条件
        "doc_values": false   #true为支持聚合操作，false为不支持聚合操作
      }
    }
  }
}
```





#### 2、范例详述

keyword类型，默认最大长度为256。

指定`goodsPrice`的类型为一个keyword类型，即关键字类型，该类型对应的数据，在存入到es的时候，被当做一个整体存放到倒排索引不会进行分词。后续查询的时候只能使用term、terms进行精确查询。



`index`：值为true为支持搜索即可以作为搜索条件，值为false为不支持搜索即不可以作为搜索条件

`doc_values`: 值true为支持聚合操作，值false为不支持聚合操作