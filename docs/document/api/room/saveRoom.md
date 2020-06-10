

**1\. 保存房屋信息**
###### 接口功能
> 用户通过web端或APP保存单元信息接口

###### URL
> [http://api.java110.com:8008/api/room.saveRoom](http://api.java110.com:8008/api/room.saveRoom)

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
|unitPrice|1|String|4|房屋单价|-|
|section|1|String|4|房间数|-|
|remark|1|String|200|备注|-|
|userId|1|String|30|用户ID|-|
|communityId|1|String|30|小区ID|-|
|layer|1|String|30|房屋楼层|-|
|builtUpArea|1|String|30|建筑面积| 如 97.98|
|roomNum|1|String|12|房间编号|1123|
|unitId|1|String|30|小区单元ID|-|
|state|1|String|12|房屋状态|2002 空闲状态|
|apartment|1|String|4|户型|1010 一室一厅 1020 一室两厅 2010 两室一厅 2020 两室两厅 3020 三室两厅|

###### 返回协议

当http返回状态不为200 时请求处理失败 body内容为失败的原因

当http返回状态为200时请求处理成功，body内容为返回内容，

成功


###### 举例
> 地址：[http://api.java110.com:8008/api/room.saveRoom](http://api.java110.com:8008/api/room.saveRoom)

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
    "communityId":"小区ID",
    "unitPrice":"填写具体值",
    "section":"填写具体值",
    "remark":"填写具体值",
    "layer":"填写具体值",
    "builtUpArea":"填写具体值",
    "roomNum":"填写具体值",
    "unitId":"填写具体值",
    "apartment":"填写具体值"
}

返回报文：
成功

```
