

**1\. 开发服务提供**
###### 接口功能
> 开发服务提供接口

###### URL
> [http://api.java110.com:8008/api/serviceProvide.saveServiceProvide](http://api.java110.com:8008/api/serviceProvide.saveServiceProvide)

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
|id|1|int|11|id|-|
|name|1|String|50|名称|-|
|params|1|String|500|参数|-|
|queryModel|1|String|1|查询方式| 1、sql,2、存储过程|
|serviceCode|1|String|50|-|对应c_service表|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/serviceProvide.saveServiceProvide](http://api.java110.com:8008/api/serviceProvide.saveServiceProvide)

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
		"isInstance": "N",
		"method": "GET",
		"serviceCode": "community.listMyEnteredCommunitys",
		"retryCount": "3",
		"name": "查询入驻并且有权限的小区",
		"index": 2,
		"flowComponent": "Service",
		"serviceId": "982019122295940001",
		"messageQueueName": "",
		"url": "",
		"timeout": "60"
	}, {
		"template": "",
		"proc": "",
		"javaScript": "",
		"flowComponent": "devServiceProvideView",
		"queryModel": "1",
		"params": "dad",
		"sql": "select * from"
	}, {
		"description": "测试曾诚",
		"remark": "",
		"flowComponent": "serviceProvideRemarkView"
	}]
}

返回报文：

```
