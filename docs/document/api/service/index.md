## 2.7.1 说明

## 2.7.2 投诉建议

###### 接口功能
> 用户通过web端或APP 添加投诉建议接口

###### URL
> [http://api.java110.com:8008/api/complaint.saveComplaint](http://api.java110.com:8008/api/complaint.saveComplaint)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|typeCd|1|String|12|投诉类型|投诉809001 建议 809002|
|roomId|1|String|20|房间ID|-|
|complaintName|1|String|200|投诉人|-|
|tel|1|String|11|投诉人电话|-|
|context|1|String|500|投诉内容|-|
|userId|1|String|30|用户ID|-|
|storeId|1|String|30|投诉商户ID|这里目前先考虑投诉物业|
|photos|1|Array|-|相关照片，base64格式|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.saveComplaint](http://api.java110.com:8008/api/complaint.saveComplaint)

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
    "typeCd":"809001",
    "roomId":"752019100758260005",
    "complaintName":"吴学文",
    "tel":"17797173942",
    "context":"服务太差",
    "userId":"1292827282727",
    "storeId":"402019032924930007",
    "photos":['base64....'],
}

返回报文：
成功

```

## 2.7.3 查询投诉建议

###### 接口功能
> 用户通过web端或APP查询投诉建议接口

###### URL
> [,http://api.java110.com:8008/api/complaint.listComplaints](,http://api.java110.com:8008/api/complaint.listComplaints)

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
|page|1|-|30|页数|-|
|row|1|-|30|每页显示行数|-|
|communityId|1|-|30|小区ID|-|
|complaintId|1|-|30|投诉ID|-|
|complaintId|1|-|30|投诉ID|-|
|typeCd|1|-|30|投诉类型|-|
|complaintName|1|-|30|投诉人姓名|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容




###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.listComplaints?storeTypeCd=800900000003&storeId=402019032924930007&userName=wuxw&userId=30518940136629616640&complaintId=111&typeCd=809002&complaintName=111&page=1&row=10&communityId=7020181217000001]()

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
	"complaints": [{
		"complaintId": "882019110351910002",
		"complaintName": "测试工作流",
		"context": "测试工作流",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "17797173945",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110394400002",
		"complaintName": "测试工作流",
		"context": "测试工作流",
		"roomId": "752019100758260005",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909874444",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110312520003",
		"complaintName": "吴学文",
		"context": "测试工作流",
		"roomId": "752019102597030030",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "18909782345",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}],
	"page": 0,
	"records": 4,
	"rows": 0,
	"total": 37
}

```

## 2.7.4 删除投诉建议

###### 接口功能
> 用户通过web端或APP 删除建议接口

###### URL
> [http://api.java110.com:8008/api/complaint.deleteComplaint](http://api.java110.com:8008/api/complaint.deleteComplaint)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|complaintId|1|String|30|投诉ID|-|
|typeCd|1|String|30|投诉类型|-|
|stateName|1|String|30|处理状态名称|-|
|typeCdName|1|String|50|投诉类型名称|-|
|context|1|String|longtext|投诉内容|-|
|complaintName|1|String|30|投诉人|-|
|tel|1|String|11|电话|-|
|state|1|String|-|状态|-|
|storeId|1|String|-|商户ID|-|
|communityId|1|String|-|小区ID|-|
|roomId|1|String|-|房间ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.deleteComplaint](http://api.java110.com:8008/api/complaint.deleteComplaint)

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
	"complaintId": "882019110351910002",
	"typeCd": "809001",
	"stateName": "处理中",
	"typeCdName": "投诉",
	"context": "测试工作流",
	"complaintName": "测试工作流",
	"tel": "17797173945",
	"state": "10001",
	"storeId": "402019032924930007",
	"communityId": "7020181217000001",
	"roomId": "752019100965690010"
}

返回报文：
成功

```

## 2.7.5 查询投诉历史单

###### 接口功能
> 查询历史投诉单接口

###### URL
> [http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints](http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints)

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
|page|1|-|30|页数|-|
|row|1|-|30|每页显示行数|-|
|storeId|1|-|30|商户ID|-|
|userId|1|-|30|用户ID|-|
|communityId|1|-|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容




###### 举例
> 地址：[,http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints?page=1&row=10&communityId=7020181217000001&storeId=402019032924930007&userId=30518940136629616640](,http://api.java110.com:8008/api/auditUser.listAuditHistoryComplaints?page=1&row=10&communityId=7020181217000001&storeId=402019032924930007&userId=30518940136629616640)

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
	"complaints": [{
		"complaintId": "882019110564340012",
		"complaintName": "七七",
		"context": "七七",
		"roomId": "752019100965690010",
		"state": "10001",
		"stateName": "处理中",
		"storeId": "402019032924930007",
		"tel": "13323654562",
		"typeCd": "809001",
		"typeCdName": "投诉"
	}, {
		"complaintId": "882019110670850021",
		"complaintName": "审批人历史单查询",
		"context": "审批人历史单查询",
		"roomId": "752019100965690010",
		"state": "10002",
		"stateName": "处理完成",
		"storeId": "402019032924930007",
		"tel": "18909716666",
		"typeCd": "809002",
		"typeCdName": "建议"
	}],
	"page": 0,
	"records": 3,
	"rows": 0,
	"total": 28
}

```

## 2.7.6 报修

###### 接口功能
> 用户通过打电话，或APP小程序上登记报修功能

###### URL
> [http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair](http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|repairType|1|String|12|报修类型|卧室报修 10001 管道报修 10002 客厅报修 10003|
|repairName|1|String|64|报修人名称|-|
|tel|1|String|11|手机号|-|
|roomId|1|String|30|房屋ID|-|
|appointmentTime|1|String|-|预约时间| 格式为 YYYY-MM-DD hh24:mi:ss|
|context|1|String|200|报修内容|-|
|communityId|1|String|30|小区ID|-|
|photos|1|Array|-|相关照片，base64格式|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair](http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair)

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
    "repairType":"10001",
    "roomId":"752019100758260005",
    "repairName":"吴学文",
    "tel":"17797173942",
    "context":"服务太差",
    "communityId":"702019120393220007",
    "appointmentTime":"2019-12-14 18:30:30",
    "photos":['base64....'],
}

返回报文：
成功

```

## 2.7.7 删除报修

###### 接口功能
> 删除报修信息接口

###### URL
> [待补充]()

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|repairId|1|String|30|报修ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[带补充]()

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
	待补充
}

返回报文：
成功

```

## 2.7.8 查询报修单

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

## 2.7.9 查询发布信息

###### 接口功能
> 用户通过web端或APP查询活动接口

###### URL
> [待补充](待补充)

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
|page|-|-|-|当前页|-|
|row|-|-|-|每页显示的行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[url&page=1&row=10](待补充)

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
	待补充
}

```