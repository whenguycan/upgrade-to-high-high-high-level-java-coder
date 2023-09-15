##  将数据写入到es中

写入到redis中的数据，只是测试用的数据。



![avatar](../images/WechatIMG486.jpeg)

按上图去拼凑数据，



拼凑的如下：

商品1：

```json
{
  "goodsId":1,
  "goodsTitle":"Apple iPhone 苹果14promax",
  "goodsSubtitle":"手机",
  "goodsPrice":1,
  "goodsImage":"",
  "goodsSaleCount":1,
  "goodsCommentCount":1,
  "goodsHasstock":true,
  "goodsBrandId":1
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"1T"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"12G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"骁龙8"
		}
  ]
}
```

商品2：

```json
{
  "goodsId":2,
  "goodsTitle":"Apple iPhone 14",
  "goodsSubtitle":"手机",
  "goodsPrice":2,
  "goodsImage":"",
  "goodsSaleCount":2,
  "goodsCommentCount":2,
  "goodsHasstock":true,
  "goodsBrandId":1,
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"512G"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"8G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"A16"
		}
  ]
}
```



商品3：

```json
{
  "goodsId":3,
  "goodsTitle":"苹果 Apple iPhone 14",
  "goodsSubtitle":"手机",
  "goodsPrice":3,
  "goodsImage":"",
  "goodsSaleCount":3,
  "goodsCommentCount":3,
  "goodsHasstock":true,
  "goodsBrandId":1,
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"512G"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"6G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"A15"
		}
  ]
}
```



商品4：

```json
{
  "goodsId":4,
  "goodsTitle":"Apple iPhone 苹果 14",
  "goodsSubtitle":"手机",
  "goodsPrice":4,
  "goodsImage":"",
  "goodsSaleCount":4,
  "goodsCommentCount":4,
  "goodsHasstock":true,
  "goodsBrandId":1,
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"256G"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"6G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"A15"
		}
  ]
}
```



商品5：

```json
{
  "goodsId":5,
  "goodsTitle":"Apple iPhone 14",
  "goodsSubtitle":"手机",
  "goodsPrice":5,
  "goodsImage":"",
  "goodsSaleCount":5,
  "goodsCommentCount":5,
  "goodsHasstock":true,
  "goodsBrandId":1,
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"128G"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"4G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"APPLE A"
		}
  ]
}
```



商品6：

```json
{
  "goodsId":6,
  "goodsTitle":"Apple iPhone 14 苹果14 苹果 14",
  "goodsSubtitle":"手机",
  "goodsPrice":6,
  "goodsImage":"",
  "goodsSaleCount":6,
  "goodsCommentCount":6,
  "goodsHasstock":true,
  "goodsBrandId":1,
  "goodsBrandName":"",
  "goodsBrandImg":"",
  "goodsCateId":1,
  "goodsCateName":""
  "goodsAttr":[
  	{
  		"attrId":"1",
  		"attrName":"机身内存",
  		"attrValue":"56G"
		},
    {
  		"attrId":"2",
  		"attrName":"运行内存",
  		"attrValue":"2G"
		},
		{
  		"attrId":"3",
  		"attrName":"CPU型号",
  		"attrValue":"垃圾"
		}
  ]
}
```



建立一个商品的vo

```java
@Data
public class GoodsVo {

    private Long goodsId;

    private String goodsTitle;

    private String goodsSubtitle;

    private BigDecimal goodsPrice;

    private String goodsImage;

    private Integer goodsSaleCount;

    private Boolean goodsHasstock;

    private Integer goodsCommentCount;

    private Long goodsBrandId;

    private String goodsBrandName;

    private String goodsBrandImg;

    private Long goodsCateId;

    private String goodsCateName;

    private List<GoodsAttr> goodsAttr;

    @Data
    public static class GoodsAttr{

        private Long attrId;

        private String attrName;

        private String attrValue;
    }
}
```



写入到es中的java代码如下：

