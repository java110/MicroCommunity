

**1\. 删除员工权限**
###### 接口功能
> 用户通过web端或APP删除员工权限接口

###### URL
> [http://api.java110.com:8008/api/delete.privilege.userPrivilege](http://api.java110.com:8008/api/delete.privilege.userPrivilege)

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
|pId|1|String|30|权限ID或权限组ID|-|
|pFlag|1|String|4|权限标志|1表示权限组 0表示权限|
|userId|1|String|30|用户ID|用户ID|
|storeId|1|String|30|用户所在的商户ID|用户所在的商户ID|
|storeTypeCd|?|String|12|用户所在的商户类别|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/delete.privilege.userPrivilege](http://api.java110.com:8008/api/delete.privilege.userPrivilege)

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
     "pId": "500201904001",
     "pFlag": "0",
     "userId": "302019040332500002",
     "storeId": "302019040332500002",
     "storeTypeCd": "302019040332500002"
}
返回报文：
成功

```
