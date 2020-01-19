

**1\. 绑定服务**
###### 接口功能
> 绑定服务接口

###### URL
> [http://api.java110.com:8008/api/service.bindingService](http://api.java110.com:8008/api/service.bindingService)

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
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|orderTypeCd|1|String|4|订单类型|参考c_order_type表|
|invokeLimitTimes|1|int|11|接口调用一分钟调用次数|-|
|invokeModel|1|String|1|-|1-同步方式 2-异步方式|-|



###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/service.bindingService](http://api.java110.com:8008/api/service.bindingService)

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
	"data": [{
		"needShowAddAppButton": "true",
		"appId": "992020011970490003",
		"blackListIp": "",
		"name": "zc测试用",
		"index": 2,
		"securityCode": "1212",
		"remark": "",
		"flowComponent": "App",
		"whileListIp": ""
	}, {
		"isInstance": "Y",
		"method": "GET",
		"serviceCode": "1313",
		"retryCount": "3",
		"name": "zc测试服务",
		"index": 2,
		"flowComponent": "Service",
		"serviceId": "982020011906410004",
		"messageQueueName": "",
		"url": "http://order-service/orderApi/service",
		"timeout": "60"
	}, {
		"invokeLimitTimes": "1000",
		"orderTypeCd": "Q",
		"flowComponent": "addRouteView",
		"invokeModel": "S"
	}]
}

返回报文：

```
