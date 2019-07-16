

**1\. 根据小区查询单元信息**
###### 接口功能
> 用户通过web端或APP查询单元信息接口

###### URL
> [http://api.java110.com:8008/api/unit.queryUnits](http://api.java110.com:8008/api/unit.queryUnits)

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
|floorId|1|String|30|小区楼ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|unitId|1|String|30|单元ID|-|
|-|floorId|1|String|200|小区楼ID|-|
|-|unitNum|1|String|12|单元编码|如 001 002|
|-|layerCount|1|String|12|楼总层数|如 34|
|-|lift|1|String|4|是否有电梯|如 1010 有 2020 无|
|-|userName|1|String|50|创建者名称|如 张三 李四|
|-|remark|?|String|200|备注|-|
|-|createTime|1|String|30|创建时间|如：2018-04-09 12:00:34|



###### 举例
> 地址：[http://api.java110.com:8008/api/unit.queryUnits?floorId=12312313&communityId=7020181217000002](http://api.java110.com:8008/api/unit.queryUnits?floorId=12312313&communityId=7020181217000002)

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
[{
		"unitId": "732019042181450002",
		"floorId": "732019042181450002",
		"layerCount": "30",
		"lift": "1010",
		"unitNum": "01",
		"remark": "填写具体值",
		"userName": "毛彬彬",
		"createTime": "2018-04-09 12:00:34",
	}]

```
