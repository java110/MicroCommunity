

**1\. 查询小区**
###### 接口功能
> 查询小区接口

###### URL
> [http://api.java110.com:8008/api/community.listCommunitys](http://api.java110.com:8008/api/community.listCommunitys)

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
|page|1|int|-|分页|-|
|row|1|int|-|分页|-|

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
|communitys|memberId|1|String|30|小区成员ID|-|
|communitys|memberTypeCd|1|String|12|编码|-|
|communitys|statusCd|1|String|2|数据状态|详细参考c_status表，S 保存|
|communitys|cityName|1|String|50|城市名称|-|
|communitys|state|1|String|4|业务状态|1000 为待审核 1200审核拒绝|
|communitys|stateName|1|String|20|状态名称|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/community.listCommunitys?page=1&row=10](http://api.java110.com:8008/api/community.listCommunitys?page=1&row=10)

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
		"address": "西安市",
		"cityCode": "0971",
		"cityName": "未知",
		"communityId": "702019073109590004",
		"mapX": "101.33",
		"mapY": "101.33",
		"name": "江林新城",
		"nearbyLandmarks": "子午大道南三环",
		"state": "1100",
		"stateName": "审核完成"
	}, {
		"address": "青海省西宁市城中区五四大街",
		"cityCode": "0971",
		"cityName": "未知",
		"communityId": "702019070408660001",
		"mapX": "101.33",
		"mapY": "101.33",
		"name": "万方城（城中区）",
		"nearbyLandmarks": "青紫大厦",
		"state": "1100",
		"stateName": "审核完成"
	}],
	"page": 0,
	"records": 3,
	"rows": 0,
	"total": 27
}
```
