

**1\. 查询服务注册**
###### 接口功能
> 查询服务注册接口

###### URL
> [http://api.java110.com:8008/api/serviceRegister.listServiceRegisters](http://api.java110.com:8008/api/serviceRegister.listServiceRegisters)

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
|serviceRegisters|appId|1|String|30|应用ID|-|
|serviceRegisters|appName|1|String|50|应用名称|-|
|serviceRegisters|id|1|int|11|ID|自增主键|
|serviceRegisters|invokeModel|1|String|1|-|1-同步方式 2-异步方式|
|serviceRegisters|orderTypeCd|1|String|4|订单类型|参考c_order_type表|
|serviceRegisters|serviceCode|1|String|50|自定义|命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo'
|serviceRegisters|serviceId|1|String|30|服务ID|-|
|serviceRegisters|serviceName|1|String|50|服务名称|-|

		
###### 举例
> 地址：[http://api.java110.com:8008/api/serviceRegister.listServiceRegisters?page=1&row=10](http://api.java110.com:8008/api/serviceRegister.listServiceRegisters?page=1&row=10)

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
 	"records": 29,
 	"rows": 0,
 	"serviceRegisters": [{
 		"appId": "8000418002",
 		"appName": "控制中心应用",
 		"id": "6",
 		"invokeModel": "S",
 		"orderTypeCd": "Q",
 		"serviceCode": "query.console.templateCol",
 		"serviceId": "6",
 		"serviceName": "查询列模板信息"
 	}, {
 		"appId": "8000418002",
 		"appName": "控制中心应用",
 		"id": "15",
 		"invokeModel": "S",
 		"orderTypeCd": "Q",
 		"serviceCode": "delete.center.mapping",
 		"serviceId": "15",
 		"serviceName": "删除映射信息"
 	}],
 	"total": 287
 }

```
