## 2.5.1 用户注册

###### 接口功能
> 用户通过web端或APP注册用户接口

###### URL
> [http://api.java110.com:8008/api/user.service.register](http://api.java110.com:8008/api/user.service.register)

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
|name|1|String|50|用户名称|-|
|email|1|String|30|邮箱地址|邮箱地址1234@xx.com|
|password|1|String|128|密码|加盐码md5|
|tel|1|String|11|手机号|11位手机号|
|sex|?|String|1|性别|0表示男孩 1表示女孩|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|userId|1|String|30|用户名称|用户ID|
|responseTime|?|String|16|时间|请求返回的时间|



###### 举例
> 地址：[http://api.java110.com:8008/api/user.service.register](http://api.java110.com:8008/api/user.service.register)

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
     "name": "张三",
     "email": "928255095@qq.com",
     "password": "ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK",
     "sex": "0",
     "tel": "17797173943"
}
返回报文：
{"responseTime":"20181125230634","userId":"30516389265349820416"}

```


## 2.5.2 用户登录

###### 接口功能
> 用户通过web端或APP登录系统

###### URL
> [http://api.java110.com:8008/api/user.service.login](http://api.java110.com:8008/api/user.service.login)

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
|username|1|String|50|用户名|-|
|passwd|1|String|30|密码|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|userName|1|String|30|用户名称|用户名称|
|userId|1|String|30|用户ID|用户ID|
|token|?|String|64|鉴权码|鉴权码|



###### 举例
> 地址：[http://api.java110.com:8008/api/user.service.login](http://api.java110.com:8008/api/user.service.login)

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
	"username":"admin",
	"passwd":"d57167e07915c9428b1c3aae57003807"
}
返回报文：
{"userName":"admin","userId":"10001","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqYXZhMTEwIiwianRpIjoiNmUzZGNjMzM3YmE4NGRmMTgxNzcwNDVjOTIzOTljOTAifQ.oUotKsoy1TXBS37isSvwKTQ9c_tTxZS48TA554QdbEU"}

```

## 2.5.3 用户退出登录

###### 接口功能
> 用户通过web端或APP退出系统

###### URL
> [http://api.java110.com:8008/api/user.service.logout](http://api.java110.com:8008/api/user.service.logout)

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
|token|?|String|64|鉴权码|鉴权码|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，




###### 举例
> 地址：[http://api.java110.com:8008/api/user.service.logout](http://api.java110.com:8008/api/user.service.logout)

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
	"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqYXZhMTEwIiwianRpIjoiYWQ0ZjRjODUyZjI4NGY0Yjk4ODQzZDE3MmI4MmNmYTYifQ.p0KrdYX3-bifZ036WYCoyFQjoAo5_7bY1NXTwwOHPuk"
}
返回报文：
退出登录成功
```

## 2.5.4 校验用户是否登录

###### 接口功能
> 用户通过web端或APP判断是否有权限操作相应操作接口

###### URL
> [http://api.java110.com:8008/api/check.user.hasPrivilege](http://api.java110.com:8008/api/check.user.hasPrivilege)

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
|userId|1|String|30|用户ID|-|
|pId|1|String|30|权限ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，




###### 举例
> 地址：[http://api.java110.com:8008/api/check.user.hasPrivilege?userId=123&pId=123](http://api.java110.com:8008/api/check.user.hasPrivilege?userId=123&pId=123)

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
成功

```

## 2.5.5 添加业主（或家庭成员）

###### 接口功能
> 用户通过web端或APP保存业主信息信息接口

###### URL
> [http://api.java110.com:8008/api/owner.saveOwner](http://api.java110.com:8008/api/owner.saveOwner)

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
|name|1|String|12|业主名称|-|
|userId|1|String|30|用户ID|-|
|age|1|int|11|年龄|-|
|link|1|String|11|联系人手机号|-|
|sex|1|int|11|性别|-|
|ownerTypeCd|1|String|4|默认1001|1001 业主本人 1002 家庭成员|
|communityId|1|String|30|小区ID|-|
|ownerId|1|String|30|业主ID|在添加业主时该字段不传，家庭成员时传业主ID|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/owner.saveOwner](http://api.java110.com:8008/api/owner.saveOwner)

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
    "sex":"填写具体值",
            "name":"填写具体值",
            "link":"填写具体值",
            "remark":"填写具体值",
            "userId":"填写具体值",
            "ownerTypeCd":"1001",
            "age":"填写具体值"
}

返回报文：
成功

```

## 2.5.6 编辑业主

###### 接口功能
> 编辑业主信息接口

###### URL
> [http://api.java110.com:8008/api/owner.editOwner](http://api.java110.com:8008/api/owner.editOwner)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配|
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|memberId|1|String|30|业主成员ID|-|
|name|1|String|12|业主名称|-|
|userId|1|String|30|用户ID|-|
|age|1|int|11|年龄|-|
|link|1|String|11|联系人手机号|-|
|sex|1|int|11|性别|-|
|ownerTypeCd|1|String|4|默认1001|1001 业主本人 1002 家庭成员|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容



###### 举例
> 地址：[http://api.java110.com:8008/api/owner.editOwner](http://api.java110.com:8008/api/owner.editOwner)

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
	"ownerPhoto": "data:image/webp;base64,UklGRgQ1AABAA=",
	"idCard": "-1",
	"sex": "0",
	"ownerTypeCd": "1001",
	"link": "15987097564",
	"remark": "",
	"ownerId": "772019102956970002",
	"userId": "30518940136629616640",
	"componentTitle": "业主",
	"name": "张三",
	"videoPlaying": true,
	"communityId": "7020181217000001",
	"age": "23",
	"memberId": "772019102956970002"
}

返回报文：

成功

```

## 2.5.7 查询业主

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

## 2.5.8 查询家庭成员

###### 接口功能
> 用户通过web端或APP查询业主成员信息

###### URL
> [http://api.java110.com:8008/api/owner.listOwnerMachines](http://api.java110.com:8008/api/owner.listOwnerMachines)

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

## 2.5.9 上传业主照片

###### 接口功能
> 上传业主照片接口

###### URL
> [http://order-service/orderApi/service](http://order-service/orderApi/service)

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
|memberId|1|-|-|-|-|
|photo|1|longtext|-|文件内容|-|
|communityId|1|String|128|小区ID|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|-|-|-|-|-|-|




###### 举例
> 地址：[http://order-service/orderApi/service](http://order-service/orderApi/service)

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