

**1\. 查询业主信息**
###### 接口功能
> 查询业主信息接口

###### URL
> [http://api.java110.com:8008/api/owner.queryOwners](http://api.java110.com:8008/api/owner.queryOwners)

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
|-|communityId|1|String|30|小区ID|-|
|-|ownerTypeCd|1|String|30|业主ID|-|
|-|page|1|int|-|页数|-|
|-|row|1|int|-|没有行数|-|

###### 举例
> 地址：[http://api.java110.com:8008/api/owner.queryOwners?ownerTypeCd=1001&floorId=-1&unitId=-1&page=1&row=10&communityId=7020181217000001](http://api.java110.com:8008/api/owner.queryOwners?ownerTypeCd=1001&floorId=-1&unitId=-1&page=1&row=10&communityId=7020181217000001)

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
    		"age": "28",
    		"createTime": "2019-09-25 09:03:22",
    		"idCard": "632126199109162011",
    		"link": "18999999999",
    		"memberId": "772019092507000013",
    		"name": "吴学文",
    		"ownerId": "772019092507000013",
    		"ownerTypeCd": "1001",
    		"ownerTypeName": "业主",
    		"sex": "0",
    		"userName": "wuxw"
    	},{
    		"age": "23",
    		"createTime": "2019-10-29 08:56:50",
    		"idCard": "-1",
    		"link": "15987097564",
    		"memberId": "772019102956970002",
    		"name": "张三",
    		"ownerId": "772019102956970002",
    		"ownerTypeCd": "1001",
    		"ownerTypeName": "业主",
    		"remark": "",
    		"sex": "0",
    		"userName": "wuxw"
    	}],
    	"page": 0,
    	"records": 7,
    	"rows": 0,
    	"total": 63
    }

```
