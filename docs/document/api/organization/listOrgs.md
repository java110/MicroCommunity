

**1\. 查询组织**
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
