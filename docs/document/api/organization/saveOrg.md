

**1\. 保存组织**
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
