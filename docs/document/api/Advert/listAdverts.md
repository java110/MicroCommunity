

**1\. 查询广告**
###### 接口功能
> 用户通过web端或APP查询广告接口

###### URL
> [,http://api.java110.com:8008/api/advert.listAdverts](,http://api.java110.com:8008/api/advert.listAdverts)

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
|page|-|-|-|当前页|-|
|row|-|-|-|每页显示的行数|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/advert.listAdverts?storeTypeCd=800900000003&page=1&row=10](http://api.java110.com:8008/api/advert.listAdverts?storeTypeCd=800900000003&page=1&row=10)

``` javascript
请求头信息：
Content-Type:application/json
USER_ID:1234
APP_ID:8000418002
TRANSACTION_ID:10029082726
REQ_TIME:20181113225612
SIGN:aabdncdhdbd878sbdudn898
请求报文：

无

返回报文：
{
	"adverts": [{
		"adName": "测试广告",
		"adTypeCd": "10000",
		"advertId": "962019120854090001",
		"classify": "9001",
		"classifyName": "物流",
		"endTime": "2020-02-13 06:30:03",
		"locationObjId": "702019120393220007",
		"locationTypeCd": "1000",
		"seq": "1",
		"startTime": "2019-12-08 18:54:02",
		"state": "1000",
		"stateName": "未审核"
	}, {
		"adName": "测试照片001",
		"adTypeCd": "10000",
		"advertId": "962019121642920006",
		"classify": "9002",
		"classifyName": "餐饮",
		"endTime": "2020-02-12 05:25:32",
		"locationObjId": "742019050396500001",
		"locationTypeCd": "2000",
		"seq": "1",
		"startTime": "2019-12-16 21:52:32",
		"state": "1000",
		"stateName": "未审核"
	}, {
		"adName": "测试图片002",
		"adTypeCd": "10000",
		"advertId": "962019121649020007",
		"classify": "9002",
		"classifyName": "餐饮",
		"endTime": "2019-12-17 09:45:23",
		"floorId": "732019100913200001",
		"floorNum": "3",
		"locationObjId": "742019103039170052",
		"locationObjName": "3栋2单元",
		"locationTypeCd": "2000",
		"seq": "2",
		"startTime": "2019-12-16 22:10:23",
		"state": "1000",
		"stateName": "未审核",
		"unitId": "742019103039170052",
		"unitNum": "2"
	}],
	"page": 0,
	"records": 1,
	"rows": 0,
	"total": 3
}

```
