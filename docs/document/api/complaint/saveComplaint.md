

**1\. 投诉建议**
###### 接口功能
> 用户通过web端或APP 添加投诉建议接口

###### URL
> [http://api.java110.com:8008/api/complaint.saveComplaint](http://api.java110.com:8008/api/complaint.saveComplaint)

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
|typeCd|1|String|12|投诉类型|投诉809001 建议 809002|
|roomId|1|String|20|房间ID|-|
|complaintName|1|String|200|投诉人|-|
|tel|1|String|11|投诉人电话|-|
|context|1|String|500|投诉内容|-|
|userId|1|String|30|用户ID|-|
|storeId|1|String|30|投诉商户ID|这里目前先考虑投诉物业|
|photos|1|Array|-|相关照片，base64格式|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/complaint.saveComplaint](http://api.java110.com:8008/api/complaint.saveComplaint)

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
    "typeCd":"809001",
    "roomId":"752019100758260005",
    "complaintName":"吴学文",
    "tel":"17797173942",
    "context":"服务太差",
    "userId":"1292827282727",
    "storeId":"402019032924930007",
    "photos":['base64....'],
}

返回报文：
成功

```
