## 说明

小区商户类接口 主要包含 小区添加 审核 商户入驻审核等功能接口

## 添加小区

###### 接口功能
> 添加小区接口

###### URL
> [http://api.java110.com:8008/api/community.saveCommunity](http://api.java110.com:8008/api/community.saveCommunity)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|name|1|String|100|小区名称|-|
|address|1|String|200|小区地址|-|
|nearbyLandmarks|1|String|200|地标|如王府井北60米|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/community.saveCommunity](http://api.java110.com:8008/api/community.saveCommunity)

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
	"address": "北京省北京市东城区zcasd",
	"nearbyLandmarks": "aodiai",
	"storeTypeCd": "800900000002",
	"cityCode": "110101",
	"name": "zccccc",
	"areaAddress": "北京省北京市东城区",
	"storeId": "402019062754960011",
	"mapY": "101.33",
	"mapX": "101.33"
}
返回报文：

成功

```

## 查询未审核小区

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

## 审核小区

###### 接口功能
> 小区审核接口

###### URL
> [http://api.java110.com:8008/api/community.auditEnterCommunity](http://api.java110.com:8008/api/community.auditEnterCommunity)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityMemberId|1|String|30|小区成员ID|-|
|remark|1|String|-|审核原因|-|
|state|1|String|4|审核状态|业务状态 1000 为待审核 1200审核拒绝|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/community.auditEnterCommunity](http://api.java110.com:8008/api/community.auditEnterCommunity)

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
	"remark": "怎会灰尘",
	"state": "1100",
	"communityMemberId": "722019120844120035"
}

返回报文：

成功

```

## 查询小区

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

## 商户入驻小区

###### 接口功能
> 用户通过web端或APP物业代理商等加入到小区中接口

###### URL
> [http://api.java110.com:8008/api/member.join.community](http://api.java110.com:8008/api/member.join.community)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|memberId|1|String|30|小区成员ID|-|
|communityId|1|String|30|小区ID|-|
|memberTypeCd|1|String|12|成员角色|-|
|auditStatusCd|1|String|4|审核状态|不需要审核时写0000|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/member.join.community](http://api.java110.com:8008/api/member.join.community)

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
	"memberId": "390001200001",
	"communityId": "1234444",
	"memberTypeCd": "12221222"
}

返回报文：

成功

```

## 商户入驻审核

###### 接口功能
> 小区审核接口

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityMemberId|1|String|30|小区成员ID|-|
|remark|1|String|-|审核原因|-|
|state|1|String|4|审核状态|业务状态 1000 为待审核 1200审核拒绝|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



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

{
	待补充
}

返回报文：

成功

```

## 查询小区商户

###### 接口功能
> 查询小区商户接口

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
|page|1|-|-|页数|-|
|row|1|-|-|每页行数|-|
|communityId|1|String|12|小区ID|-|

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
|communitys|memberTypeCd|1|String|30|小区成员类型|-|
|communitys|statusCd|1|String|4|状态|0 有效状态 1 失效状态 1000 入驻审核状态|




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
	待补充
}

```

## 查询组织

###### 接口功能
> 用户通过web端或APP查询组织信息接口

###### URL
> [http://api.java110.com:8008/api/org.listOrgs](http://api.java110.com:8008/api/org.listOrgs)

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
|storeId|1|String|30|商户ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|description|1|String|200|组织描述|-|
|-|orgId|1|String|30|组织ID|-|
|-|orgLevel|1|String|10|组织级别|-|
|-|orgLevelName|1|String|200|级别名称|-|
|-|orgName|1|String|200|组织名称|-|
|-|parentOrgId|1|String|30|上级组织ID|-|
|-|parentOrgName|1|String|30|上级组织名称|-|





###### 举例
> 地址：[http://api.java110.com:8008/api/org.listOrgs?storeTypeCd=800900000003&storeId=402019032924930007&userName=wuxw&userId=30518940136629616640&page=1&row=10&class=class](http://api.java110.com:8008/api/org.listOrgs?storeTypeCd=800900000003&storeId=402019032924930007&userName=wuxw&userId=30518940136629616640&page=1&row=10&class=class)

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
	"orgs": [{
		"description": "总公司",
		"orgId": "123",
		"orgLevel": "1",
		"orgLevelName": "公司级",
		"orgName": "java110工作室",
		"parentOrgId": "123",
		"parentOrgName": "java110工作室"
	}, {
		"description": "萨基打工撒谎发AFJasgfhASdfaSKl1",
		"orgId": "842019120765720013",
		"orgLevel": "2",
		"orgLevelName": "分公司级",
		"orgName": "奥利给工作室",
		"parentOrgId": "123",
		"parentOrgName": "java110工作室"
	}, {
		"description": "阿打发dAD",
		"orgId": "842019120749040014",
		"orgLevel": "3",
		"orgLevelName": "部门级",
		"orgName": "阿迪敬爱的",
		"parentOrgId": "842019120765720013",
		"parentOrgName": "奥利给工作室"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 3
}

```

