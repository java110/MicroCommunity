

**1\. 停车费缴费清单**
###### 接口功能
> 用户通过web端或APP查询停车费缴费清单接口

###### URL
> [http://api.java110.com:8008/api/api.getParkingSpacePayFee](http://api.java110.com:8008/api/api.getParkingSpacePayFee)

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
|page|1|-|-|页数|-|
|row|1|-|-|每页显示的行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/api.getParkingSpacePayFee?page=0&row=30&communityId=7020181217000001&storeId=402019032924930007 ](http://api.java110.com:8008/api/api.getParkingSpacePayFee?page=0&row=30&communityId=7020181217000001&storeId=402019032924930007 )

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：

无

返回报文：
{
	"total": 38,
	"orderTypeCd": "Q",
	"serviceCode": "",
	"response": {
		"code": "0000",
		"message": "成功"
	},
	"responseTime": "20191226222549",
	"bId": "-1",
	"businessType": "",
	"transactionId": "-1",
	"payFees": [{
		"payObjName": "学文君",
		"createTime": "2019-12-25 14:34:14",
		"feeTypeCdName": "地上出售车位费",
		"detailId": "912019122510810018",
		"receivableAmount": 70000.00,
		"cycles": 1,
		"receivedAmount": 70000.00,
		"userName": "wuxw"
	}, {
		"payObjName": "12222",
		"createTime": "2019-12-25 14:34:14",
		"feeTypeCdName": "地上出售车位费",
		"detailId": "912019122510810018",
		"receivableAmount": 70000.00,
		"cycles": 1,
		"receivedAmount": 70000.00,
		"userName": "wuxw"
	},{
			"payObjName": "学文君",
			"createTime": "2019-09-15 10:45:40",
			"feeTypeCdName": "地下出租车位费",
			"detailId": "912019091546320003",
			"receivableAmount": 203.00,
			"cycles": 1,
			"receivedAmount": 99998.99,
			"userName": "wuxw"
		}],
		"dataFlowId": "-1"
	}

```
