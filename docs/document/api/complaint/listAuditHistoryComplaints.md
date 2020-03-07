

**1\. 查询历史投诉单**
###### 接口功能
> 查询历史投诉单接口

###### URL
> [http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints](http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints)

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
|page|1|-|30|页数|-|
|row|1|-|30|每页显示行数|-|
|storeId|1|-|30|商户ID|-|
|userId|1|-|30|用户ID|-|
|communityId|1|-|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容




###### 举例
> 地址：[,http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints?page=1&row=10&communityId=7020181217000001&storeId=402019032924930007&userId=30518940136629616640](,http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints?page=1&row=10&communityId=7020181217000001&storeId=402019032924930007&userId=30518940136629616640)

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
	"complaints": [{
		"complaintId": "882019110564340012",
		"complaintName": "七七",
		"context": "七七",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "13323654562",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110670850021",
		"complaintName": "审批人历史单查询",
		"context": "审批人历史单查询",
		"roomId": "752019100965690010",
		"state": "10002",
		"stateName": "处理完成",
		"storeId": "402019032924930007",
		"tel": "18909716666",
		"typeCd": "809002",
		"typeCdName": "建议"
	}],
	"page": 0,
	"records": 3,
	"rows": 0,
	"total": 28
}

```