## 添加组织

###### 接口功能
> 用户通过web端或APP保存组织接口

###### URL
> [http://api.java110.com:8008/api/org.saveOrg](http://api.java110.com:8008/api/org.saveOrg)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|storeId|1|String|30|商户ID|-|
|communityId|1|String|12|小区ID|-|
|orgLevel|1|String|10|组织级别|-|
|description|1|String|200|组织描述|-|
|orgName|1|String|200|组织名称|-|
|parentOrgId|1|String|30|上级组织ID|-|
|parentOrg|-|-|-|上级组织对象|包含上诉字段具体格式请看下面举例|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/org.saveOrg](http://api.java110.com:8008/api/org.deleteOrg)

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
	"orgName": "ddadad",
	"parentOrgId": "123",
	"description": "eqeqqeqe",
	"orgLevel": "2",
	"parentOrg": [{
		"orgName": "java110工作室",
		"parentOrgId": "123",
		"description": "总公司",
		"orgLevelName": "公司级",
		"orgLevel": "1",
		"parentOrgName": "java110工作室",
		"orgId": "123"
	}],
	"communityId": "7020181217000001",
	"storeId": "402019032924930007"
}

返回报文：

成功

```

## 编辑组织

###### 接口功能
> 用户通过web端或APP编辑组织接口

###### URL
> [http://api.java110.com:8008/api/org.updateOrg](http://api.java110.com:8008/api/org.updateOrg)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|orgId|1|String|30|组织ID|-|
|storeId|1|String|30|商户ID|-|
|communityId|1|String|12|小区ID|-|
|orgLevel|1|String|10|组织级别|-|
|description|1|String|200|组织描述|-|
|parentOrgId|1|String|30|上级组织ID|-|
|orgName|1|String|200|组织名称|-|
|parentOrg|1|-|-|上级组织对象|包含上诉字段格式请看下面举例|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/org.updateOrg ](http://api.java110.com:8008/api/org.updateOrg )

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
	"orgName": "奥利给工作室",
	"parentOrgId": "123",
	"description": "2121212",
	"orgLevel": "2",
	"parentOrg": [{
		"orgName": "java110工作室",
		"parentOrgId": "123",
		"description": "总公司",
		"orgLevelName": "公司级",
		"orgLevel": "1",
		"parentOrgName": "java110工作室",
		"orgId": "123"
	}],
	"communityId": "7020181217000001",
	"storeId": "402019032924930007",
	"orgId": "842019120765720013"
}

返回报文：

成功

```

## 删除组织

###### 接口功能
> 用户通过web端或APP编辑组织接口

