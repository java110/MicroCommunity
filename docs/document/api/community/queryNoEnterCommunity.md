

**1\. 查询商户未入驻小区信息**
###### 接口功能
> 用户通过web端或APP查询商户入驻小区信息接口

###### URL
> [http://api.java110.com:8008/api/query.noEnterCommunity.byMember](http://api.java110.com:8008/api/query.noEnterCommunity.byMember)

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
|memberId|1|String|30|小区成员ID|-|
|memberTypeCd|1|String|12|小区成员角色|-|

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
> 地址：[http://api.java110.com:8008/api/query.noEnterCommunity.byMember?memberId=345678&memberTypeCd=390001200001](http://api.java110.com:8008/api/query.noEnterCommunity.byMember?memberId=345678&memberTypeCd=390001200001)

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
	"orderTypeCd": "Q",
	"serviceCode": "",
	"response": {
		"code": "0000",
		"message": "成功"
	},
	"responseTime": "20190415115326",
	"communitys": [{
		"address": "青海省西宁市城中区129号",
		"nearbyLandmarks": "王府井旁30米",
		"cityCode": "100010",
		"name": "万博家博园（城西区）",
		"communityId": "7020181217000001",
		"mapY": "36.597263",
		"mapX": "101.801909"
	}],
	"bId": "-1",
	"businessType": "",
	"transactionId": "-1",
	"dataFlowId": "-1"
}

```
