

**1\. 添加审核人员**
###### 接口功能
> 添加审核人员接口

###### URL
> [http://api.java110.com:8008/api/auditUser.saveAuditUser](http://api.java110.com:8008/api/auditUser.saveAuditUser)

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
|userId|1|String|30|用户ID|-|
|storeId|1|String|30|商户ID|-|
|userName|1|String|100|用户名称|-|
|auditLink|1|String|64|审核环节|见t_dict表 如部门经理审核 ， 财务审核 ，采购人员采购|
|objCode|1|String|64|流程对象|流程对象编码,如采购申请 resource_enter|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/auditUser.saveAuditUser](http://api.java110.com:8008/api/auditUser.saveAuditUser)

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
	"objCode": "resourceEntry",
	"userName": "发生的",
	"storeId": "402019032924930007",
	"userId": "302019120764000020",
	"auditLink": "809001"
}
返回报文：
成功

```
