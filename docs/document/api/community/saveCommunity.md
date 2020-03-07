

**1\. 添加小区**
###### 接口功能
> 添加小区接口

###### URL
> [http://api.java110.com:8008/api/community.saveCommunity](http://api.java110.com:8008/api/community.saveCommunity)

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
|name|1|String|100|小区名称|-|
|address|1|String|200|小区地址|-|
|nearbyLandmarks|1|String|200|地标|如王府井北60米|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/community.saveCommunity](http://api.java110.com:8008/api/community.saveCommunity)

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
	"address": "北京省北京市东城区zcasd",
	"nearbyLandmarks": "aodiai",
	"storeTypeCd": "800900000002",
	"cityCode": "110101",
	"name": "zccccc",
	"areaAddress": "北京省北京市东城区",
	"storeId": "402019062754960011",
	"mapY": "101.33",
	"mapX": "101.33"
}
返回报文：

成功

```
