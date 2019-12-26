

**1\. 钥匙审核**
###### 接口功能
> 提供给app 或小程序申请钥匙

###### URL
> [http://api.java110.com:8008/api/applicationKey.saveApplicationKey](http://api.java110.com:8008/api/applicationKey.saveApplicationKey)

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
|name|1|String|30|姓名|-|
|communityId|1|String|30|小区|001号 002号|
|tel|1|String|11|手机号|如 001 002|
|typeCd|1|String|12|用户类型|10001 保洁 10002 保安 10003 其他人员|
|sex|1|String|2|性别|申请人男女  0男 1女|
|age|1|int|2|年龄|申请人年龄|
|idCard|1|String|20|身份证号|-|
|startTime|1|String|30|开始时间|格式：YYYY-MM-DD hh24:mi:ss|
|endTime|1|String|30|结束时间|格式：YYYY-MM-DD hh24:mi:ss|
|locationTypeCd|1|String|12|位置|位置类型，1000 东大门  1001 西大门 1002 北大门 1003 南大门 2000 单元门 3000 房屋门|
|locationObjId|1|String|30|位置对象|对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID|
|storeId|1|String|30|商户ID|商户ID|
|photo|1|String|-|base64照片信息|base64照片信息|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/applicationKey.saveApplicationKey](http://api.java110.com:8008/api/applicationKey.saveApplicationKey)

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
	"name":"学文",
	"communityId":"702019120393220007",
	"tel":"17797173942",
	"typeCd":"10001",
	"sex":"0",
	"age":"28",
	"idCard":"632126199109162011",
	"startTime":"2019-12-06 12:00:00",
	"endTime":"2022-12-06 12:00:00",
	"locationTypeCd":"1000",
	"locationObjId":"702019120393220007",
	"storeId":"71000023495857001",
    "photo":"base64...."
}


返回报文：
返回状态码200

{
"applicationKeyId":"123456"
}

```
