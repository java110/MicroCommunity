

**1\. 查询审核人员**
###### 接口功能
> 查询审核人员接口

###### URL
> [http://api.java110.com:8008/api/auditUser.listAuditUsers](http://api.java110.com:8008/api/auditUser.listAuditUsers)

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
|page|1|-|-|当前页|-|
|row|1|-|-|每页显示的行数|-|
|storeId|1|String|30|商户ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/auditUser.listAuditUsers?page=1&row=10&storeId=402019032924930007](http://api.java110.com:8008/api/auditUser.listAuditUsers?page=1&row=10&storeId=402019032924930007)

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
	"auditUsers": [{
		"auditLink": "809002",
		"auditLinkName": "财务主管",
		"auditUserId": "862019102783930001",
		"objCode": "resourceEntry",
		"objCodeName": "采购流程",
		"userId": "302019101636810001",
		"userName": "11"
	},{
		"auditLink": "809004",
		"auditLinkName": "投诉处理人员",
		"auditUserId": "862019110314090001",
		"objCode": "complaint",
		"objCodeName": "投诉流程",
		"userId": "302019110143040008",
		"userName": "vvb"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 6
}

```
