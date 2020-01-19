

**1\. 查询映射**
###### 接口功能
> 查询映射接口

###### URL
> [http://api.java110.com:8008/api/mapping.listMappings](http://api.java110.com:8008/api/mapping.listMappings)

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
|page|1|int|-|页数|-|
|row|1|int|-|每页显示的行数|不能超过50条|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|total|1|int|-|数据总记录数|-|
|mappings|domain|1|String|50|域|-|
|mappings|id|1|int|11|主键ID|-|
|mappings|key|1|String|50|key|-|
|mappings|name|1|String|50|名称|-|
|mappings|remark|1|String|200|描述|-|
|mappings|value|1|String|1000|value|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/mapping.listMappings?page=1&row=10](http://api.java110.com:8008/api/mapping.listMappings?page=1&row=10)

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
	"mappings": [{
		"domain": "YUNLUN",
		"id": "72",
		"key": "hc_feature_url",
		"name": "获取特征url",
		"remark": "",
		"value": "http://49.234.194.98:30900/face/detect"
	},{
		"domain": "DOMAIN.COMMON",
		"id": "63",
		"key": "107",
		"name": "户型类别",
		"remark": "",
		"value": "七厅"
	}],
	"page": 0,
	"records": 7,
	"rows": 0,
	"total": 67
}

```
