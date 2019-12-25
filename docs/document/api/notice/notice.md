

**1\. 公告接口协议**
###### 接口功能
> 提供给app 或小程序查询公告信息

###### URL
> [http://api.java110.com:8008/api/api.queryNotices](http://api.java110.com:8008/api/api.queryNotices)

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
|page|1|int|-|分页信息，传1|-|
|row|1|int|-|分页信息传10|每页显示数|
|communityId|1|String|30|小区ID|我们是以小区ID做数据库分片键 必填|


###### 返回协议

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|noticeTypeCd|1|string|12|公告类型，1000 业主通知，1001员工通知，1002小区通知|-|
|context|1|string|2000|公告内容|公告内容|
|startTime|1|String|30|公告开始时间|公告开始时间|
|title|1|String|100|公告标题|-|
|communityId|1|String|30|小区ID|-|
|userId|1|String|30|用户ID|发布公告的用户ID|
|noticeId|1|String|30|公告ID|-|

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/api.queryNotices?page=1&row=10&communityId=702019120393220007](http://api.java110.com:8008/api/api.queryNotices?page=1&row=10&communityId=702019120393220007)

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
返回状态码200

{
	"notices": [{
		"noticeTypeCd": "1002",
		"context": "<p>今晚5点停电</p>",
		"startTime": 1576573877000,
		"title": "停电通知",
		"communityId": "702019120393220007",
		"userId": "302019120306580017",
		"noticeId": "962019121730430003"
	}],
	"orderTypeCd": "Q",
	"serviceCode": "",
	"response": {
		"code": "0000",
		"message": "成功"
	},
	"responseTime": "20191222231514",
	"bId": "-1",
	"businessType": "",
	"transactionId": "-1",
	"dataFlowId": "-1"
}

```
