

**1\. 保存资源（物品）**
###### 接口功能
> 保存资源（物品）接口

###### URL
> [http://api.java110.com:8008/api/resourceStore.saveResourceStore](http://api.java110.com:8008/api/resourceStore.saveResourceStore)

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
|resName|1|String|100|资源名称|-|
|resCode|1|String|-|资源编码|如终端串码|
|price|1|decimal|9,2|资源单价|-|
|description|1|String|11|资源描述|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/resourceStore.saveResourceStore](http://api.java110.com:8008/api/resourceStore.saveResourceStore)

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
	"resName": "色佛说法",
	"price": "1231",
	"resCode": "1231341",
	"description": "二分五十发",
	"communityId": "7020181217000001",
	"storeId": "402019032924930007"
}

返回报文：
成功

```
