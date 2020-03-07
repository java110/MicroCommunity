

**1\. 查询费用配置(初始化物业费)**
###### 接口功能
> 查询费用接口

###### URL
> [http://api.java110.com:8008/api/fee.queryFeeConfig](http://api.java110.com:8008/api/fee.queryFeeConfig)

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
|communityId|1|String|30|小区ID|-|
|feeTypeCd|1|String|12|费用类型|物业费，停车费|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，



###### 举例
> 地址：[http://api.java110.com:8008/api/fee.queryFeeConfig?feeTypeCd=888800010001&communityId=7020181217000001](http://api.java110.com:8008/api/fee.queryFeeConfig?feeTypeCd=888800010001&communityId=7020181217000001)

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
   	"squarePrice": "7.00",
   	"total": 0,
   	"feeTypeCd": "888800010001",
   	"records": 0,
   	"additionalAmount": "300.00",
   	"configId": "922019060280800001",
   	"statusCd": "0",
   	"page": -1,
   	"row": 0,
   	"communityId": "7020181217000001"
   }

```
