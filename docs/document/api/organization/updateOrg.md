

**1\. 编辑组织**
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
