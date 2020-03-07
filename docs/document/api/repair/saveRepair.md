

**1\. 报修**
###### 接口功能
> 用户通过打电话，或APP小程序上登记报修功能

###### URL
> [http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair](http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair)

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
|repairType|1|String|12|报修类型|卧室报修 10001 管道报修 10002 客厅报修 10003|
|repairName|1|String|64|报修人名称|-|
|tel|1|String|11|手机号|-|
|roomId|1|String|30|房屋ID|-|
|appointmentTime|1|String|-|预约时间| 格式为 YYYY-MM-DD hh24:mi:ss|
|context|1|String|200|报修内容|-|
|communityId|1|String|30|小区ID|-|
|photos|1|Array|-|相关照片，base64格式|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair](http://api.java110.com:8008/api/ownerRepair.saveOwnerRepair)

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
    "repairType":"10001",
    "roomId":"752019100758260005",
    "repairName":"吴学文",
    "tel":"17797173942",
    "context":"服务太差",
    "communityId":"702019120393220007",
    "appointmentTime":"2019-12-14 18:30:30",
    "photos":['base64....'],
}

返回报文：
成功

```
