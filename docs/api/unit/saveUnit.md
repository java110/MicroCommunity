

**1\. 保存小区单元信息**
###### 接口功能
> 用户通过web端或APP保存单元信息接口

###### URL
> [http://api.java110.com:8008/api/unit.saveUnit](http://api.java110.com:8008/api/unit.saveUnit)

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
|floorId|1|String|30|小区楼ID|-|
|communityId|1|String|30|小区ID|-|
|unitNum|1|String|12|单元编号|-|
|layerCount|1|int|-|楼总层数|-|
|lift|1|String|4|是否有楼梯|1010 有 2020 无|
|layerCount|1|int|-|楼总层数|-|
|remark|?|string|200|备注|-|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/unit.saveUnit](http://api.java110.com:8008/api/unit.saveUnit)

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
    "floorId":"填写具体值",
     "layerCount":"填写具体值",
     "unitId":"填写具体值",
     "unitNum":"填写具体值",
     "lift":"填写具体值",
     "remark":"填写具体值"
}

返回报文：
成功

```
