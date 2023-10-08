## date类型



#### 1、使用范例

```json
{
  "mappings": {
    "properties": {
      "birthday": {
        "type": "date",
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
      }
    }
  }
}
```



#### 2、范例详述

指定`birthday`字段为date类型，其中`format`的把数据格式化成具体的格式，格式有：`yyyy-MM-dd HH:mm:ss`、`yyyy-MM-dd`、`epoch_millis`。epoch_millis是指从1970.1.1 零点到现在的毫秒数



排序的时候可以使用`birthday`，具体的排序操作是，将birthday对应的字段值统一转换成时间戳然后比较



![avatar](../../images/WechatIMG488.jpeg)