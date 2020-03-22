

**1\. 查询订单信息**
###### 接口功能
> 查询订单信息接口

###### URL
> [http://api.java110.com:8008/api/corders.listCorders](http://api.java110.com:8008/api/corders.listCorders)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(url部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|page|1|int|-|分页信息|页数|
|row|1|int|最大50|分页信息|每页显示数|
|appId|1|String|30|应用ID|-|
|oId|1|String|30|订单id|-|
|extTransactionId|1|String|36|-|-|
|orderTypeCd|1|String|4|订单类型|参考c_order_type表|


###### 返回协议

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: || :-: |
|-|appId|1|String|30|应用ID|-|
|-|createTime|1|datetime|-|创建时间|-|
|-|extTransactionId|1|String|36|-|-|
|-|oId|1|String|30|订单id|-|
|-|orderTypeCd|1|String|4|订单类型|参考c_order_type表|
|-|requestTime|1|datetime|-|外部系统请求时间|-|
|-|userId|1|String|30|用户ID|-|
|cBusiness|bId|1|String|30|业务Id|-|
|cBusiness|businessTypeCd|1|String|30|订单类型|参考c_business_type|
|cBusiness|createTime|1|datetime|-|创建时间|-|
|cBusiness|oId|1|String|30|订单ID|-|


当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/corders.listCorders?page=40&row=20](http://api.java110.com:8008/api/corders.listCorders?page=40&row=20)

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：



返回报文：

{
	"corderDataVos": [{
		"appId": "8000418004",
		"cBusiness": [{
			"bId": "202019070480970004",
			"businessTypeCd": "500100030002",
			"businessTypeCdName": "小区成员加入信息",
			"createTime": "2019-07-04 15:53:21.0",
			"oId": "102019070437810001"
		}, {
			"bId": "202019070484530005",
			"businessTypeCd": "500100030002",
			"businessTypeCdName": "小区成员加入信息",
			"createTime": "2019-07-04 15:53:21.0",
			"oId": "102019070437810001"
		}, {
			"bId": "202019070498940003",
			"businessTypeCd": "500100030002",
			"businessTypeCdName": "小区成员加入信息",
			"createTime": "2019-07-04 15:53:21.0",
			"oId": "102019070437810001"
		}],
		"createTime": "2019-07-04 15:53:21.0",
		"extTransactionId": "c090609a-5e42-4510-96de-b548cde6a40c",
		"oId": "102019070437810001",
		"orderTypeCd": "D",
		"orderTypeCdName": "受理单",
		"requestTime": "20190704115320",
		"userId": "302019062765360003",
		"userName": "dails"
	}, {
		"appId": "8000418004",
		"cBusiness": [{
			"bId": "202019070503600003",
			"businessTypeCd": "500100040002",
			"businessTypeCdName": "小区成员退出信息",
			"createTime": "2019-07-04 16:30:21.0",
			"oId": "102019070518650001"
		}, {
			"bId": "202019070505690004",
			"businessTypeCd": "500100040002",
			"businessTypeCdName": "小区成员退出信息",
			"createTime": "2019-07-04 16:30:21.0",
			"oId": "102019070518650001"
		}],
		"createTime": "2019-07-04 16:30:21.0",
		"extTransactionId": "e7f78526-4779-487b-abc2-f05449188cc9",
		"oId": "102019070518650001",
		"orderTypeCd": "D",
		"orderTypeCdName": "受理单",
		"requestTime": "20190705123020",
		"userId": "302019062765360003",
		"userName": "dails"
	}],
	"page": 0,
	"records": 520,
	"rows": 0,
	"total": 10396
}, 
```