###### URL
> [http://api.java110.com:8008/api/org.deleteOrg](http://api.java110.com:8008/api/org.deleteOrg)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|orgId|1|String|30|组织ID|-|
|communityId|1|String|12|小区ID|-|
|orgLevel|1|String|10|组织级别|-|
|orgLevelName|1|String|200|级别名称|-|
|description|1|String|200|组织描述|-|
|parentOrgId|1|String|30|上级组织ID|-|
|orgName|1|String|200|组织名称|-|
|parentOrgId|1|String|30|上级组织ID|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

###### 举例
> 地址：[http://api.java110.com:8008/api/org.deleteOrg](http://api.java110.com:8008/api/org.deleteOrg)

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
	"orgName": "阿迪敬爱的",
	"parentOrgId": "842019120765720013",
	"description": "阿打发dAD",
	"orgLevelName": "部门级",
	"orgLevel": "3",
	"communityId": "7020181217000001",
	"storeId": "402019032924930007",
	"parentOrgName": "奥利给工作室",
	"orgId": "842019120749040014"
}

返回报文：

成功

```

## 添加员工

###### 接口功能
> 用户通过web端或APP添加添加员工信息接口

###### URL
> [http://api.java110.com:8008/api/user.staff.add](http://api.java110.com:8008/api/user.staff.add)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|name|1|String|50|名称|-|
|email|1|String|30|邮箱地址|-|
|tel|1|String|11|电话|-|
|orgId|1|String|200|备注|-|
|address|1|String|300|现居住地址|-|
|sex|1|String|1|小区ID|0表示男孩 1表示女孩|
|relCd|1|String|30|关系角色|10000 普通员工， 20000部门经理 查看t_dict表|


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
	"address": "13131",
	"storeTypeCd": "800900000003",
	"sex": "0",
	"name": "31313",
	"tel": "1331313",
	"storeId": "402019032924930007",
	"orgId": "842019122030120003",
	"email": "121184950@qq.com",
	"username": "31313",
	"relCd": "10000"
}


返回报文：

```


## 删除员工

###### 接口功能
> 用户通过web端或APP添加添加员工信息接口

###### URL
> [http://api.java110.com:8008/api/user.staff.delete](http://api.java110.com:8008/api/user.staff.delete)

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

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|storeId|1|String|30|商户ID|-|
|userId|1|String|30|用户ID|-|



###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/user.staff.delete](http://api.java110.com:8008/api/user.staff.delete)

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
	"address": "13131",
    "age": 0,
	"sex": "0",
	"name": "31313",
    "orgName": "高速反辐射",
	"tel": "1331313",
	"email": "121184950@qq.com",
	"username": "302019122019140002",
    "userId": "31313"
}


返回报文：

## 查询员工

###### 接口功能
> 用户通过web端或APP搜索员工信息接口

###### URL
> [http://api.java110.com:8008/api/query.staff.infos?rows=10&storeId=402019032924930007&page=1&row=10](http://api.java110.com:8008/api/query.myCommunity.byMember)

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
|page|-|-|30|页数|-|
|row|-|-|30|行数|-|
|storeId|1|String|30|商户ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|email|1|String|30|邮箱地址|-|
|username|1|String|50|名称|-|
|name|1|String|50|名称|-|
|tel|1|String|11|电话|-|
|errorInfo|1|String|-|错误信息|-|
|userId|1|String|30|用户ID|-|
|address|1|String|300|现居住地址|-|
|sex|1|String|1|小区ID|0表示男孩 1表示女孩|
|age|1|int|-|年龄|-|
|orgName|1|String|30|组织名称|-|





###### 举例
> 地址：[http://api.java110.com:8008/api/query.staff.infos?rows=10&storeId=402019032924930007&page=1&row=10](http://api.java110.com:8008/api/query.staff.infos?rows=10&storeId=402019032924930007&page=1&row=10)

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
		"records": 1,
		"rows": 0,
		"staffs": [{
			"address": "3131",
			"age": 0,
			"email": "121184950@qq.com",
			"name": "31313",
			"orgName": "高速反辐射",
			"sex": "0",
			"tel": "15178831314",
			"userId": "302019122019140002",
			"userName": "31313"
		}, {
			"address": "31313",
			"age": 0,
			"email": "121184950@qq.com",
			"name": "3131",
			"orgName": "高速反辐射",
			"sex": "0",
			"tel": "31313",
			"userId": "302019122023920001",
			"userName": "3131"
		}, {
			"address": "123467",
			"age": 0,
			"email": "928255095@qq.com",
			"name": "gsfa",
			"orgName": "研发部",
			"sex": "1",
			"tel": "18909782345",
			"userId": "302019101771430001",
			"userName": "gsfa"
		}, {
			"address": "11",
			"age": 0,
			"email": "1@qq.cn",
			"name": "11",
			"orgName": "研发部",
			"sex": "0",
			"tel": "17111111111",
			"userId": "302019101636810001",
			"userName": "11"
		}],
		"total": 4
	}

```






