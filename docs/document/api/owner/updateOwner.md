

**1\. 修改业主信息**
###### 接口功能
> 用户通过web端或APP修改业主信息接口

###### URL
> [http://api.java110.com:8008/api/owner.updateOwner](http://api.java110.com:8008/api/owner.updateOwner)

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

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|sex|1|String|30|-|-|
|name|1|String|30|-|-|
|link|1|String|30|-|-|
|remark|1|String|30|-|-|
|ownerId|1|String|30|-|-|
|userId|1|String|30|-|-|
|ownerTypeCd|1|String|4|业主类型|1001 业主 1002 家庭成员|
|age|1|String|30|-|-|
|memberId|1|String|30|-|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/owner.updateOwner](http://api.java110.com:8008/api/owner.updateOwner)

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
    "memberId":"12313",
    "ownerId":"12313",
    "sex":"填写具体值",
    "name":"填写具体值",
    "link":"填写具体值",
    "remark":"填写具体值",
     "ownerTypeCd":"1001",
    "userId":"填写具体值",
    "age":"填写具体值"
}

返回报文：
成功

```
