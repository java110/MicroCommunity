

**1\. 添加员工信息**
###### 接口功能
> 用户通过web端或APP添加添加员工信息接口

###### URL
> [http://api.java110.com:8008/api/user.staff.add](http://api.java110.com:8008/api/user.staff.add)

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
|name|1|String|50|名称|-|
|email|1|String|30|邮箱地址|-|
|tel|1|String|11|电话|-|
|orgId|?|String|200|备注|-|
|address|1|String|300|现居住地址|-|
|sex|1|String|1|小区ID|0表示男孩 1表示女孩|
|relCd|1|String|30|关系角色|10000 普通员工， 20000部门经理 查看t_dict表|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/floor.saveFloor](http://api.java110.com:8008/api/floor.saveFloor)

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
	"address": "13131",
	"storeTypeCd": "800900000003",
	"sex": "0",
	"name": "31313",
	"tel": "1331313",
	"storeId": "402019032924930007",
	"orgId": "842019122030120003",
	"email": "121184950@qq.com",
	"username": "31313",
	"relCd": "10000"
}


返回报文：

```
