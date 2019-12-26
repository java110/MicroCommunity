

**1\. 删除建议**
###### 接口功能
> 用户通过web端或APP 删除建议接口

###### URL
> [http://api.java110.com:8008/api/complaint.deleteComplaint](http://api.java110.com:8008/api/complaint.deleteComplaint)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|complaintId|1|String|30|投诉ID|-|
|typeCd|1|String|30|投诉类型|-|
|stateName|1|String|30|处理状态名称|-|
|typeCdName|1|String|50|投诉类型名称|-|
|context|1|String|longtext|投诉内容|-|
|complaintName|1|String|30|投诉人|-|
|tel|1|String|11|电话|-|
|state|1|String|-|状态|-|
|storeId|1|String|-|商户ID|-|
|communityId|1|String|-|小区ID|-|
|roomId|1|String|-|房间ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.deleteComplaint](http://api.java110.com:8008/api/complaint.deleteComplaint)

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：
{
	"complaintId": "882019110351910002",
	"typeCd": "809001",
	"stateName": "处理中",
	"typeCdName": "投诉",
	"context": "测试工作流",
	"complaintName": "测试工作流",
	"tel": "17797173945",
	"state": "10001",
	"storeId": "402019032924930007",
	"communityId": "7020181217000001",
	"roomId": "752019100965690010"
}

返回报文：
成功

```
