

**1\. 报修派单**
###### 接口功能
> 报修派单接口

###### URL
> [http://api.java110.com:8008/api/ownerRepair.repairDispatchStep](http://api.java110.com:8008/api/ownerRepair.repairDispatchStep)

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
|userId|1|String|30|员工ID|-|
|repairId|1|String|30|保修ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/ownerRepair.repairDispatchStep](http://api.java110.com:8008/api/ownerRepair.repairDispatchStep)

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
	"sex": "0",
	"name": "发生的",
	"tel": "13037402201",
	"repairId": "822019121683520071",
	"communityId": "7020181217000001",
	"userId": "302019120764000020",
	"orgId": "842019120749040014",
	"email": "354545@qq.com"
}

返回报文：
成功

```
