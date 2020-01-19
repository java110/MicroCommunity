

**1\. 配置菜单**
###### 接口功能
> 配置菜单接口

###### URL
> [http://api.java110.com:8008/api/menu.configMenu](http://api.java110.com:8008/api/menu.configMenu)

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
|floorId|1|String|30|楼ID|-|
|communityId|1|String|30|小区ID|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/menu.configMenu](http://api.java110.com:8008/api/menu.configMenu)

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
	"data": [{
		"gId": "802019110855900043",
		"name": "设备管理",
		"icon": "fa fa-globe",
		"index": 2,
		"description": "",
		"flowComponent": "viewMenuGroupInfo",
		"label": "NEW",
		"seq": "10"
	}, {
		"name": "zc测试",
		"description": "而且地区的",
		"flowComponent": "addMenuView",
		"url": "dadad",
		"seq": "12",
		"isShow": "Y"
	}, {
		"resource": "dadad",
		"domain": "800900000003",
		"name": "zc测试",
		"description": "而且地区的",
		"flowComponent": "addPrivilegeView"
	}]
}

返回报文：


```
