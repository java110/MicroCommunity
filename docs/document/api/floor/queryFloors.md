

**1\. 根据小区查询小区楼信息**
###### 接口功能
> 用户通过web端或APP查询小区楼信息接口

###### URL
> [http://api.java110.com:8008/api/floor.queryFloors](http://api.java110.com:8008/api/floor.queryFloors)

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
|row|1|int|-|每页显示的行数|不能超过50条|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|total|1|int|-|数据总记录数|-|
|-|apiFloorDataVoList|1|Array|-|小区列表节点|-|
|apiFloorDataVoList|floorId|1|String|30|楼ID|-|
|apiFloorDataVoList|floorName|1|String|200|楼名称|001号 002号|
|apiFloorDataVoList|floorNum|1|String|12|小区楼编码|如 001 002|
|apiFloorDataVoList|remark|?|String|200|备注|-|
|apiFloorDataVoList|userName|1|String|30|创建员工名称|-|



###### 举例
> 地址：[http://api.java110.com:8008/api/floor.queryFloors?page=1&row=10&communityId=7020181217000002](http://api.java110.com:8008/api/floor.queryFloors?page=1&row=10&communityId=7020181217000002)

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
	"apiFloorDataVoList": [{
		"floorId": "732019042181450002",
		"floorName": "1号楼",
		"floorNum": "01",
		"remark": "填写具体值",
		"userName": "毛彬彬"
	}, {
		"floorId": "732019042188750002",
		"floorName": "2号楼",
		"floorNum": "02",
		"userName": "钟原"
	}, {
		"floorId": "732019042218110002",
		"floorName": "3号楼01",
		"floorNum": "003",
		"userName": "毛彬彬"
	}],
	"page": 0,
	"rows": 0,
	"total": 3
}

```
