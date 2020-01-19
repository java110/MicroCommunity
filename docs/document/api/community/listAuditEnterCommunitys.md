

**1\. 查询未审核入驻小区**
###### 接口功能
> 查询未审核入驻小区接口

###### URL
> [http://api.java110.com:8008/api/community.listAuditEnterCommunitys](http://api.java110.com:8008/api/community.listAuditEnterCommunitys)

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
|page|1|int|-|页数|-|
|row|1|int|-|每页行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|communitys|1|Array|30|小区节点|-|
|communitys|communityId|1|String|30|小区ID|-|
|communitys|name|1|String|50|小区名称|-|
|communitys|address|1|String|200|小区地址|-|
|communitys|nearbyLandmarks|1|String|200|小区地标|-|
|communitys|cityCode|1|String|12|城市编码|-|
|communitys|mapX|1|String|12|精度|-|
|communitys|mapY|1|String|12|维度|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/community.listAuditEnterCommunitys?page=1&row=10](http://api.java110.com:8008/api/community.listAuditEnterCommunitys?page=1&row=10)

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
	"communitys": [{
		"address": "城南格兰小镇",
		"communityId": "702019070583460001",
		"communityMemberId": "722019120844120035",
		"memberId": "402019032924930007",
		"name": "安心亚燕（城中1）",
		"stateName": "待审核",
		"storeName": "小吴工作室",
		"storeTypeCd": "800900000003",
		"storeTypeName": "物业",
		"tel": "17797173942"
	}, {
		"address": "城南格兰小镇",
		"communityId": "702019121822890017",
		"communityMemberId": "722019122220460001",
		"memberId": "402019032924930007",
		"name": "青岛国信",
		"stateName": "待审核",
		"storeName": "小吴工作室",
		"storeTypeCd": "800900000003",
		"storeTypeName": "物业",
		"tel": "17797173942"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 6
}

```
