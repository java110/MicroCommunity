

**1\. 查询服务实现**
###### 接口功能
> 查询服务实现接口

###### URL
> [http://api.java110.com:8008/api/serviceImpl.listServiceImpls](http://api.java110.com:8008/api/serviceImpl.listServiceImpls)

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
|page|-|-|30|页数|-|
|row|-|-|30|行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|serviceImpls|businessTypeCd|1|String|12|-|
|serviceImpls|description|1|int|11|id|-|
|serviceImpls|invokeType|1|String|4|调用类型|-|
|serviceImpls|messageTopic|1|String|50|异步时的消息topic名称|-|
|serviceImpls|name|1|String|50|服务名称|-|
|serviceImpls|retryCount|1|int|11|重试次数|-|
|serviceImpls|serviceBusinessId|1|int|11|ID|-|
|serviceImpls|timeout|1|int|11|超时时间|默认60|
|serviceImpls|url|1|String|200|目标地址|-|


		
###### 举例
> 地址：[http://api.java110.com:8008/api/serviceImpl.listServiceImpls?page=1&row=10](http://api.java110.com:8008/api/serviceImpl.listServiceImpls?page=1&row=10)

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
 	"records": 13,
 	"rows": 0,
 	"serviceImpls": [{
 		"businessTypeCd": "100100030001",
 		"description": "保存用户信息",
 		"invokeType": "1",
 		"messageTopic": "userServiceTopic",
 		"name": "保存用户信息",
 		"retryCount": "3",
 		"serviceBusinessId": "1",
 		"timeout": "60",
 		"url": "ORDER_USER_SERVICE_URL"
 	},{
 		"businessTypeCd": "200100040001",
 		"description": "修改商户信息",
 		"invokeType": "1",
 		"messageTopic": "storeServiceTopic",
 		"name": "修改商户信息",
 		"retryCount": "3",
 		"serviceBusinessId": "10",
 		"timeout": "60",
 		"url": "ORDER_STORE_SERVICE_URL"
 	}],
 	"total": 126
 }

```
