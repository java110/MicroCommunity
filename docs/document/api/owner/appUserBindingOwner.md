

**1\. app用户绑定业主信息**
###### 接口功能
> 用户通过app用户绑定业主信息

###### URL
> [http://api.java110.com:8008/api/owner.appUserBindingOwner](http://api.java110.com:8008/api/owner.appUserBindingOwner)

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
|userId|1|String|30|用户ID|-|

###### 请求参数
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityName|1|String|60|小区名称|-|
|areaCode|1|String|12|地区编码|-|
|appUserName|1|String|100|业主名称|-|
|idCard|1|String|20|身份证号码|-|
|link|1|String|11|手机号|-|
|remark|?|String|200|备注|-|


###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/owner.appUserBindingOwner](http://api.java110.com:8008/api/owner.appUserBindingOwner)

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
    "communityName":"格兰小镇",
    "areaCode":"123123",
    "appUserName":"用户名称",
    "idCard":"身份证号",
    "link":"联系电话",
    "remark":"123123"
}

返回报文：
成功

```
