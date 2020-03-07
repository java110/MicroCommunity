

**1\. 查询房屋**
###### 接口功能
> 用户APP小程序上查询报修

###### URL
> [http://api.java110.com:8008/api/ownerRepair.listOwnerRepairs](http://api.java110.com:8008/api/ownerRepair.listOwnerRepairs)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|roomId|1|String|30|房屋ID|-|
|communityId|1|String|30|小区ID|-|
|page|1|int|-|分页页数|-|
|row|1|int|-|每页展示数量|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/ownerRepair.listOwnerRepairs?communityId=7020181217000001&roomId=752019100758260005&page=1&row=10](http://api.java110.com:8008/api/ownerRepair.listOwnerRepairs?communityId=7020181217000001&roomId=752019100758260005&page=1&row=10)

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
	"ownerRepairs": [{
		"appointmentTime": "2019-12-14 18:30:30",
		"context": "服务太差",
		"repairId": "822019121623590070",
		"repairName": "吴学文",
		"repairType": "10001",
		"repairTypeName": "卧室报修",
		"roomId": "752019100758260005",
		"state": "1000",
		"stateName": "未派单",
		"tel": "17797173942"
	}, {
		"appointmentTime": "2019-12-14 18:30:30",
		"context": "服务太差",
		"repairId": "822019121683520071",
		"repairName": "吴学文",
		"repairType": "10001",
		"repairTypeName": "卧室报修",
		"roomId": "752019100758260005",
		"state": "1000",
		"stateName": "未派单",
		"tel": "17797173942"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 2
}
```
