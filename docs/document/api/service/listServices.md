

**1\. 查询服务**
###### 接口功能
> 查询服务接口

###### URL
> [http://api.java110.com:8008/api/service.listServices](http://api.java110.com:8008/api/service.listServices)

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
|services|serviceId|1|String|30|服务ID|-|
|services|name|1|String|50|服务名称|-|
|services|serviceCode|1|String|50|自定义|命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo|
|services|businessTypeCd|1|String|12|-|-|
|services|seq|1|int|11|顺序|只有同步方式下根据seq从小到大调用接口|
|services|isInstance|1|String|2|是否Instance Y 需要，N不需要，NT透传类|-|
|services|method|1|String|50|方法|空 为http post LOCAL_SERVICE 为调用本地服务 其他为webservice方式调用|
|services|timeout|1|int|11|超时时间|-|
|services|retryCount|1|int|11|重试次数|-|
|services|provideAppId|1|String|30|应用ID|-|
		
###### 举例
> 地址：[http://api.java110.com:8008/api/service.listServices?page=1&row=10](http://api.java110.com:8008/api/service.listServices?page=1&row=10)

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
	"records": 27,
	"rows": 0,
	"services": [{
		"businessTypeCd": "API",
		"isInstance": "Y",
		"messageQueueName": "",
		"method": "GET",
		"name": "zc测试服务",
		"provideAppId": "8000418002",
		"retryCount": "3",
		"seq": "1",
		"serviceCode": "13131",
		"serviceId": "982020011906410004",
		"timeout": "60",
		"url": "http://order-service/orderApi/service"
	},{
		"businessTypeCd": "API",
		"isInstance": "Y",
		"messageQueueName": "",
		"method": "POST",
		"name": "上传业主照片",
		"provideAppId": "8000418002",
		"retryCount": "3",
		"seq": "1",
		"serviceCode": "owner.uploadOwnerPhoto",
		"serviceId": "982020010765690001",
		"timeout": "60",
		"url": "http://order-service/orderApi/service"
	}],
	"total": 263
}

```
