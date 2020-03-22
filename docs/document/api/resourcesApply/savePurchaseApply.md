

**1\. 添加采购/出库申请**
###### 接口功能
> 用户通过web端或APP添加采购/出库申请接口

###### URL
> [http://api.java110.com:8008/api/purchaseApply.savePurchaseApply](http://api.java110.com:8008/api/purchaseApply.savePurchaseApply)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|file|1|String|100|文件名称|待定(还未添加文件功能)|
|-|resOrderType|1|String|8|订单类型|10000采购入库,20000出库 |
|-|description|1|String|200|申请说明|-|
|-|storeId|1|String|30|商户ID|前端不用传|
|-|userName|1|String|50|申请人姓名|前端不用传|
|-|userId|1|String|30|申请人ID|前端不用传|
|resourceStores|resId|1|String|30|物品ID|-|
|resourceStores|quantity|1|String|100|数量|出库/入口数量|
|resourceStores|remark|1|String|100|备注|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，




###### 举例
> 地址：[http://api.java110.com:8008/api/purchaseApply.savePurchaseApply](http://api.java110.com:8008/api/purchaseApply.savePurchaseApply)

``` json
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418004
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：
{
	"file": "",
	"resOrderType": "10000",
	"resourceStores": [{
		"resName": "12",
		"quantity": "12",
		"price": "3.00",
		"resCode": "2",
		"description": "4",
		"remark": "士大夫VS",
		"stock": "0",
		"resId": "852020021392580008"
	}, {
		"resName": "摄像头",
		"quantity": "14",
		"price": "32.00",
		"resCode": "00011",
		"description": "she",
		"remark": "顺风顺水",
		"stock": "0",
		"resId": "852020010916260001"
	}],
	"description": "非法访问",
	"storeId": "402019032924930007",
	"userName": "wuxw",
	"userId": "30518940136629616640"
}
返回报文：
状态码为 200
成功

```
