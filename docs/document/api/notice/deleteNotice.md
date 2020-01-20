

**1\. 删除通知**
###### 接口功能
>删除通知接口

###### URL
> [http://api.java110.com:8008/api/notice.deleteNotice](http://api.java110.com:8008/api/notice.deleteNotice)

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
|noticeId|1|String|30|通知ID|-|



###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，


###### 举例
> 地址：[http://api.java110.com:8008/api/notice.deleteNotice](http://api.java110.com:8008/api/notice.deleteNotice)

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
	"createTime": "2020-01-14 10:24:05",
	"noticeTypeCd": "1002",
	"context": "<p>5151</p>",
	"startTime": "2020-01-14 10:23:16",
	"endTime": "2020-01-14 10:25:43",
	"title": "6106301",
	"communityId": "7020181217000001",
	"noticeId": "962020011403410004",
	"noticeTypeCdName": "小区通知"
}

返回报文：


```
