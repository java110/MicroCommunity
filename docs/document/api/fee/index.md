## 2.6.1 说明

## 2.6.2 查询物业费


## 2.6.3 物业费缴费明细

## 2.6.4 预交费接口

## 2.6.5 确认缴费接口

## 2.6.7 查询停车费

## 2.6.8 查询业主欠费

###### 接口功能
> 用户通过web端或APP 查询业主欠费情况

###### URL
> [http://api.java110.com:8008/api/fee.listOwnerOweFee](http://api.java110.com:8008/api/fee.listOwnerOweFee)

###### 支持格式
> JSON

###### HTTP请求方式
> GET

###### 请求参数(body部分)
|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: |
|communityId|1|String|30|小区ID|-|
|ownerId|1|String|30|业主ID|业主ID|

###### 返回协议

|父节点|参数名称|约束|类型|长度|描述|取值说明|
| :-: | :-: | :-: | :-: | :-: | :-: | :-:|
|-|code|1|int|-|编码|0成功，其他失败|
|-|msg|1|String|200|描述，失败时描述失败信息|描述，失败时描述失败信息|
|-|total|1|int|-|总记录数|总记录数|
|-|data|1|Array|-|数据信息|
|data|feeId|1|String|30|费用ID|费用ID|
|data|feeName|1|String|200|费用名称|费用名称|
|data|oweFee|1|double|-|欠费金额|欠费金额|
|data|payObjName|1|String|50|欠费对象|房屋时 房屋名称 车位时 车位名称|
|data|payObjId|1|String|50|欠费对象Id|房屋时 房屋ID 车位时 车位Id|
|data|payerObjType|1|String|12|欠费对象类型|房屋时 3333 车位时 6666|



###### 举例
> 地址：[http://api.java110.com:8008/api/fee.listOwnerOweFee?communityId=&ownerId=](http://api.java110.com:8008/api/fee.listOwnerOweFee?communityId=&ownerId=)

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
{"code":0,"msg":"成功","total":1,data:[{
"feeId":"",
"feeName":"",
"oweFee":"",
"payObjName":"",
"payObjId":"",
"payerObjType":""
}]}

```