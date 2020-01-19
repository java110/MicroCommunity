

**1\. 访客登记变更	**
###### 接口功能
> 访客登记变更接口

###### URL
> [待补充]()

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
|name|1|String|50|用户名称|-|
|visitGender|1|String|255|访客姓名|-|
|phoneNumber|1|String|11|联系方式"|-|
|visitTime|1|datetime|-|访客拜访时间|-|
|vId|1|String|30|访客记录ID|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-:|
|vName|1|String|255|访客姓名|-|
|visitGender|1|String|1|访客性别|-|
|phoneNumber|1|String|11|访客联系方式|-|
|visitTime|1|date|-|访客拜访时间|-|



###### 举例
> 地址：[待补充]()

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
	待补充
}
返回报文：
    成功

```
