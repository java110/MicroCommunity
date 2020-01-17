

**1\. 查询资源(物品)**
###### 接口功能
> 查询资源(物品)接口

###### URL
> [http://api.java110.com:8008/api/resourceStore.listResourceStores](http://api.java110.com:8008/api/resourceStore.listResourceStores)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|page|1|int|-|分页页数|-|
|row|1|int|-|每页展示数量|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/resourceStore.listResourceStores?page=1&row=10](http://api.java110.com:8008/api/resourceStore.listResourceStores?page=1&row=10)

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
	"page": 0,
	"records": 1,
	"resourceStores": [{
		"description": "2",
		"price": "11.00",
		"resCode": "111111",
		"resId": "852019112116690048",
		"resName": "阿斯顿发斯蒂芬",
		"stock": "0"
	}, {
		"description": "很好",
		"price": "88888.00",
		"resCode": "dsg",
		"resId": "852019112848360001",
		"resName": "电脑",
		"stock": "0"
	}],
	"rows": 0,
	"total": 5
}
```
