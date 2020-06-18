## 2.4.1说明

资产类接口主要包括小区内的资产信息，如果楼栋 单元 房屋等信息

## 2.4.2添加楼栋

###### 接口功能
> 用户通过web端或APP添加小区楼信息接口

###### URL
> [http://api.java110.com:8008/api/floor.saveFloor](http://api.java110.com:8008/api/floor.saveFloor)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

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
|floorId|1|String|30|楼ID|-|
|name|1|String|200|楼名称|001号 002号|
|floorNum|1|String|12|小区楼编码|如 001 002|
|remark|?|String|200|备注|-|
|userId|1|String|30|创建员工ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/floor.saveFloor](http://api.java110.com:8008/api/floor.saveFloor)

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

    "name": "3号楼01",
    "floorNum": "003",
    "userId": "123213213",
    "remark":"备注",
    "communityId":"小区ID"

}


返回报文：

```

## 2.4.3修改楼栋

###### 接口功能
> 用户通过web端或APP修改小区楼信息接口

###### URL
> [http://api.java110.com:8008/api/floor.editFloor](http://api.java110.com:8008/api/floor.editFloor)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

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
|floorId|1|String|30|楼ID|-|
|name|1|String|200|楼名称|001号 002号|
|floorNum|1|String|12|小区楼编码|如 001 002|
|remark|?|String|200|备注|-|
|userId|1|String|30|创建员工ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/floor.editFloor](http://api.java110.com:8008/api/floor.editFloor)

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
    "floorId":"1234567890",
    "name": "3号楼01",
    "floorNum": "003",
    "userId": "123213213",
    "remark":"备注",
    "communityId":"小区ID"

}

```

## 2.4.4添加单元

###### 接口功能
> 用户通过web端或APP保存单元信息接口

###### URL
> [http://api.java110.com:8008/api/unit.saveUnit](http://api.java110.com:8008/api/unit.saveUnit)

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
|floorId|1|String|30|小区楼ID|-|
|communityId|1|String|30|小区ID|-|
|unitNum|1|String|12|单元编号|-|
|layerCount|1|int|-|楼总层数|-|
|lift|1|String|4|是否有楼梯|1010 有 2020 无|
|layerCount|1|int|-|楼总层数|-|
|remark|?|string|200|备注|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/unit.saveUnit](http://api.java110.com:8008/api/unit.saveUnit)

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
    "floorId":"填写具体值",
     "layerCount":"填写具体值",
     "unitNum":"填写具体值",
     "lift":"填写具体值",
     "remark":"填写具体值"
}

返回报文：
成功

```

## 2.4.5修改单元

**1\. 修改小区单元信息**
###### 接口功能
> 用户通过web端或APP修改单元信息接口

###### URL
> [http://api.java110.com:8008/api/unit.updateUnit](http://api.java110.com:8008/api/unit.updateUnit)

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
|floorId|1|String|30|小区楼ID|-|
|communityId|1|String|30|小区ID|-|
|unitId|1|String|30|单元ID|-|
|unitNum|1|String|12|单元编号|-|
|layerCount|1|int|-|楼总层数|-|
|lift|1|String|4|是否有楼梯|1010 有 2020 无|
|layerCount|1|int|-|楼总层数|-|
|remark|?|string|200|备注|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/unit.updateUnit](http://api.java110.com:8008/api/unit.updateUnit)

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
    "floorId":"填写具体值",
     "layerCount":"填写具体值",
     "unitId":"填写具体值",
     "unitNum":"填写具体值",
     "lift":"填写具体值",
     "remark":"填写具体值"
}

返回报文：
成功

```

## 2.4.6添加房屋

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


## 2.4.7修改房屋

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

## 2.4.8房屋业主关系绑定


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


## 2.4.9房屋业主关系解绑


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

## 2.4.10查询房屋

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

## 2.4.11查询绑定业主房屋


## 2.4.12查询未绑定业主房屋

## 2.4.13查询业主房屋

###### 接口功能
> 查询业主房屋接口

###### URL
> [待补充]()

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
|ownerId|1|String|30|业主ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[待补充]()

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
	待更新
}

```

## 2.4.14查询楼栋

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

## 2.4.15查询单元

###### 接口功能
> 用户通过web端或APP查询单元信息接口

###### URL
> [http://api.java110.com:8008/api/unit.queryUnits](http://api.java110.com:8008/api/unit.queryUnits)

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
|floorId|1|String|30|小区楼ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|unitId|1|String|30|单元ID|-|
|-|floorId|1|String|200|小区楼ID|-|
|-|unitNum|1|String|12|单元编码|如 001 002|
|-|layerCount|1|String|12|楼总层数|如 34|
|-|lift|1|String|4|是否有电梯|如 1010 有 2020 无|
|-|userName|1|String|50|创建者名称|如 张三 李四|
|-|remark|?|String|200|备注|-|
|-|createTime|1|String|30|创建时间|如：2018-04-09 12:00:34|



###### 举例
> 地址：[http://api.java110.com:8008/api/unit.queryUnits?floorId=12312313&communityId=7020181217000002](http://api.java110.com:8008/api/unit.queryUnits?floorId=12312313&communityId=7020181217000002)

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
[{
		"unitId": "732019042181450002",
		"floorId": "732019042181450002",
		"layerCount": "30",
		"lift": "1010",
		"unitNum": "01",
		"remark": "填写具体值",
		"userName": "毛彬彬",
		"createTime": "2018-04-09 12:00:34",
	}]

```