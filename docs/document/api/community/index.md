## 说明

资产类接口主要包括小区内的资产信息，如果楼栋 单元 房屋等信息

## 添加楼栋

未整理

## 修改楼栋

未整理

## 添加单元

未整理

## 修改单元

未整理

## 添加房屋

#### 接口说明

#### 请求地址及方式

>https://ip:port/api/room.saveRoom

>http post

#### 请求参数说明
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|unitPrice|1|String|4|房屋单价|-|
|section|1|String|4|房间数|-|
|remark|1|String|200|备注|-|
|userId|1|String|30|用户ID|-|
|communityId|1|String|30|小区ID|-|
|layer|1|String|30|房屋楼层|-|
|builtUpArea|1|String|30|建筑面积| 如 97.98|
|roomNum|1|String|12|房间编号|1123|
|unitId|1|String|30|小区单元ID|-|
|state|1|String|12|房屋状态|2002 空闲状态|
|apartment|1|String|4|户型|1010 一室一厅 1020 一室两厅 2010 两室一厅 2020 两室两厅 3020 三室两厅|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/room.saveRoom](http://api.java110.com:8008/api/room.saveRoom)

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
    "communityId":"小区ID",
    "unitPrice":"填写具体值",
    "section":"填写具体值",
    "remark":"填写具体值",
    "layer":"填写具体值",
    "builtUpArea":"填写具体值",
    "roomNum":"填写具体值",
    "unitId":"填写具体值",
    "apartment":"填写具体值"
}

返回报文：
成功

```


## 修改房屋

#### 接口说明

#### 请求地址及方式

>https://ip:port/api/room.updateRoom

>http get

#### 请求参数说明
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|roomId|1|String|30|房间ID|12312313|
|unitPrice|1|String|4|房屋单价|-|
|section|1|String|4|房间数|-|
|remark|1|String|200|备注|-|
|userId|1|String|30|用户ID|-|
|communityId|1|String|30|小区ID|-|
|layer|1|String|30|房屋楼层|-|
|builtUpArea|1|String|30|建筑面积| 如 97.98|
|roomNum|1|String|12|房间编号|1123|
|unitId|1|String|30|小区单元ID|-|
|apartment|1|String|4|户型|1010 一室一厅 1020 一室两厅 2010 两室一厅 2020 两室两厅 3020 三室两厅|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/room.updateRoom](http://api.java110.com:8008/api/room.updateRoom)

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
    "roomId":"12313",
    "communityId":"小区ID",
    "unitPrice":"填写具体值",
    "section":"填写具体值",
    "remark":"填写具体值",
    "layer":"填写具体值",
    "builtUpArea":"填写具体值",
    "roomNum":"填写具体值",
    "unitId":"填写具体值",
    "apartment":"填写具体值"
}

返回报文：
成功

```

## 房屋业主关系绑定


#### 接口说明

#### 请求地址及方式

>https://ip:port/api/room.updateRoom

>http get

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityId|1|String|30|小区ID|-|
|ownerId|1|String|30|业主ID|-|
|roomId|1|String|30|房屋ID|-|
|state|1|String|4|房屋状态|如房屋出售等，请查看state 表|
|storeId|1|String|30|商户ID|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功



###### 举例
>

``` javascript

请求报文：

返回报文：


```


## 房屋业主关系解绑


#### 接口说明

#### 请求地址及方式

>https://ip:port/api/room.exitRoom

>http get

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityId|1|String|30|组织ID|-|
|ownerId|1|String|12|小区ID|-|
|roomId|1|String|10|组织级别|-|
|storeId|1|String|200|级别名称|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功



###### 举例
> 地址：[http://api.java110.com:8008/api/room.exitRoom](http://api.java110.com:8008/api/room.exitRoom)

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
	"ownerId": "772019092517890016",
	"communityId": "7020181217000001",
	"storeId": "402019032924930007",
	"userId": "30518940136629616640",
	"roomId": "752019103088990077"
}

返回报文：

成功

```

## 查询房屋

#### 接口说明

#### 请求地址及方式

>https://ip:port/api/room.queryRooms

>http get

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityId|1|String|30|小区ID|-|
|page|1|-|-|分页|-|
|row|1|-|-|分页|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容
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

## 查询绑定业主房屋


## 查询未绑定业主房屋

## 查询业主房屋