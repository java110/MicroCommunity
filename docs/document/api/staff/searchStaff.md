

**1\. 搜索员工信息**
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
