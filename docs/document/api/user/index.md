## 1.0 用户注册

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


## 2.0 用户登录

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

## 3.0 用户退出登录

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

## 4.0 校验用户是否登录

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

