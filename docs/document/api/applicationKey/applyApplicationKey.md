

**1\. 手机端申请钥匙**
###### 接口功能
> 手机端申请钥匙接口

###### URL
> [http://order-service/orderApi/service](http://order-service/orderApi/service)

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
|name|1|String|64|姓名|-|
|communityId|1|String|30|小区ID|-|
|tel|1|String|12|手机号|-|
|typeCd|1|String|12|类型|10001 保洁 10002 保安 10003 其他人员，详情查看t_dict表|
|sex|1|String|2|性别|申请人男女 0男 1女|
|age|1|int|11|申请人年龄|-|
|idCard|1|String|20|申请人身份证号|-|
|startTime|1|timestamp|-|开始时间|-|
|endTime|1|timestamp|-|结束时间|-|
|machineIds|1|String|30|设备ID|-|
|photos|1|String|longtext|结束时间|-|
|typeFlag|1|String|12|人脸申请1100101 固定密码申请 1100102 临时密码 1100103|-|





###### 返回协议(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|pwd|1|String|6|访客密码|-|
|endTime|1|String|16|结束时间|-|

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[待补充](待补充)

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

{
    待补充
}

```
