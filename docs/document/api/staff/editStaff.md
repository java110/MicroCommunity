

**1\. 修改员工信息**
###### 接口功能
> 用户通过web端或APP添加添加员工信息接口

###### URL
> [http://api.java110.com:8008/api/user.staff.modify](http://api.java110.com:8008/api/user.staff.modify)

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
|email|1|String|30|邮箱地址|-|
|username|1|String|50|名称|-|
|name|1|String|50|名称|-|
|tel|1|String|11|电话|-|
|errorInfo|1|String|-|错误信息|-|
|userId|1|String|30|用户ID|-|
|address|1|String|300|现居住地址|-|
|sex|1|String|1|小区ID|0表示男孩 1表示女孩|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/user.staff.modify](http://api.java110.com:8008/api/user.staff.modify)

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
		"address": "3131",
		"sex": "0",
		"name": "31313",
		"errorInfo": "",
		"tel": "15178831314",
		"userId": "302019122019140002",
		"email": "121184950@qq.com",
		"username": "31313"
	}


返回报文：

```
