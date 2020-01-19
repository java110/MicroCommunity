

**1\. 访客记录查询**
###### 接口功能
> 访客记录查询接口

###### URL
> [http://api.java110.com:8008/api/visit.listVisits](http://api.java110.com:8008/api/visit.listVisits)

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
|row|1|-|-|每页行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/visit.listVisits?page=1&row=10](http://api.java110.com:8008/api/visit.listVisits?page=1&row=10)

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
	"page": 0,
	"records": 4,
	"rows": 0,
	"total": 34,
	"visits": [{
		"communityId": "7020181217000001",
		"departureTime": "2019-09-12 00:00:00",
		"ownerId": "772019051712520001",
		"phoneNumber": "13800138000",
		"statusCd": "0",
		"vId": "112019100856060003",
		"vName": "11",
		"visitGender": "1",
		"visitTime": "2019-10-07 17:37:31"
	},{
		"communityId": "7020181217000001",
		"departureTime": "2019-10-24 03:00:22",
		"ownerId": "772019051712520001",
		"phoneNumber": "18678909876",
		"statusCd": "0",
		"vId": "112019102334410003",
		"vName": "zhangsan",
		"visitCase": "test",
		"visitGender": "0",
		"visitTime": "2019-10-23 15:05:44"
	}]
}

```