```java
BulkRequest bulkRequest = new BulkRequest();

//第一个商品
IndexRequest indexRequest = new IndexRequest("products");
GoodsVo goodsVo = new GoodsVo();
goodsVo.setGoodsId(1L);
goodsVo.setGoodsTitle("Apple iPhone 苹果14promax");
goodsVo.setGoodsSubtitle("手机");
goodsVo.setGoodsPrice(new BigDecimal(1));
goodsVo.setGoodsImage("");
goodsVo.setGoodsSaleCount(1);
goodsVo.setGoodsHasstock(true);
goodsVo.setGoodsCommentCount(1);
goodsVo.setGoodsBrandId(1L);
goodsVo.setGoodsBrandName("");
goodsVo.setGoodsBrandImg("");
goodsVo.setGoodsCateId(1L);
goodsVo.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr1 = new GoodsVo.GoodsAttr();
goodsAttr1.setAttrId(1L);
goodsAttr1.setAttrName("机身内存");
goodsAttr1.setAttrValue("1T");

GoodsVo.GoodsAttr goodsAttr2 = new GoodsVo.GoodsAttr();
goodsAttr2.setAttrId(2L);
goodsAttr2.setAttrName("运行内存");
goodsAttr2.setAttrValue("12G");

GoodsVo.GoodsAttr goodsAttr3 = new GoodsVo.GoodsAttr();
goodsAttr3.setAttrId(3L);
goodsAttr3.setAttrName("CPU型号");
goodsAttr3.setAttrValue("骁龙8");

List<GoodsVo.GoodsAttr> li = new ArrayList<>();
li.add(goodsAttr1);
li.add(goodsAttr2);
li.add(goodsAttr3);
goodsVo.setGoodsAttr(li);
indexRequest.source(JSON.toJSONString(goodsVo), XContentType.JSON);


//第二个商品
IndexRequest indexRequest2 = new IndexRequest("products");
GoodsVo goodsVo2 = new GoodsVo();
goodsVo2.setGoodsId(2L);
goodsVo2.setGoodsTitle("Apple iPhone 14");
goodsVo2.setGoodsSubtitle("手机");
goodsVo2.setGoodsPrice(new BigDecimal(2));
goodsVo2.setGoodsImage("");
goodsVo2.setGoodsSaleCount(2);
goodsVo2.setGoodsHasstock(true);
goodsVo2.setGoodsCommentCount(2);
goodsVo2.setGoodsBrandId(2L);
goodsVo2.setGoodsBrandName("");
goodsVo2.setGoodsBrandImg("");
goodsVo2.setGoodsCateId(2L);
goodsVo2.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr12 = new GoodsVo.GoodsAttr();
goodsAttr12.setAttrId(1L);
goodsAttr12.setAttrName("机身内存");
goodsAttr12.setAttrValue("512G");

GoodsVo.GoodsAttr goodsAttr22 = new GoodsVo.GoodsAttr();
goodsAttr22.setAttrId(2L);
goodsAttr22.setAttrName("运行内存");
goodsAttr22.setAttrValue("8G");

GoodsVo.GoodsAttr goodsAttr32 = new GoodsVo.GoodsAttr();
goodsAttr32.setAttrId(3L);
goodsAttr32.setAttrName("CPU型号");
goodsAttr32.setAttrValue("A16");

List<GoodsVo.GoodsAttr> li2 = new ArrayList<>();
li2.add(goodsAttr12);
li2.add(goodsAttr22);
li2.add(goodsAttr32);
goodsVo2.setGoodsAttr(li2);
indexRequest2.source(JSON.toJSONString(goodsVo2), XContentType.JSON);

//第三个商品
IndexRequest indexRequest3 = new IndexRequest("products");
GoodsVo goodsVo3 = new GoodsVo();
goodsVo3.setGoodsId(3L);
goodsVo3.setGoodsTitle("苹果 Apple iPhone 14");
goodsVo3.setGoodsSubtitle("手机");
goodsVo3.setGoodsPrice(new BigDecimal(3));
goodsVo3.setGoodsImage("");
goodsVo3.setGoodsSaleCount(3);
goodsVo3.setGoodsHasstock(true);
goodsVo3.setGoodsCommentCount(3);
goodsVo3.setGoodsBrandId(3L);
goodsVo3.setGoodsBrandName("");
goodsVo3.setGoodsBrandImg("");
goodsVo3.setGoodsCateId(3L);
goodsVo3.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr13 = new GoodsVo.GoodsAttr();
goodsAttr13.setAttrId(1L);
goodsAttr13.setAttrName("机身内存");
goodsAttr13.setAttrValue("512G");

GoodsVo.GoodsAttr goodsAttr23 = new GoodsVo.GoodsAttr();
goodsAttr23.setAttrId(2L);
goodsAttr23.setAttrName("运行内存");
goodsAttr23.setAttrValue("6G");

GoodsVo.GoodsAttr goodsAttr33 = new GoodsVo.GoodsAttr();
goodsAttr33.setAttrId(3L);
goodsAttr33.setAttrName("CPU型号");
goodsAttr33.setAttrValue("A15");

List<GoodsVo.GoodsAttr> li3 = new ArrayList<>();
li3.add(goodsAttr13);
li3.add(goodsAttr23);
li3.add(goodsAttr33);
goodsVo3.setGoodsAttr(li3);
indexRequest3.source(JSON.toJSONString(goodsVo3), XContentType.JSON);

//第四件商品
IndexRequest indexRequest4 = new IndexRequest("products");
GoodsVo goodsVo4 = new GoodsVo();
goodsVo4.setGoodsId(4L);
goodsVo4.setGoodsTitle("Apple iPhone 苹果 14");
goodsVo4.setGoodsSubtitle("手机");
goodsVo4.setGoodsPrice(new BigDecimal(4));
goodsVo4.setGoodsImage("");
goodsVo4.setGoodsSaleCount(4);
goodsVo4.setGoodsHasstock(true);
goodsVo4.setGoodsCommentCount(4);
goodsVo4.setGoodsBrandId(4L);
goodsVo4.setGoodsBrandName("");
goodsVo4.setGoodsBrandImg("");
goodsVo4.setGoodsCateId(4L);
goodsVo4.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr14 = new GoodsVo.GoodsAttr();
goodsAttr14.setAttrId(1L);
goodsAttr14.setAttrName("机身内存");
goodsAttr14.setAttrValue("256G");

GoodsVo.GoodsAttr goodsAttr24 = new GoodsVo.GoodsAttr();
goodsAttr24.setAttrId(2L);
goodsAttr24.setAttrName("运行内存");
goodsAttr24.setAttrValue("6G");

GoodsVo.GoodsAttr goodsAttr34 = new GoodsVo.GoodsAttr();
goodsAttr34.setAttrId(3L);
goodsAttr34.setAttrName("CPU型号");
goodsAttr34.setAttrValue("A15");

List<GoodsVo.GoodsAttr> li4 = new ArrayList<>();
li4.add(goodsAttr14);
li4.add(goodsAttr24);
li4.add(goodsAttr34);
goodsVo4.setGoodsAttr(li4);
indexRequest4.source(JSON.toJSONString(goodsVo4), XContentType.JSON);

//第五个商品
IndexRequest indexRequest5 = new IndexRequest("products");
GoodsVo goodsVo5 = new GoodsVo();
goodsVo5.setGoodsId(5L);
goodsVo5.setGoodsTitle("Apple iPhone 14");
goodsVo5.setGoodsSubtitle("手机");
goodsVo5.setGoodsPrice(new BigDecimal(5));
goodsVo5.setGoodsImage("");
goodsVo5.setGoodsSaleCount(5);
goodsVo5.setGoodsHasstock(true);
goodsVo5.setGoodsCommentCount(5);
goodsVo5.setGoodsBrandId(5L);
goodsVo5.setGoodsBrandName("");
goodsVo5.setGoodsBrandImg("");
goodsVo5.setGoodsCateId(5L);
goodsVo5.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr15 = new GoodsVo.GoodsAttr();
goodsAttr15.setAttrId(1L);
goodsAttr15.setAttrName("机身内存");
goodsAttr15.setAttrValue("128G");

GoodsVo.GoodsAttr goodsAttr25 = new GoodsVo.GoodsAttr();
goodsAttr25.setAttrId(2L);
goodsAttr25.setAttrName("运行内存");
goodsAttr25.setAttrValue("4G");

GoodsVo.GoodsAttr goodsAttr35 = new GoodsVo.GoodsAttr();
goodsAttr35.setAttrId(3L);
goodsAttr35.setAttrName("CPU型号");
goodsAttr35.setAttrValue("APPLE A");

List<GoodsVo.GoodsAttr> li5 = new ArrayList<>();
li5.add(goodsAttr15);
li5.add(goodsAttr25);
li5.add(goodsAttr35);
goodsVo5.setGoodsAttr(li5);
indexRequest5.source(JSON.toJSONString(goodsVo5), XContentType.JSON);

//第六个商品
IndexRequest indexRequest6 = new IndexRequest("products");
GoodsVo goodsVo6 = new GoodsVo();
goodsVo6.setGoodsId(6L);
goodsVo6.setGoodsTitle("Apple iPhone 14 苹果14 苹果 14");
goodsVo6.setGoodsSubtitle("手机");
goodsVo6.setGoodsPrice(new BigDecimal(6));
goodsVo6.setGoodsImage("");
goodsVo6.setGoodsSaleCount(6);
goodsVo6.setGoodsHasstock(true);
goodsVo6.setGoodsCommentCount(6);
goodsVo6.setGoodsBrandId(6L);
goodsVo6.setGoodsBrandName("");
goodsVo6.setGoodsBrandImg("");
goodsVo6.setGoodsCateId(6L);
goodsVo6.setGoodsCateName("");

GoodsVo.GoodsAttr goodsAttr16 = new GoodsVo.GoodsAttr();
goodsAttr16.setAttrId(1L);
goodsAttr16.setAttrName("机身内存");
goodsAttr16.setAttrValue("56G");

GoodsVo.GoodsAttr goodsAttr26 = new GoodsVo.GoodsAttr();
goodsAttr26.setAttrId(2L);
goodsAttr26.setAttrName("运行内存");
goodsAttr26.setAttrValue("2G");

GoodsVo.GoodsAttr goodsAttr36 = new GoodsVo.GoodsAttr();
goodsAttr36.setAttrId(3L);
goodsAttr36.setAttrName("CPU型号");
goodsAttr36.setAttrValue("垃圾");

List<GoodsVo.GoodsAttr> li6 = new ArrayList<>();
li6.add(goodsAttr16);
li6.add(goodsAttr26);
li6.add(goodsAttr36);
goodsVo6.setGoodsAttr(li6);
indexRequest6.source(JSON.toJSONString(goodsVo6), XContentType.JSON);

bulkRequest.add(indexRequest).add(indexRequest2).add(indexRequest3).add(indexRequest4).add(indexRequest5).add(indexRequest6);
BulkResponse bulkResponse = esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
if (bulkResponse.hasFailures()) {
  System.out.println(bulkResponse.buildFailureMessage());
}
```

