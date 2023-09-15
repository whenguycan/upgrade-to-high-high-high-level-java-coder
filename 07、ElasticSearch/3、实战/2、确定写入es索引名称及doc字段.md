## 确定写入es的索引名称和doc字段



其中doc字段才是重点，写入到doc中的数据大概长这样

注意：<font color="red">不要用 "_" 因为java的vo中用的驼峰写入到es中就直接是驼峰了，不会转成下划线</font>

```json
{
  "goodsId":1,  #商品ID号
  "goodsTitle":"", #商品标题
  "goodsSubtitle":"", #商品副标题
  "goodsPrice":100, #商品价格
  "goodsSaleCount":100, #商品销量
  "goodsCommentCount":100, #商品评论数
  "goodsHasstock":true, #商品是否有库存
  "goodsBranchId":1, #商品品牌ID号
  "goodsBranchName":"", #商品品牌名称
  "goodsBranchImg":"", #商品品牌图片
  "goodsCateId":1, #商品分类ID号
  "goodsCateName":"" #商品分类名称
  "goodsAttr":[
  	{
  		"attrId":"", #商品规则ID号
  		"attrName":"", #商品规格的名称
  		"attrValue":""  #商品规格的值
		},
    {
  		"attrId":"", #商品规则ID号
  		"attrName":"", #商品规格的名称
  		"attrValue":""  #商品规格的值
		},
		{
  		"attrId":"", #商品规则ID号
  		"attrName":"", #商品规格的名称
  		"attrValue":""  #商品规格的值
		}
		.....
		.....
		.....
  ]
}
```

