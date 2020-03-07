

**1\. 查询车位**
###### 接口功能
> 查询车位接口

###### URL
> [http://api.java110.com:8008/api/parkingSpace.queryParkingSpaces](http://api.java110.com:8008/api/parkingSpace.queryParkingSpaces)

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
|communityId|1|String|30|小区ID|-|



###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|owners|1|Array|-|业主成员对象|-|
|owners|age|1|int|-|年龄|-|
|owners|idCard|1|String|20|身份证号|-|
|owners|link|1|String|11|手机号|-|
|owners|memberId|1|String|30|成员ID|-|
|owners|name|1|String|100|成员名称|-|
|owners|ownerId|?|String|30|业主ID|-|
|owners|ownerTypeCd|?|String|12|业主成员1002|-|
|owners|remark|?|String|200|备注|-|
|owners|sex|?|String|1|性别|0男 1女|
|owners|createTime|1|String|30|创建时间|如：2018-04-09 12:00:34|



###### 举例
> 地址：[http://api.java110.com:8008/api/parkingSpace.queryParkingSpaces?page=1&row=10&communityId=7020181217000001]( http://api.java110.com:8008/api/parkingSpace.queryParkingSpaces?page=1&row=10&communityId=7020181217000001)

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
	"parkingSpaces": [{
		"area": "12.00",
		"areaNum": "001",
		"communityId": "7020181217000001",
		"num": "003",
		"paId": "102020012373390001",
		"psId": "792020012595620001",
		"remark": "",
		"state": "F",
		"stateName": "空闲 ",
		"typeCd": "1001"
	}, {
		"area": "12.00",
		"areaNum": "001",
		"communityId": "7020181217000001",
		"num": "002",
		"paId": "102020012373390001",
		"psId": "792020012569710002",
		"remark": "",
		"state": "S",
		"stateName": "出售",
		"typeCd": "1001"
	}, {
		"area": "23.12",
		"areaNum": "001",
		"communityId": "7020181217000001",
		"num": "001",
		"paId": "102020012373390001",
		"psId": "792020012575250001",
		"remark": "123",
		"state": "S",
		"stateName": "出售",
		"typeCd": "1001"
	}],
	"records": 1,
	"rows": 0,
	"total": 3
}

```
