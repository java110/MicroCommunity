

**1\. 查询菜单**
###### 接口功能
> 查询菜单接口

###### URL
> [http://api.java110.com:8008/api/menu.listMenus](http://api.java110.com:8008/api/menu.listMenus)

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
|page|-|-|-|分页|-|
|row|-|-|-|分页|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|父参数名称|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-: |
|-|-|-|-|-|-|-|




###### 举例
> 地址：[http://api.java110.com:8008/api/menu.listMenus?page=1&row=10](http://api.java110.com:8008/api/menu.listMenus?page=1&row=10)

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
	"menus": [{
		"description": "而且地DADADAD",
		"gId": "802019110855900043",
		"isShow": "Y",
		"isShowName": "显示",
		"mId": "702020011938730002",
		"name": "zc测试",
		"pId": "502020011928140001",
		"pName": "zc测试",
		"seq": "12",
		"url": "dadad"
	},{
		"description": "",
		"gId": "800201904007",
		"isShow": "Y",
		"isShowName": "显示",
		"mId": "702019110356630007",
		"name": "审核投诉单",
		"pId": "502019110360380006",
		"pName": "审核投诉单",
		"seq": "6",
		"url": "/flow/myAuditComplaintsFlow"
	}],
	"page": 0,
	"records": 8,
	"rows": 0,
	"total": 72
}

```
