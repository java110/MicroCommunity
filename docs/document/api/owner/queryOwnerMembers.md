

**1\. 根据业主成员信息**
###### 接口功能
> 用户通过web端或APP查询业主成员信息

###### URL
> [http://api.java110.com:8008/api/owner.queryOwnerMembers](http://api.java110.com:8008/api/owner.queryOwnerMembers)

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
|ownerTypeCd|1|String|12|业主成员1002|-|

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
> 地址：[http://hc.demo.winqi.cn:8012/appApi/owner.queryOwnerMembers?ownerTypeCd=1002&ownerId=772019091360360003&communityId=7020181217000001]( http://hc.demo.winqi.cn:8012/appApi/owner.queryOwnerMembers?ownerTypeCd=1002&ownerId=772019091360360003&communityId=7020181217000001)

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
	"owners": [{
		"age": "1",
		"createTime": "2019-12-25 13:31:33",
		"idCard": "1",
		"link": "18888888888",
		"memberId": "772019122571010003",
		"name": "12222",
		"ownerId": "772019091360360003",
		"ownerTypeCd": "1002",
		"remark": "1",
		"sex": "0",
		"userName": "wuxw"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 1
}

```
