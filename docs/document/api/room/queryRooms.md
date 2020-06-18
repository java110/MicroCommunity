

**1\. 查询房屋信息**
###### 接口功能
> 查询已销售的房屋信息接口

###### URL
> [http://api.java110.com:8008/api/room.queryRooms](http://api.java110.com:8008/api/room.queryRooms)

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
|communityId|1|String|30|小区ID|-|
|page|1|-|-|分页|-|
|row|1|-|-|分页|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |


###### 举例
> 地址：[http://api.java110.com:8008/api/room.queryRooms?page=1&row=10&communityId=7020181217000001](http://api.java110.com:8008/api/room.queryRooms?page=1&row=10&communityId=7020181217000001)

``` javascript
请求报文：

无

返回报文：
{
	"page": 0,
	"records": 3,
	"rooms": [{
		"apartment": "10102",
		"apartmentName": "一室两厅",
		"builtUpArea": "94.56",
		"floorId": "732019092566800002",
		"floorNum": "501",
		"layer": "1",
		"roomId": "752019092595400003",
		"roomNum": "1013",
		"section": "1",
		"state": "2001",
		"unitId": "742019092594570007",
		"unitNum": "1",
		"unitPrice": "1000.00",
		"userName": "wuxw"
	},{
		"apartment": "1010",
		"builtUpArea": "94.56",
		"floorId": "732019100967710004",
		"floorNum": "1",
		"layer": "1",
		"roomId": "752019103084230076",
		"roomNum": "1013",
		"section": "1",
		"state": "2001",
		"unitId": "742019103065850047",
		"unitNum": "1",
		"unitPrice": "1000.00",
		"userName": "wuxw"
	}, {
		"apartment": "1010",
		"builtUpArea": "94.56",
		"floorId": "732019100967710004",
		"floorNum": "1",
		"layer": "1",
		"roomId": "752019103088990077",
		"roomNum": "1014",
		"section": "1",
		"state": "2001",
		"unitId": "742019103065850047",
		"unitNum": "1",
		"unitPrice": "1000.00",
		"userName": "wuxw"
	}],
	"rows": 0,
	"total": 28
}
```

