## text类型



#### 1、使用范例

```json
{
  "mappings": {
    "properties": {
      "goodsTitle":{
        "type":"text",
        "analyzer":"ik_smart"
      }
    }
  }
}
      
```



#### 2、范例详述

指定`goodsTitle`的类型为text类型，即全文索引的类型，该类型对应的数据，在存入到es的时候，会被执行分词并将分词统计写入到倒排索引中。后续的查询关键字`match`、`match_phrase`会用到这个关键字！

`analyzer`是指该字段使用ik_smart这个分词器进行分词，即数据存入到es的时候会先经过这个分词器分词然后写入到倒排索引中。后面查询数据的时候，查询条件也是按这个配置进行分词，然后去倒排索引中查询！

