

**1\. 车位出售**
###### 接口功能
> 车位出售接口

###### URL
> [http://api.java110.com:8008/api/parkingSpace.sellParkingSpace](http://api.java110.com:8008/api/parkingSpace.sellParkingSpace)

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
|communityId|1|String|30|小区ID|-|
|ownerId|1|String|30|业主ID|-|
|carNum|1|String|12|车牌号|-|
|carBrand|1|String|50|汽车品牌|-|
|carType|1|String|4|汽车类型|9901 家用小汽车，9902 客车，9903 货车|
|carColor|1|String|12|颜色|-|
|psId|1|String|30|车位ID|-|
|storeId|1|String|30|商户ID|-|
|receivedAmount|1|String|30|小区ID|-|
|sellOrHire|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/parkingSpace.sellParkingSpace](http://api.java110.com:8008/api/parkingSpace.sellParkingSpace)

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
	"carBrand": "baoma",
	"flowComponentShow": true,
	"additionalAmount": "0.00",
	"carNum": "12121",
	"index": 3,
	"receivableAmount": "0.00",
	"psId": "792020012595620001",
	"carRemark": "da",
	"receivedAmount": "0.00",
	"ownerId": "772019092507000013",
	"storeId": "402019032924930007",
	"userId": "30518940136629616640",
	"carColor": "baise",
	"carType": "9901",
	"flowComponent": "sellParkingSpaceFee",
	"sellOrHire": "S",
	"communityId": "7020181217000001"
}

返回报文：


```
