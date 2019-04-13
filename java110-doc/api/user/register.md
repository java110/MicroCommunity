

**1\. 用户注册**
###### 接口功能
> 用户通过web端或APP注册用户接口

###### URL
> [http://api.java110.com:8008/api/user.service.register](http://api.java110.com:8008/api/user.service.register)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
> |参数名称|约束|类型|长度|描述|取值说明
|:-----  |:-------|:-----|:-----  |:-----  |-----     |
|app_id|1|String|30|应用ID|Api服务分配                      |
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
> |参数名称|约束|类型|长度|描述|取值说明
|:-----  |:-------|:-----|:-----  |:-----  |-----     |
|name|1|String|50|用户名称|-|
|email|1|String|30|邮箱地址|邮箱地址1234@xx.com|
|password|1|String|128|密码|加盐码md5|
|tel|1|String|11|手机号|11位手机号|
|sex|?|String|1|性别|0表示男孩 1表示女孩|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

> |参数名称|约束|类型|长度|描述|取值说明
|:-----  |:-------|:-----|:-----  |:-----  |-----     |
|userId|1|String|30|用户名称|用户ID|
|responseTime|?|String|16|时间|请求返回的时间|



###### 举例
> 地址：[http://api.java110.com:8008/api/user.service.register](http://api.java110.com:8008/api/user.service.register)
``` javascript
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
