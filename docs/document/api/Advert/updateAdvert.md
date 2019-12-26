

**1\. 修改广告**
###### 接口功能
> 用户通过web端或APP删除广告接口

###### URL
> [http://api.java110.com:8008/api/advert.updateAdvert](http://api.java110.com:8008/api/advert.updateAdvert)

###### 支持格式
> JSON

###### HTTP请求方式
> POST

###### 请求参数(header部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|app_id|1|String|30|应用ID|Api服务分配|
|transaction_id|1|String|30|请求流水号|不能重复 1000000000+YYYYMMDDhhmmss+6位序列 |
|sign|1|String|-|签名|请参考签名说明|
|req_time|1|String|-|请求时间|YYYYMMDDhhmmss|

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|advertId|1|String|30|广告ID|-|
|adName|1|String|200|广告名称|-|
|classify|1|String|12|广告分类|9001 物流 9002 餐饮,请查看advert表|
|locationTypeCd|1|String|12|位置类型|-1000 大门 2000 单元门，请查看advert表|
|adTypeCd|1|String|12|广告类型|门禁机 10000，请查看advert表|
|seq|1|int|1|播放顺序|-|
|startTime|1|date|-|投发时间|-|
|endTime|1|date|-|结束时间|-|
|locationObjId|1|String|30|对象ID|大门时小区ID，单元门 时单元ID 房屋时房屋ID，楼栋时 楼栋ID'|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/advert.updateAdvert](http://api.java110.com:8008/api/advert.updateAdvert)

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
	"classify": "9002",
	"adName": "测试照片001",
	"locationTypeCd": "2000",
	"unitNum": "",
	"adTypeCd": "10000",
	"photos": [],
	"advertId": "962019121642920006",
	"floorNum": "",
	"roomId": "",
	"floorId": "732019042188750002",
	"roomNum": "",
	"viewType": "9999",
	"vedioName": "",
	"unitId": "742019050304250004",
	"startTime": "2019-12-16 21:52:32",
	"state": "1000",
	"endTime": "2020-02-12 05:25:32",
	"floorName": "",
	"communityId": "7020181217000001",
	"locationObjId": "742019050304250004",
	"seq": "1"
}

返回报文：

成功

```
