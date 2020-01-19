

**1\. 编辑服务注册**
###### 接口功能
> 编辑服务注册接口

###### URL
> [http://api.java110.com:8008/api/serviceRegister.updateServiceRegister](http://api.java110.com:8008/api/serviceRegister.updateServiceRegister)

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

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|id|1|int|11|主键ID|-|
|-|appId|1|String|30|应用ID|-|
|-|invokeModel|1|String|1|-|1-同步方式 2-异步方式|
|-|orderTypeCd|1|String|4|订单类型|参考c_order_type表|
|-|serviceId|1|String|30|服务ID|-|
|-|invokeLimitTimes|1|int|11|接口调用一分钟调用次数|-|


		
###### 举例
> 地址：[http://api.java110.com:8008/api/serviceRegister.updateServiceRegister](http://api.java110.com:8008/api/serviceRegister.updateServiceRegister)

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
 	id: "6"
    appId: "8000418002"
    serviceId: "6"
    orderTypeCd: "Q"
    invokeLimitTimes: "10001"
    invokeModel: "S"
    communityId: "7020181217000001"
 }

无

返回报文：
 

```
